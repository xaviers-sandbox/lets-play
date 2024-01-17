package com.item.inventory.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.item.inventory.model.ItemInventoryDTO;
import com.item.inventory.model.request.ItemInventoryDTORequest;
import com.item.inventory.model.response.ErrorDTOResponse;
import com.item.inventory.model.response.ItemInventoryDTOResponse;
import com.item.inventory.service.ItemInventoryService;
import com.item.inventory.util.ItemInventoryTestUtil;
import com.item.inventory.util.ItemInventoryUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@AutoConfigureWebTestClient
@WebFluxTest(controllers = ItemInventoryController.class)
@Slf4j
@ActiveProfiles("test")
public class ItemInventoryControllerTest {
	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private ItemInventoryService itemInventoryServiceMock;

	static final String ITEM_INVENTORIES_URL = "/v1/item-inventories";

	private List<ItemInventoryDTO> ItemInventoryDTOListMock;

	static final int RANGE_MAX = 2;

	static final String TEST_ITEM_ID = "123456";

	@BeforeEach
	void initData() {
		log.debug("\n\nInitializing Test Data");
		ItemInventoryDTOListMock = ItemInventoryTestUtil.generateItemInventoryDTOList(RANGE_MAX);

		ItemInventoryDTOListMock.get(RANGE_MAX - 1).setId(TEST_ITEM_ID);

		log.debug("Initialize Test Data Complete size={}", RANGE_MAX);
		ItemInventoryDTOListMock.forEach(i -> log.debug(i.toString()));
	}

	@Test
	// @RepeatedTest(3)
	public void addNewItemInventoryTest() {
		log.debug("\n\naddNewItemInventoryTest Unit Test");

		ItemInventoryDTORequest itemInventoryDTORequestMock = ItemInventoryTestUtil.buildMockItemInventoryDTORequest();
		itemInventoryDTORequestMock.setId(TEST_ITEM_ID);

		ItemInventoryDTOResponse itemInventoryDTOResponseMock = ItemInventoryTestUtil
				.buildMockItemInventoryDTOResponse(1);
		itemInventoryDTOResponseMock.getItemInventoryDTOList().stream().findFirst().get().setId(TEST_ITEM_ID);

		when(itemInventoryServiceMock.addNewItemInventory(any()))
				.thenReturn(Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(itemInventoryDTOResponseMock)));

		webTestClient.post()
				.uri(ITEM_INVENTORIES_URL)
				.bodyValue(itemInventoryDTORequestMock)
				.exchange()
				.expectStatus()
				.isCreated()
				.expectBody(ItemInventoryDTOResponse.class)
				.consumeWith(response -> {
					ItemInventoryDTOResponse itemInventoryDTOResponse = response.getResponseBody();

					assertNotNull(itemInventoryDTOResponse);
					assertEquals(1, itemInventoryDTOResponse.getResultSetSize());
					assertEquals(itemInventoryDTOResponseMock.getResultSetSize(),
							itemInventoryDTOResponse.getResultSetSize());
					assertEquals(itemInventoryDTOResponseMock.getItemInventoryDTOList().size(),
							itemInventoryDTOResponse.getItemInventoryDTOList().size());
					assertEquals(
							itemInventoryDTOResponseMock.getItemInventoryDTOList().stream().findFirst().get().getName(),
							itemInventoryDTOResponse.getItemInventoryDTOList().stream().findFirst().get().getName());
				})
				.value(ItemInventoryUtil::prettyPrintObjectToJson);
	}

	@Test
	// @RepeatedTest(3)
	public void getAllItemInventoriesTest() {
		log.debug("\n\ngetAllItemInventoriesTest Unit Test");

		ItemInventoryDTOResponse itemInventoryDTOResponseMock = ItemInventoryTestUtil
				.buildMockItemInventoryDTOResponse(5);

		when(itemInventoryServiceMock.getAllItemInventories())
				.thenReturn(Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(itemInventoryDTOResponseMock)));

		webTestClient.get()
				.uri(ITEM_INVENTORIES_URL)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(ItemInventoryDTOResponse.class)
				.consumeWith(response -> {
					ItemInventoryDTOResponse itemInventoryDTOResponse = response.getResponseBody();

					assertNotNull(itemInventoryDTOResponse);
					assertEquals(5, itemInventoryDTOResponse.getResultSetSize());
					assertEquals(itemInventoryDTOResponseMock.getResultSetSize(),
							itemInventoryDTOResponse.getResultSetSize());
					assertEquals(itemInventoryDTOResponseMock.getItemInventoryDTOList().size(),
							itemInventoryDTOResponse.getItemInventoryDTOList().size());
					assertEquals(itemInventoryDTOResponseMock.getItemInventoryDTOList().get(4).getName(),
							itemInventoryDTOResponse.getItemInventoryDTOList().get(4).getName());
				})
				.value(ItemInventoryUtil::prettyPrintObjectToJson);
	}

	@Test
	// @RepeatedTest(3)
	public void getItemInventoryByIdTest() {
		log.debug("\n\ngetItemInventoryByIdTest Unit Test- id=" + TEST_ITEM_ID);

		ItemInventoryDTOResponse itemInventoryDTOResponseMock = ItemInventoryTestUtil
				.buildMockItemInventoryDTOResponse(1);
		itemInventoryDTOResponseMock.getItemInventoryDTOList().stream().findFirst().get().setId(TEST_ITEM_ID);

		when(itemInventoryServiceMock.getItemInventoryById(any()))
				.thenReturn(Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(itemInventoryDTOResponseMock)));

		webTestClient.get()
				.uri(ITEM_INVENTORIES_URL + "/{id}", TEST_ITEM_ID)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(ItemInventoryDTOResponse.class)
				.consumeWith(response -> {
					ItemInventoryDTOResponse itemInventoryDTOResponse = response.getResponseBody();

					assertNotNull(itemInventoryDTOResponse);
					assertEquals(1, itemInventoryDTOResponse.getResultSetSize());
					assertEquals(itemInventoryDTOResponseMock.getResultSetSize(),
							itemInventoryDTOResponse.getResultSetSize());
					assertEquals(itemInventoryDTOResponseMock.getItemInventoryDTOList().size(),
							itemInventoryDTOResponse.getItemInventoryDTOList().size());
					assertEquals(TEST_ITEM_ID,
							itemInventoryDTOResponse.getItemInventoryDTOList().stream().findFirst().get().getId());
					assertEquals(
							itemInventoryDTOResponseMock.getItemInventoryDTOList().stream().findFirst().get().getId(),
							itemInventoryDTOResponse.getItemInventoryDTOList().stream().findFirst().get().getId());
				})
				.value(ItemInventoryUtil::prettyPrintObjectToJson);
	}

	@Test
	// @RepeatedTest(3)
	public void updateItemInventoryIdTest() {
		log.debug("\n\nupdateItemInventoryIdTest Unit Test - id=" + TEST_ITEM_ID);

		ItemInventoryDTORequest updatedItemInventoryDTORequestMock = ItemInventoryTestUtil
				.buildMockItemInventoryDTORequest();
		updatedItemInventoryDTORequestMock.setId(TEST_ITEM_ID);

		ItemInventoryDTOResponse itemInventoryDTOResponseMock = ItemInventoryTestUtil
				.buildMockItemInventoryDTOResponse(1);
		itemInventoryDTOResponseMock.getItemInventoryDTOList().stream().findFirst().get().setId(TEST_ITEM_ID);

		when(itemInventoryServiceMock.updateItemInventoryById(any(), any()))
				.thenReturn(Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(itemInventoryDTOResponseMock)));

		webTestClient.put()
				.uri(ITEM_INVENTORIES_URL + "/{id}", TEST_ITEM_ID)
				.bodyValue(updatedItemInventoryDTORequestMock)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(ItemInventoryDTOResponse.class)
				.consumeWith(response -> {
					ItemInventoryDTOResponse itemInventoryDTOResponse = response.getResponseBody();

					assertNotNull(itemInventoryDTOResponse);
					assertEquals(1, itemInventoryDTOResponse.getResultSetSize());
					assertEquals(itemInventoryDTOResponseMock.getResultSetSize(),
							itemInventoryDTOResponse.getResultSetSize());
					assertEquals(itemInventoryDTOResponseMock.getItemInventoryDTOList().size(),
							itemInventoryDTOResponse.getItemInventoryDTOList().size());
					assertEquals(TEST_ITEM_ID,
							itemInventoryDTOResponse.getItemInventoryDTOList().stream().findFirst().get().getId());
					assertEquals(
							itemInventoryDTOResponseMock.getItemInventoryDTOList().stream().findFirst().get().getId(),
							itemInventoryDTOResponse.getItemInventoryDTOList().stream().findFirst().get().getId());
				})
				.value(ItemInventoryUtil::prettyPrintObjectToJson);
	}

	@Test
	// @RepeatedTest(3)
	public void deleteItemInventoryByIdTest() {
		log.debug("\n\ndeleteItemInventoryByIdTest - id=" + TEST_ITEM_ID);

		when(itemInventoryServiceMock.deleteItemInventoryById(any())).thenReturn(Mono.empty());

		webTestClient.delete()
				.uri(ITEM_INVENTORIES_URL + "/{id}", TEST_ITEM_ID)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(Void.class);
	}

	@Test
	// @RepeatedTest(3)
	public void addNewItemInventoryTest_empty_BAD_REQUESTS() {
		log.debug("\n\naddNewItemInventoryTest_empty_BAD_REQUESTS - ");

		ItemInventoryDTORequest itemInventoryDTORequestMock = ItemInventoryTestUtil.buildMockItemInventoryDTORequest();

		// empty name test
		itemInventoryDTORequestMock.setName("");

		webTestClient.post()
				.uri(ITEM_INVENTORIES_URL)
				.bodyValue(itemInventoryDTORequestMock)
				.exchange()
				.expectStatus()
				.isBadRequest()
				.expectBody(ErrorDTOResponse.class)
				.consumeWith(response -> {
					ErrorDTOResponse errorDTOResponse = response.getResponseBody();

					assertNotNull(errorDTOResponse);
					assertEquals(400, errorDTOResponse.getErrorCode());
					assertEquals("Name Cannot Be Empty or Null", errorDTOResponse.getErrorMessage());
					assertEquals("BAD_REQUEST", errorDTOResponse.getStatus());
				})
				.value(ItemInventoryUtil::prettyPrintObjectToJson);
	}

	@Test
	// @RepeatedTest(3)
	public void addNewItemInventoryTest_null_BAD_REQUESTS() {
		log.debug("\n\naddNewItemInventoryTest_null_BAD_REQUESTS - ");

		ItemInventoryDTORequest itemInventoryDTORequestMock = ItemInventoryTestUtil.buildMockItemInventoryDTORequest();

		// null name test
		itemInventoryDTORequestMock.setName(null);

		webTestClient.post()
				.uri(ITEM_INVENTORIES_URL)
				.bodyValue(itemInventoryDTORequestMock)
				.exchange()
				.expectStatus()
				.isBadRequest()
				.expectBody(ErrorDTOResponse.class)
				.consumeWith(response -> {
					ErrorDTOResponse errorDTOResponse = response.getResponseBody();

					assertNotNull(errorDTOResponse);
					assertEquals(400, errorDTOResponse.getErrorCode());
					assertEquals("Name Cannot Be Empty or Null", errorDTOResponse.getErrorMessage());
					assertEquals("BAD_REQUEST", errorDTOResponse.getStatus());
				})
				.value(ItemInventoryUtil::prettyPrintObjectToJson);

		itemInventoryDTORequestMock = ItemInventoryTestUtil.buildMockItemInventoryDTORequest();

		// null quantity test
		itemInventoryDTORequestMock.setQuantity(null);

		webTestClient.post()
				.uri(ITEM_INVENTORIES_URL)
				.bodyValue(itemInventoryDTORequestMock)
				.exchange()
				.expectStatus()
				.isBadRequest()
				.expectBody(ErrorDTOResponse.class)
				.consumeWith(response -> {
					ErrorDTOResponse errorDTOResponse = response.getResponseBody();

					assertNotNull(errorDTOResponse);
					assertEquals(400, errorDTOResponse.getErrorCode());
					assertEquals("Quantity Cannot Be Empty or Null", errorDTOResponse.getErrorMessage());
					assertEquals("BAD_REQUEST", errorDTOResponse.getStatus());
				})
				.value(ItemInventoryUtil::prettyPrintObjectToJson);

		itemInventoryDTORequestMock = ItemInventoryTestUtil.buildMockItemInventoryDTORequest();

		// null price test
		itemInventoryDTORequestMock.setPrice(null);

		webTestClient.post()
				.uri(ITEM_INVENTORIES_URL)
				.bodyValue(itemInventoryDTORequestMock)
				.exchange()
				.expectStatus()
				.isBadRequest()
				.expectBody(ErrorDTOResponse.class)
				.consumeWith(response -> {

					ErrorDTOResponse errorDTOResponse = response.getResponseBody();

					assertNotNull(errorDTOResponse);
					assertEquals(400, errorDTOResponse.getErrorCode());
					assertEquals("Price Cannot Be Empty or Null", errorDTOResponse.getErrorMessage());
					assertEquals("BAD_REQUEST", errorDTOResponse.getStatus());
				})
				.value(ItemInventoryUtil::prettyPrintObjectToJson);
	}

	@Test
	// @RepeatedTest(3)
	public void addNewItemInventoryTest_negative_BAD_REQUESTS() {
		log.debug("\n\naddNewItemInventoryTest_negative_BAD_REQUESTS - ");

		ItemInventoryDTORequest itemInventoryDTORequest = ItemInventoryTestUtil.buildMockItemInventoryDTORequest();

		// negative price test
		itemInventoryDTORequest.setPrice(-10.0);

		webTestClient.post()
				.uri(ITEM_INVENTORIES_URL)
				.bodyValue(itemInventoryDTORequest)
				.exchange()
				.expectStatus()
				.isBadRequest()
				.expectBody(ErrorDTOResponse.class)

				.consumeWith(response -> {
					ErrorDTOResponse errorResponseDTO = response.getResponseBody();

					assertNotNull(errorResponseDTO);
					assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponseDTO.getErrorCode());
					assertEquals("Price Cannot Be Negative", errorResponseDTO.getErrorMessage());
					assertEquals(HttpStatus.BAD_REQUEST.name(), errorResponseDTO.getStatus());
				})
				.value(ItemInventoryUtil::prettyPrintObjectToJson);

		itemInventoryDTORequest = ItemInventoryTestUtil.buildMockItemInventoryDTORequest();

		// negative quantity test
		itemInventoryDTORequest.setQuantity(-5);

		webTestClient.post()
				.uri(ITEM_INVENTORIES_URL)
				.bodyValue(itemInventoryDTORequest)
				.exchange()
				.expectStatus()
				.isBadRequest()
				.expectBody(ErrorDTOResponse.class)

				.consumeWith(response -> {
					ErrorDTOResponse errorResponseDTO = response.getResponseBody();

					assertNotNull(errorResponseDTO);
					assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponseDTO.getErrorCode());
					assertEquals("Quantity Cannot Be Negative", errorResponseDTO.getErrorMessage());
					assertEquals(HttpStatus.BAD_REQUEST.name(), errorResponseDTO.getStatus());
				})
				.value(ItemInventoryUtil::prettyPrintObjectToJson);
	}

	@Test
	// @RepeatedTest(3)
	public void isBetweenTester() {
		assertFalse(ItemInventoryTestUtil.isBetweenTwoNums(9, 10, 15));
		assertFalse(ItemInventoryTestUtil.isBetweenTwoNums(16, 10, 15));
		assertTrue(ItemInventoryTestUtil.isBetweenTwoNums(10, 10, 15));
		assertTrue(ItemInventoryTestUtil.isBetweenTwoNums(15, 10, 15));
	}
}
