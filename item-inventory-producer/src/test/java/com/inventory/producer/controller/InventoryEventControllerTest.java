package com.inventory.producer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

import com.inventory.producer.enums.InventoryEventType;
import com.inventory.producer.mapper.InventoryEventMapper;
import com.inventory.producer.model.InventoryEventDTO;
import com.inventory.producer.model.record.InventoryEventRecord;
import com.inventory.producer.model.request.InventoryEventDTORequest;
import com.inventory.producer.model.request.ItemDTORequest;
import com.inventory.producer.model.response.ErrorDTOResponse;
import com.inventory.producer.model.response.InventoryEventDTOResponse;
import com.inventory.producer.model.response.ResponseDTO;
import com.inventory.producer.service.InventoryEventService;
import com.item.inventory.test.utils.ItemInventoryTestUtils;
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
	void addNewMockItems_test_200() {
		log.debug("\n\naddNewMockItems_test_200");

		List<InventoryEventRecord> inventoryEventRecordListMock = ItemInventoryTestUtils
				.generateInventoryEventRecordList(TEST_LIST_SIZE);

		List<InventoryEventDTO> inventoryEventDTOList = inventoryEventRecordListMock.stream()
				.map(InventoryEventMapper::mapInventoryEventRecordToInventoryEventDTO)
				.collect(Collectors.toList());

		ResponseDTO responseDTO = InventoryEventMapper.buildResponseDTO(inventoryEventDTOList);

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

		verify(inventoryEventService).addNewMockItems(anyInt());
	}

	@Test
	void addNewMockItems_test_400_bad_path_variable() {
		log.debug("\n\naddNewMockItems_test_400_bad_path_variable");

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

	@Test
	void addNewItemInventory_test_200() {
		log.debug("\n\naddNewItemInventory_test_200");

		ItemDTORequest item = ItemDTORequest.builder().name("new test item").price(11.99).quantity(50).build();

		InventoryEventDTORequest inventoryEventDTORequest = InventoryEventDTORequest.builder()
				.eventType(InventoryEventType.NEW)
				.item(item)
				.build();

		InventoryEventRecord inventoryEventRecordListMock = ItemInventoryTestUtils.generateInventoryEventRecordList(1)
				.getFirst();

		InventoryEventDTO inventoryEventDTOMock = InventoryEventMapper
				.mapInventoryEventRecordToInventoryEventDTO(inventoryEventRecordListMock);

		ResponseDTO responseDTO = InventoryEventMapper
				.buildResponseDTO(Collections.singletonList(inventoryEventDTOMock));

		when(inventoryEventService.addNewItemInventory(any(InventoryEventDTORequest.class)))
				.thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(responseDTO));

		SandboxUtils.prettyPrintObjectToJson(inventoryEventDTORequest);

		webTestClient.post()
				.uri(INVENTORY_EVENTS_URL + "/create")
				.bodyValue(inventoryEventDTORequest)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(InventoryEventDTOResponse.class)
				.consumeWith(response -> {
					InventoryEventDTOResponse inventoryEventDTOResponse = response.getResponseBody();

					List<InventoryEventDTO> responseList = inventoryEventDTOResponse.getItemInventoryDTOList();
					assertNotNull(responseList);
					assertEquals(1, responseList.size());

					responseList.stream().findFirst().stream().forEach(responseInventoryEventDTO -> {
						assertNotNull(responseInventoryEventDTO);
						assertEquals(inventoryEventDTOMock.getEventId(), responseInventoryEventDTO.getEventId());
						assertEquals(inventoryEventDTOMock.getEventType(), responseInventoryEventDTO.getEventType());
						assertNotNull(inventoryEventDTOMock.getItemDTO());

						assertEquals(inventoryEventDTOMock.getItemDTO().getItemId(),
								responseInventoryEventDTO.getItemDTO().getItemId());
						assertEquals(inventoryEventDTOMock.getItemDTO().getPrice(),
								responseInventoryEventDTO.getItemDTO().getPrice());
						assertEquals(inventoryEventDTOMock.getItemDTO().getQuantity(),
								responseInventoryEventDTO.getItemDTO().getQuantity());
						assertEquals(inventoryEventDTOMock.getItemDTO().getName(),
								responseInventoryEventDTO.getItemDTO().getName());
					});
				})
				.value(SandboxUtils::prettyPrintObjectToJson);

		verify(inventoryEventService).addNewItemInventory(any(InventoryEventDTORequest.class));
	}

	@Test
	void addNewItemInventory_test_400_no_name() {
		log.debug("\n\naddNewItemInventory_test_400_no_name");

		ItemDTORequest item = ItemDTORequest.builder().name(null).price(11.99).quantity(50).build();

		InventoryEventDTORequest inventoryEventDTORequest = InventoryEventDTORequest.builder()
				.eventType(InventoryEventType.NEW)
				.item(item)
				.build();

		webTestClient.post()
				.uri(INVENTORY_EVENTS_URL + "/create")
				.bodyValue(inventoryEventDTORequest)
				.exchange()
				.expectStatus()
				.is4xxClientError()
				.expectBody(ErrorDTOResponse.class)
				.consumeWith(response -> {
					ErrorDTOResponse errorDTOResponse = response.getResponseBody();
					SandboxUtils.prettyPrintObjectToJson(errorDTOResponse);
					assertNotNull(errorDTOResponse);
					assertEquals("Name Cannot Be Empty or Null", errorDTOResponse.getErrorMessage());
					assertEquals(400, errorDTOResponse.getErrorCode());
					assertEquals("BAD_REQUEST", errorDTOResponse.getStatus());

				})
				.value(SandboxUtils::prettyPrintObjectToJson);
	}

	@Test
	void addNewItemInventory_test_400_no_price() {
		log.debug("\n\naddNewItemInventory_test_400_no_price");

		ItemDTORequest item = ItemDTORequest.builder().name("new test item").price(null).quantity(50).build();

		InventoryEventDTORequest inventoryEventDTORequest = InventoryEventDTORequest.builder()
				.eventType(InventoryEventType.NEW)
				.item(item)
				.build();

		webTestClient.post()
				.uri(INVENTORY_EVENTS_URL + "/create")
				.bodyValue(inventoryEventDTORequest)
				.exchange()
				.expectStatus()
				.is4xxClientError()
				.expectBody(ErrorDTOResponse.class)
				.consumeWith(response -> {
					ErrorDTOResponse errorDTOResponse = response.getResponseBody();
					SandboxUtils.prettyPrintObjectToJson(errorDTOResponse);
					assertNotNull(errorDTOResponse);
					assertEquals("Price Cannot Be Empty or Null", errorDTOResponse.getErrorMessage());
					assertEquals(400, errorDTOResponse.getErrorCode());
					assertEquals("BAD_REQUEST", errorDTOResponse.getStatus());

				})
				.value(SandboxUtils::prettyPrintObjectToJson);
	}

	@Test
	void addNewItemInventory_test_400_no_quantity() {
		log.debug("\n\naddNewItemInventory_test_400_no_quantity");

		ItemDTORequest item = ItemDTORequest.builder().name("new test item").price(11.99).quantity(null).build();

		InventoryEventDTORequest inventoryEventDTORequest = InventoryEventDTORequest.builder()
				.eventType(InventoryEventType.NEW)
				.item(item)
				.build();

		webTestClient.post()
				.uri(INVENTORY_EVENTS_URL + "/create")
				.bodyValue(inventoryEventDTORequest)
				.exchange()
				.expectStatus()
				.is4xxClientError()
				.expectBody(ErrorDTOResponse.class)
				.consumeWith(response -> {
					ErrorDTOResponse errorDTOResponse = response.getResponseBody();
					SandboxUtils.prettyPrintObjectToJson(errorDTOResponse);
					assertNotNull(errorDTOResponse);
					assertEquals("Quantity Cannot Be Empty or Null", errorDTOResponse.getErrorMessage());
					assertEquals(400, errorDTOResponse.getErrorCode());
					assertEquals("BAD_REQUEST", errorDTOResponse.getStatus());

				})
				.value(SandboxUtils::prettyPrintObjectToJson);
	}

	@Test
	void addNewItemInventory_test_400_no_item() {
		log.debug("\n\naddNewItemInventory_test_400_no_item");

		InventoryEventDTORequest inventoryEventDTORequest = InventoryEventDTORequest.builder()
				.eventType(InventoryEventType.NEW)
				.item(null)
				.build();

		webTestClient.post()
				.uri(INVENTORY_EVENTS_URL + "/create")
				.bodyValue(inventoryEventDTORequest)
				.exchange()
				.expectStatus()
				.is4xxClientError()
				.expectBody(ErrorDTOResponse.class)
				.consumeWith(response -> {
					ErrorDTOResponse errorDTOResponse = response.getResponseBody();
					SandboxUtils.prettyPrintObjectToJson(errorDTOResponse);
					assertNotNull(errorDTOResponse);
					assertEquals("Item Cannot Be Empty or Null", errorDTOResponse.getErrorMessage());
					assertEquals(400, errorDTOResponse.getErrorCode());
					assertEquals("BAD_REQUEST", errorDTOResponse.getStatus());

				})
				.value(SandboxUtils::prettyPrintObjectToJson);
	}

	@Test
	void addNewItemInventory_test_400_empty_requestBody() {
		log.debug("\n\naddNewItemInventory_test_400_empty_requestBody");

		webTestClient.post()
				.uri(INVENTORY_EVENTS_URL + "/create")
				.bodyValue(Optional.empty())
				.exchange()
				.expectStatus()
				.is4xxClientError()
				.expectBody(ErrorDTOResponse.class)
				.consumeWith(response -> {
					ErrorDTOResponse errorDTOResponse = response.getResponseBody();
					SandboxUtils.prettyPrintObjectToJson(errorDTOResponse);
					assertNotNull(errorDTOResponse);
					assertEquals("Invalid Request Body", errorDTOResponse.getErrorMessage());
					assertEquals(400, errorDTOResponse.getErrorCode());
					assertEquals("BAD_REQUEST", errorDTOResponse.getStatus());

				})
				.value(SandboxUtils::prettyPrintObjectToJson);
	}

	@Test
	void updateItemInventory_test_200() {
		log.debug("\n\nupdateItemInventory_test_200");

		ItemDTORequest item = ItemDTORequest.builder().name("new test item").price(11.99).quantity(50).build();

		InventoryEventDTORequest inventoryEventDTORequest = InventoryEventDTORequest.builder()
				.eventType(InventoryEventType.NEW)
				.item(item)
				.build();

		InventoryEventRecord inventoryEventRecordListMock = ItemInventoryTestUtils.generateInventoryEventRecordList(1)
				.getFirst();

		InventoryEventDTO inventoryEventDTOMock = InventoryEventMapper
				.mapInventoryEventRecordToInventoryEventDTO(inventoryEventRecordListMock);

		ResponseDTO responseDTO = InventoryEventMapper
				.buildResponseDTO(Collections.singletonList(inventoryEventDTOMock));

		when(inventoryEventService.updateItemInventory(anyString(), any(InventoryEventDTORequest.class)))
				.thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(responseDTO));

		SandboxUtils.prettyPrintObjectToJson(inventoryEventDTORequest);

		webTestClient.put()
				.uri(INVENTORY_EVENTS_URL + "/update/{eventId}", "testEventId")
				.bodyValue(inventoryEventDTORequest)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(InventoryEventDTOResponse.class)
				.consumeWith(response -> {
					InventoryEventDTOResponse inventoryEventDTOResponse = response.getResponseBody();

					List<InventoryEventDTO> responseList = inventoryEventDTOResponse.getItemInventoryDTOList();
					assertNotNull(responseList);
					assertEquals(1, responseList.size());

					responseList.stream().findFirst().stream().forEach(responseInventoryEventDTO -> {
						assertNotNull(responseInventoryEventDTO);
						assertEquals(inventoryEventDTOMock.getEventId(), responseInventoryEventDTO.getEventId());
						assertEquals(inventoryEventDTOMock.getEventType(), responseInventoryEventDTO.getEventType());
						assertNotNull(inventoryEventDTOMock.getItemDTO());

						assertEquals(inventoryEventDTOMock.getItemDTO().getItemId(),
								responseInventoryEventDTO.getItemDTO().getItemId());
						assertEquals(inventoryEventDTOMock.getItemDTO().getPrice(),
								responseInventoryEventDTO.getItemDTO().getPrice());
						assertEquals(inventoryEventDTOMock.getItemDTO().getQuantity(),
								responseInventoryEventDTO.getItemDTO().getQuantity());
						assertEquals(inventoryEventDTOMock.getItemDTO().getName(),
								responseInventoryEventDTO.getItemDTO().getName());
					});
				})
				.value(SandboxUtils::prettyPrintObjectToJson);

		verify(inventoryEventService).updateItemInventory(anyString(), any(InventoryEventDTORequest.class));
	}

	@Test
	void updateItemInventory_test_400_no_name() {
		log.debug("\n\nupdateItemInventory_test_400_no_name");

		ItemDTORequest item = ItemDTORequest.builder().name(null).price(11.99).quantity(50).build();

		InventoryEventDTORequest inventoryEventDTORequest = InventoryEventDTORequest.builder()
				.eventType(InventoryEventType.NEW)
				.item(item)
				.build();

		SandboxUtils.prettyPrintObjectToJson(inventoryEventDTORequest);

		webTestClient.put()
				.uri(INVENTORY_EVENTS_URL + "/update/{eventId}", "testEventId")
				.bodyValue(inventoryEventDTORequest)
				.exchange()
				.expectStatus()
				.is4xxClientError()
				.expectBody(ErrorDTOResponse.class)
				.consumeWith(response -> {
					ErrorDTOResponse errorDTOResponse = response.getResponseBody();
					SandboxUtils.prettyPrintObjectToJson(errorDTOResponse);
					assertNotNull(errorDTOResponse);
					assertEquals("Name Cannot Be Empty or Null", errorDTOResponse.getErrorMessage());
					assertEquals(400, errorDTOResponse.getErrorCode());
					assertEquals("BAD_REQUEST", errorDTOResponse.getStatus());

				})
				.value(SandboxUtils::prettyPrintObjectToJson);
	}

	@Test
	void updateItemInventory_test_400_no_price() {
		log.debug("\n\nupdateItemInventory_test_400_no_name");

		ItemDTORequest item = ItemDTORequest.builder().name("new test item").price(null).quantity(50).build();

		InventoryEventDTORequest inventoryEventDTORequest = InventoryEventDTORequest.builder()
				.eventType(InventoryEventType.NEW)
				.item(item)
				.build();

		SandboxUtils.prettyPrintObjectToJson(inventoryEventDTORequest);

		webTestClient.put()
				.uri(INVENTORY_EVENTS_URL + "/update/{eventId}", "testEventId")
				.bodyValue(inventoryEventDTORequest)
				.exchange()
				.expectStatus()
				.is4xxClientError()
				.expectBody(ErrorDTOResponse.class)
				.consumeWith(response -> {
					ErrorDTOResponse errorDTOResponse = response.getResponseBody();
					SandboxUtils.prettyPrintObjectToJson(errorDTOResponse);
					assertNotNull(errorDTOResponse);
					assertEquals("Price Cannot Be Empty or Null", errorDTOResponse.getErrorMessage());
					assertEquals(400, errorDTOResponse.getErrorCode());
					assertEquals("BAD_REQUEST", errorDTOResponse.getStatus());

				})
				.value(SandboxUtils::prettyPrintObjectToJson);
	}

	@Test
	void updateItemInventory_test_400_no_quantity() {
		log.debug("\n\nupdateItemInventory_test_400_no_quantity");

		ItemDTORequest item = ItemDTORequest.builder().name("new test item").price(11.99).quantity(null).build();

		InventoryEventDTORequest inventoryEventDTORequest = InventoryEventDTORequest.builder()
				.eventType(InventoryEventType.NEW)
				.item(item)
				.build();

		SandboxUtils.prettyPrintObjectToJson(inventoryEventDTORequest);

		webTestClient.put()
				.uri(INVENTORY_EVENTS_URL + "/update/{eventId}", "testEventId")
				.bodyValue(inventoryEventDTORequest)
				.exchange()
				.expectStatus()
				.is4xxClientError()
				.expectBody(ErrorDTOResponse.class)
				.consumeWith(response -> {
					ErrorDTOResponse errorDTOResponse = response.getResponseBody();
					SandboxUtils.prettyPrintObjectToJson(errorDTOResponse);
					assertNotNull(errorDTOResponse);
					assertEquals("Quantity Cannot Be Empty or Null", errorDTOResponse.getErrorMessage());
					assertEquals(400, errorDTOResponse.getErrorCode());
					assertEquals("BAD_REQUEST", errorDTOResponse.getStatus());

				})
				.value(SandboxUtils::prettyPrintObjectToJson);
	}

	@Test
	void updateItemInventory_test_400_empty_requestBody() {
		log.debug("\n\nupdateItemInventory_test_400_empty_requestBody");

		webTestClient.put()
				.uri(INVENTORY_EVENTS_URL + "/update/{eventId}", "testEventId")
				.bodyValue(Optional.empty())
				.exchange()
				.expectStatus()
				.is4xxClientError()
				.expectBody(ErrorDTOResponse.class)
				.consumeWith(response -> {
					ErrorDTOResponse errorDTOResponse = response.getResponseBody();
					SandboxUtils.prettyPrintObjectToJson(errorDTOResponse);
					assertNotNull(errorDTOResponse);
					assertEquals("Invalid Request Body", errorDTOResponse.getErrorMessage());
					assertEquals(400, errorDTOResponse.getErrorCode());
					assertEquals("BAD_REQUEST", errorDTOResponse.getStatus());

				})
				.value(SandboxUtils::prettyPrintObjectToJson);
	}
}
