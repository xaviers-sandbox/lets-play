package com.inventory.producer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.inventory.producer.mapper.InventoryEventMapper;
import com.inventory.producer.model.ErrorDTOResponse;
import com.inventory.producer.model.InventoryEventDTO;
import com.inventory.producer.model.InventoryEventDTOResponse;
import com.inventory.producer.model.ResponseDTO;
import com.inventory.producer.record.InventoryEvent;
import com.inventory.producer.service.InventoryEventService;
import com.inventory.producer.util.InventoryEventUtils;
import com.sandbox.util.SandboxUtils;

import lombok.extern.slf4j.Slf4j;

@AutoConfigureWebTestClient
@WebMvcTest(controllers = InventoryEventController.class)
@Slf4j
@ActiveProfiles("test")
public class InventoryEventControllerTest {
	private static final String INVENTORY_EVENTS_URL = "/v1/inventory-events/app";

	private static final int TEST_LIST_SIZE = 2;

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private InventoryEventService inventoryEventService;

	@Test
	void postItemEvents_test_200() {
		log.debug("\n\npostItemEvents_test_200");

		List<InventoryEvent> inventoryEventListMock = InventoryEventUtils.generateInventoryEventList(TEST_LIST_SIZE);

		List<InventoryEventDTO> inventoryEventDTOList = inventoryEventListMock.stream()
				.map(InventoryEventMapper::buildInventoryEventDTO)
				.collect(Collectors.toList());

		ResponseDTO responseDTO = InventoryEventMapper.buildResponseDTO(inventoryEventDTOList);

		ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

		when(inventoryEventService.addNewMockItems(anyInt()))
				.thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(responseDTO));

		webTestClient.post()
				.uri(INVENTORY_EVENTS_URL + "/init-test-data/{size}", TEST_LIST_SIZE)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(InventoryEventDTOResponse.class)
				.consumeWith(response -> {
					InventoryEventDTOResponse inventoryEventDTOResponse = response.getResponseBody();

					List<InventoryEventDTO> responseList = inventoryEventDTOResponse.getItemInventoryDTOList();
					assertNotNull(responseList);
					assertEquals(TEST_LIST_SIZE, responseList.size());

					responseList.forEach(i -> {
						assertNotNull(i.getEventId());
						assertNotNull(i.getEventType());
						assertNotNull(i.getItemDTO());
					});
				})
				.value(SandboxUtils::prettyPrintObjectToJson);
	}

	@Test
	void postItemEvents_test_400() {
		log.debug("\n\npostItemEvents_test_400");

		webTestClient.post()
				.uri(INVENTORY_EVENTS_URL + "/init-test-data/{size}", "oiajfaoijf")
				.exchange()
				.expectStatus()
				.is4xxClientError()
				.expectBody(ErrorDTOResponse.class)
				.consumeWith(response -> {
					ErrorDTOResponse errorDTOResponse = response.getResponseBody();
					assertNotNull(errorDTOResponse);
					assertTrue(errorDTOResponse.getErrorMessage().contains("Is Not Numeric"));
					assertEquals(400, errorDTOResponse.getErrorCode());
					assertEquals("BAD_REQUEST", errorDTOResponse.getStatus());

				})
				.value(SandboxUtils::prettyPrintObjectToJson);
	}
}
