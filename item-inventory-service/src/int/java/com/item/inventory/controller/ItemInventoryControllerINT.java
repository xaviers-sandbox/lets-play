package com.item.inventory.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.item.inventory.entity.ItemInventoryEntity;
import com.item.inventory.model.request.ItemInventoryDTORequest;
import com.item.inventory.model.response.ItemInventoryDTOResponse;
import com.item.inventory.repository.ItemInventoryRepository;
import com.item.inventory.util.ItemInventoryTestUtil;
import com.sandbox.util.SandboxUtils;

import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@Slf4j
//@Import(SecurityConfigs.class)
public class ItemInventoryControllerINT {
	@Autowired
	private ItemInventoryRepository itemInventoryRepo;

	@Autowired
	private WebTestClient webTestClient;

	static final String ITEM_INVENTORIES_URL = "/v1/item-inventories";

	static final int TEST_LIST_SIZE = 4;

	static final String TEST_ITEM_ID = "123456";

	@BeforeEach
	void initData() {
		log.debug("\n\nInitializing Test Data");
		List<ItemInventoryEntity> itemInventoryEntityList = ItemInventoryTestUtil
				.generateItemInventoryEntityList(TEST_LIST_SIZE);

		itemInventoryEntityList.get(TEST_LIST_SIZE - 1).setId(TEST_ITEM_ID);

		log.debug("Initialize Test Data Complete size={}", TEST_LIST_SIZE);
		itemInventoryEntityList.forEach(i -> log.debug(i.toString()));

		// block the thread to ensure this takes place before the test runs
		itemInventoryRepo.deleteAll().block();

		// block the thread to ensure this takes place before the test runs
		// itemInventoryRepo.saveAll(itemInventoryEntityList).blockLast();
	}

	@AfterEach
	void deleteData() {
		// block the thread to ensure this takes place before the test runs
		itemInventoryRepo.deleteAll().block();
	}

	@Test
	// @RepeatedTest(3)
	public void deleteDataTest() {
		// block the thread to ensure this takes place before the test runs
		itemInventoryRepo.deleteAll().block();
	}

	@Test
	// @RepeatedTest(3)
	public void addNewItemInventoryTest() {
		log.debug("\n\naddNewItemInventoryTest INT Test");
		String testItemId = new Faker().phoneNumber().cellPhone();

		ItemInventoryDTORequest itemInventoryDTORequestMock = ItemInventoryTestUtil.buildMockItemInventoryDTORequest();
		itemInventoryDTORequestMock.setId(testItemId);

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
					assertEquals(testItemId,
							itemInventoryDTOResponse.getItemInventoryDTOList().stream().findFirst().get().getId());
					assertEquals(itemInventoryDTORequestMock.getId(),
							itemInventoryDTOResponse.getItemInventoryDTOList().stream().findFirst().get().getId());

					assertEquals(itemInventoryDTORequestMock.getName(),
							itemInventoryDTOResponse.getItemInventoryDTOList().stream().findFirst().get().getName());

					assertEquals(itemInventoryDTORequestMock.getPrice(),
							itemInventoryDTOResponse.getItemInventoryDTOList().stream().findFirst().get().getPrice());

					assertEquals(itemInventoryDTORequestMock.getQuantity(),
							itemInventoryDTOResponse.getItemInventoryDTOList()
									.stream()
									.findFirst()
									.get()
									.getQuantity());

				})
				.value(SandboxUtils::prettyPrintObjectToJson);

		webTestClient.get()
				.uri(ITEM_INVENTORIES_URL)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(ItemInventoryDTOResponse.class)
				.consumeWith(response -> {
					ItemInventoryDTOResponse itemInventoryDTOResponse = response.getResponseBody();

					assertNotNull(itemInventoryDTOResponse);
					assertEquals(TEST_LIST_SIZE + 1, itemInventoryDTOResponse.getResultSetSize());
				});
	}

	@Test
	// @RepeatedTest(3)
	public void getAllItemInventoriesTest() {
		log.debug("\n\ngetAllItemInventoriesTest INT Test");

		webTestClient.get()
				.uri(ITEM_INVENTORIES_URL)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(ItemInventoryDTOResponse.class)
				.consumeWith(response -> {
					ItemInventoryDTOResponse itemInventoryDTOResponse = response.getResponseBody();

					assertNotNull(itemInventoryDTOResponse);
					assertEquals(TEST_LIST_SIZE, itemInventoryDTOResponse.getResultSetSize());
				})
				.value(SandboxUtils::prettyPrintObjectToJson);
	}

	@Test
	// @RepeatedTest(3)
	public void getItemInventoryByIdTest() {
		log.debug("\n\ngetItemInventoryByIdTest INT Test - id=" + TEST_ITEM_ID);

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
					assertEquals(TEST_ITEM_ID,
							itemInventoryDTOResponse.getItemInventoryDTOList().stream().findFirst().get().getId());

				})
				.value(SandboxUtils::prettyPrintObjectToJson);
	}

	@Test
	// @RepeatedTest(3)
	public void updateItemInventoryByIdTest() {
		log.debug("\n\nupdateItemInventoryByIdTest INT Test - id=" + TEST_ITEM_ID);

		ItemInventoryDTORequest updatedItemInventoryDTORequest = ItemInventoryTestUtil
				.buildMockItemInventoryDTORequest();
		updatedItemInventoryDTORequest.setId(TEST_ITEM_ID);
		updatedItemInventoryDTORequest.setName("coding is fun");

		webTestClient.put()
				.uri(ITEM_INVENTORIES_URL + "/{id}", TEST_ITEM_ID)
				.bodyValue(updatedItemInventoryDTORequest)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(ItemInventoryDTOResponse.class)
				.consumeWith(response -> {
					ItemInventoryDTOResponse itemInventoryDTOResponse = response.getResponseBody();

					assertNotNull(itemInventoryDTOResponse);
					assertEquals(1, itemInventoryDTOResponse.getResultSetSize());
					assertEquals(updatedItemInventoryDTORequest.getName(),
							itemInventoryDTOResponse.getItemInventoryDTOList().stream().findFirst().get().getName());
					assertEquals(updatedItemInventoryDTORequest.getPrice(),
							itemInventoryDTOResponse.getItemInventoryDTOList().stream().findFirst().get().getPrice());
					assertEquals(updatedItemInventoryDTORequest.getQuantity(),
							itemInventoryDTOResponse.getItemInventoryDTOList()
									.stream()
									.findFirst()
									.get()
									.getQuantity());
				})
				.value(SandboxUtils::prettyPrintObjectToJson);
	}

	@Test
	// @RepeatedTest(3)
	public void deleteItemInventoryByIdTest() {
		log.debug("\n\ndeleteItemInventoryByIdTest INT Test - id=" + TEST_ITEM_ID);

		webTestClient.delete()
				.uri(ITEM_INVENTORIES_URL + "/{id}", TEST_ITEM_ID)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(Void.class);

		webTestClient.get()
				.uri(ITEM_INVENTORIES_URL)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(ItemInventoryDTOResponse.class)
				.consumeWith(response -> {
					ItemInventoryDTOResponse itemInventoryDTOResponse = response.getResponseBody();

					assertNotNull(itemInventoryDTOResponse);
					assertEquals(TEST_LIST_SIZE - 1, itemInventoryDTOResponse.getResultSetSize());
				})
				.value(SandboxUtils::prettyPrintObjectToJson);
	}

	@Test
	// @RepeatedTest(3)
	public void deleteAllItemInventoriesTest() {
		log.debug("\n\ndeleteAllItemInventoriesTest INT Test");

		webTestClient.get()
				.uri(ITEM_INVENTORIES_URL)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(ItemInventoryDTOResponse.class)
				.consumeWith(response -> {
					ItemInventoryDTOResponse itemInventoryDTOResponse = response.getResponseBody();

					assertNotNull(itemInventoryDTOResponse);
					assertEquals(4, itemInventoryDTOResponse.getResultSetSize());
				});

		webTestClient.delete()
				.uri(ITEM_INVENTORIES_URL + "/delete")
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(Void.class);

		webTestClient.get()
				.uri(ITEM_INVENTORIES_URL)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(ItemInventoryDTOResponse.class)
				.consumeWith(response -> {
					ItemInventoryDTOResponse itemInventoryDTOResponse = response.getResponseBody();

					assertNotNull(itemInventoryDTOResponse);
					assertEquals(0, itemInventoryDTOResponse.getResultSetSize());
				})
				.value(SandboxUtils::prettyPrintObjectToJson);
	}
}
