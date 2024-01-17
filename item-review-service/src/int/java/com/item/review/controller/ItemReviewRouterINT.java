package com.item.review.controller;

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

import com.item.review.entity.ItemReviewEntity;
import com.item.review.model.request.ItemReviewDTORequest;
import com.item.review.model.response.ItemReviewDTOResponse;
import com.item.review.repository.ItemReviewRepository;
import com.item.review.util.ItemReviewTestUtil;
import com.item.review.util.ItemReviewUtil;

import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@Slf4j
public class ItemReviewRouterINT {
	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private ItemReviewRepository ItemReviewRepo;

	static final String ITEM_REVIEWS_URL = "/v1/itemreviews";

	static final int TEST_LIST_SIZE = 4;

	static final String TEST_ITEM_ID = "123456";

	static String item_inventory_id;;

	@BeforeEach
	void initData() {
		log.debug("\n\nInitializing Test Data");
		List<ItemReviewEntity> itemReviewEntityList = ItemReviewTestUtil.generateItemReviewEntityList(TEST_LIST_SIZE);

		item_inventory_id = itemReviewEntityList.get(TEST_LIST_SIZE - 1).getItemInventoryId();
		itemReviewEntityList.get(0).setItemInventoryId(item_inventory_id);
		itemReviewEntityList.get(TEST_LIST_SIZE - 1).setId(TEST_ITEM_ID);

		log.debug("Initialize Test Data Complete size={}", TEST_LIST_SIZE);
		itemReviewEntityList.forEach(i -> log.debug(i.toString()));

		// block the thread to ensure this takes place before the test runs
		ItemReviewRepo.deleteAll().block();

		// block the thread to ensure this takes place before the test runs
		ItemReviewRepo.saveAll(itemReviewEntityList).blockLast();
	}

	@AfterEach
	void deleteData() {
		// block the thread to ensure this takes place before the test runs
		ItemReviewRepo.deleteAll().block();
	}

	@Test
	// @RepeatedTest(3)
	public void addNewItemReviewTest() {
		log.debug("\n\naddNewItemReviewTest INT Test");
		String testItemId = new Faker().phoneNumber().cellPhone();

		ItemReviewDTORequest itemReviewDTORequestMock = ItemReviewTestUtil.buildMockItemReviewDTORequest();
		itemReviewDTORequestMock.setId(testItemId);

		webTestClient.post()
				.uri(ITEM_REVIEWS_URL)
				.bodyValue(itemReviewDTORequestMock)
				.exchange()
				.expectStatus()
				.isCreated()
				.expectBody(ItemReviewDTOResponse.class)
				.consumeWith(response -> {
					ItemReviewDTOResponse itemReviewDTOResponse = response.getResponseBody();

					assertNotNull(itemReviewDTOResponse);
					assertEquals(1, itemReviewDTOResponse.getResultSetSize());
					assertEquals(testItemId,
							itemReviewDTOResponse.getItemReviewDTOList().stream().findFirst().get().getId());
					assertEquals(itemReviewDTORequestMock.getId(),
							itemReviewDTOResponse.getItemReviewDTOList().stream().findFirst().get().getId());

					assertEquals(itemReviewDTORequestMock.getItemInventoryId(),
							itemReviewDTOResponse.getItemReviewDTOList()
									.stream()
									.findFirst()
									.get()
									.getItemInventoryId());

					assertEquals(itemReviewDTORequestMock.getFeedback(),
							itemReviewDTOResponse.getItemReviewDTOList().stream().findFirst().get().getFeedback());

					assertEquals(itemReviewDTORequestMock.getRating(),
							itemReviewDTOResponse.getItemReviewDTOList().stream().findFirst().get().getRating());

				})
				.value(ItemReviewUtil::prettyPrintObjectToJson);

		webTestClient.get()
				.uri(ITEM_REVIEWS_URL)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(ItemReviewDTOResponse.class)
				.consumeWith(response -> {
					ItemReviewDTOResponse itemReviewDTOResponse = response.getResponseBody();

					assertNotNull(itemReviewDTOResponse);
					assertEquals(TEST_LIST_SIZE + 1, itemReviewDTOResponse.getResultSetSize());
				});
	}

	@Test
	// @RepeatedTest(3)
	public void getAllItemReviewsTest() {
		log.debug("\n\ngetAllItemReviewsTest INT Test");

		webTestClient.get()
				.uri(ITEM_REVIEWS_URL)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(ItemReviewDTOResponse.class)
				.consumeWith(response -> {
					ItemReviewDTOResponse itemReviewDTOResponse = response.getResponseBody();

					assertNotNull(itemReviewDTOResponse);
					assertEquals(TEST_LIST_SIZE, itemReviewDTOResponse.getResultSetSize());
				})
				.value(ItemReviewUtil::prettyPrintObjectToJson);
	}

	@Test
	// @RepeatedTest(3)
	public void getItemReviewByIdTest() {
		log.debug("\n\ngetItemReviewByIdTest INT Test - id=" + TEST_ITEM_ID);

		webTestClient.get()
				.uri(ITEM_REVIEWS_URL + "/{id}", TEST_ITEM_ID)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(ItemReviewDTOResponse.class)
				.consumeWith(response -> {
					ItemReviewDTOResponse itemReviewDTOResponse = response.getResponseBody();
					assertNotNull(itemReviewDTOResponse);
					assertEquals(1, itemReviewDTOResponse.getResultSetSize());
					assertEquals(TEST_ITEM_ID,
							itemReviewDTOResponse.getItemReviewDTOList().stream().findFirst().get().getId());

				})
				.value(ItemReviewUtil::prettyPrintObjectToJson);
	}

	@Test
	// @RepeatedTest(3)
	public void getItemReviewsByItemInventoryIdTest() {

		log.debug("\n\ngetItemReviewsByItemInventoryIdTest INT Test - itemInventoryId=" + item_inventory_id);

		webTestClient.get()
				.uri(uriBuilder -> uriBuilder.path(ITEM_REVIEWS_URL)
						.queryParam("itemInventoryId", item_inventory_id)
						.build())
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(ItemReviewDTOResponse.class)
				.consumeWith(response -> {
					ItemReviewDTOResponse itemReviewDTOResponse = response.getResponseBody();
					assertNotNull(itemReviewDTOResponse);
					assertEquals(2, itemReviewDTOResponse.getResultSetSize());

					itemReviewDTOResponse.getItemReviewDTOList().forEach(i -> {
						assertEquals(item_inventory_id, i.getItemInventoryId());
					});

				})
				.value(ItemReviewUtil::prettyPrintObjectToJson);
	}

	@Test
	// @RepeatedTest(3)
	public void updateItemReviewByIdTest() {
		log.debug("\n\nupdateItemReviewByIdTest INT Test - id=" + TEST_ITEM_ID);

		ItemReviewDTORequest updatedItemInventoryDTORequest = ItemReviewTestUtil.buildMockItemReviewDTORequest();
		updatedItemInventoryDTORequest.setId(TEST_ITEM_ID);
		updatedItemInventoryDTORequest.setFeedback("coding is the best!");

		webTestClient.put()
				.uri(ITEM_REVIEWS_URL + "/{id}", TEST_ITEM_ID)
				.bodyValue(updatedItemInventoryDTORequest)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(ItemReviewDTOResponse.class)
				.consumeWith(response -> {
					ItemReviewDTOResponse itemReviewDTOResponse = response.getResponseBody();

					assertNotNull(itemReviewDTOResponse);
					assertEquals(1, itemReviewDTOResponse.getResultSetSize());
					assertEquals(TEST_ITEM_ID,
							itemReviewDTOResponse.getItemReviewDTOList().stream().findFirst().get().getId());
					assertEquals(updatedItemInventoryDTORequest.getId(),
							itemReviewDTOResponse.getItemReviewDTOList().stream().findFirst().get().getId());

					assertEquals(updatedItemInventoryDTORequest.getItemInventoryId(),
							itemReviewDTOResponse.getItemReviewDTOList()
									.stream()
									.findFirst()
									.get()
									.getItemInventoryId());

					assertEquals(updatedItemInventoryDTORequest.getFeedback(),
							itemReviewDTOResponse.getItemReviewDTOList().stream().findFirst().get().getFeedback());

					assertEquals(updatedItemInventoryDTORequest.getRating(),
							itemReviewDTOResponse.getItemReviewDTOList().stream().findFirst().get().getRating());
				})
				.value(ItemReviewUtil::prettyPrintObjectToJson);
	}

	@Test
	// @RepeatedTest(3)
	public void deleteItemReviewByIdTest() {
		log.debug("\n\ndeleteItemReviewByIdTest INT Test - id=" + TEST_ITEM_ID);

		webTestClient.delete()
				.uri(ITEM_REVIEWS_URL + "/{id}", TEST_ITEM_ID)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(ItemReviewDTOResponse.class)
				.value(ItemReviewUtil::prettyPrintObjectToJson);

		webTestClient.get()
				.uri(ITEM_REVIEWS_URL)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(ItemReviewDTOResponse.class)
				.consumeWith(response -> {
					ItemReviewDTOResponse itemReviewDTOResponse = response.getResponseBody();

					assertNotNull(itemReviewDTOResponse);
					assertEquals(TEST_LIST_SIZE - 1, itemReviewDTOResponse.getResultSetSize());
				})
				.value(ItemReviewUtil::prettyPrintObjectToJson);
	}

	@Test
	// @RepeatedTest(3)
	public void deleteAllItemReviewsTest() {
		log.debug("\n\ndeleteAllItemReviewsTest INT Test");

		webTestClient.get()
				.uri(ITEM_REVIEWS_URL)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(ItemReviewDTOResponse.class)
				.consumeWith(response -> {
					ItemReviewDTOResponse itemReviewDTOResponse = response.getResponseBody();

					assertNotNull(itemReviewDTOResponse);
					assertEquals(TEST_LIST_SIZE, itemReviewDTOResponse.getResultSetSize());
				});

		webTestClient.delete()
				.uri(ITEM_REVIEWS_URL + "/delete")
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(Void.class);

		webTestClient.get()
				.uri(ITEM_REVIEWS_URL)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(ItemReviewDTOResponse.class)
				.consumeWith(response -> {
					ItemReviewDTOResponse itemReviewDTOResponse = response.getResponseBody();

					assertNotNull(itemReviewDTOResponse);
					assertEquals(0, itemReviewDTOResponse.getResultSetSize());
				})
				.value(ItemReviewUtil::prettyPrintObjectToJson);
	}
}
