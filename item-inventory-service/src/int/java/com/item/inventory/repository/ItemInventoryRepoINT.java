package com.item.inventory.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import com.item.inventory.entity.ItemInventoryEntity;
import com.item.inventory.util.ItemInventoryTestUtil;
import com.sandbox.util.SandboxUtils;

import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
@ActiveProfiles("test")
@Slf4j
public class ItemInventoryRepoINT {

	@Autowired
	private ItemInventoryRepository itemInventoryRepo;

	static final int TEST_LIST_SIZE = 4;

	static final String TEST_ITEM_ID = "123aBc";

	static final Double TEST_PRICE = 65.0;

	static final int TEST_QUANTITY = 14;

	private String testItemName;

	Faker f;

	@BeforeEach
	void initData() {
		f = new Faker();

		log.debug("\n\nInitializing Test Data");

		List<ItemInventoryEntity> itemInventoryEntityList = ItemInventoryTestUtil
				.generateItemInventoryEntityList(TEST_LIST_SIZE);

		itemInventoryEntityList.get(TEST_LIST_SIZE - 1).setId(TEST_ITEM_ID);

		itemInventoryEntityList.get(TEST_LIST_SIZE - 1).setPrice(TEST_PRICE);

		itemInventoryEntityList.get(TEST_LIST_SIZE - 1).setQuantity(TEST_QUANTITY);

		testItemName = itemInventoryEntityList.get(TEST_LIST_SIZE - 1).getName();

		itemInventoryEntityList.get(TEST_LIST_SIZE - 1).setId(TEST_ITEM_ID);

		log.debug("Initialize Test Data Complete size={}", TEST_LIST_SIZE);
		itemInventoryEntityList.forEach(i -> log.debug(i.toString()));

		// block the thread to ensure this takes place before the test runs
		itemInventoryRepo.deleteAll().block();

		// block the thread to ensure this takes place before the test runs
		itemInventoryRepo.saveAll(itemInventoryEntityList).blockLast();
	}

	@AfterEach
	void deleteData() {
		// block the thread to ensure this takes place before the test runs
		itemInventoryRepo.deleteAll().block();
	}

	@Test
	// @RepeatedTest(3)
	void findAllTest() {
		log.debug("\n\nfindAllByOrderByNameAsc Test INT Test");
		Flux<ItemInventoryEntity> itemInventoryEntityFlux = itemInventoryRepo.findAllByOrderByNameAsc();

		itemInventoryEntityFlux.subscribe(SandboxUtils::prettyPrintObjectToJson);

		StepVerifier.create(itemInventoryEntityFlux).expectNextCount(TEST_LIST_SIZE).verifyComplete();
	}

	@Test
	// @RepeatedTest(3)
	void findByIdTest() {
		log.debug("\n\nfindByIdTest INT Test id=" + TEST_ITEM_ID);
		Mono<ItemInventoryEntity> itemInventoryEntityMono = itemInventoryRepo.findById(TEST_ITEM_ID);

		itemInventoryEntityMono.subscribe(SandboxUtils::prettyPrintObjectToJson);

		StepVerifier.create(itemInventoryEntityMono).assertNext(i -> {
			assertEquals(testItemName, i.getName());

		}).verifyComplete();
	}

	@Test
	// @RepeatedTest(3)
	void addNewItemInventoryTest() {
		log.debug("\n\naddNewItemInventoryTest INT Test");
		ItemInventoryEntity itemInventoryEntityMock = ItemInventoryTestUtil.buildMockItemInventoryEntity();

		String itemName = itemInventoryEntityMock.getName();
		String itemId = f.phoneNumber().cellPhone();

		itemInventoryEntityMock.setId(itemId);

		Mono<ItemInventoryEntity> savedItemInventoryMono = itemInventoryRepo.save(itemInventoryEntityMock);

		savedItemInventoryMono.subscribe(SandboxUtils::prettyPrintObjectToJson);

		StepVerifier.create(savedItemInventoryMono).assertNext(i -> { //
			assertEquals(itemName, i.getName());
			assertEquals(itemId, i.getId());
		}).verifyComplete();
	}

	@Test
	// @RepeatedTest(3)
	void updateItemInventoryByIdTest() {
		log.debug("\n\nupdateItemInventoryByIdTest INT Test id=" + TEST_ITEM_ID);
		ItemInventoryEntity retrievedItemInventoryEntity = itemInventoryRepo.findById(TEST_ITEM_ID).block();
		retrievedItemInventoryEntity.setPrice(100.00);

		Mono<ItemInventoryEntity> updatedItemInventoryMono = itemInventoryRepo.save(retrievedItemInventoryEntity);

		updatedItemInventoryMono.subscribe(SandboxUtils::prettyPrintObjectToJson);

		StepVerifier.create(updatedItemInventoryMono).assertNext(i -> { //
			assertEquals(100.00, i.getPrice());
		}).verifyComplete();
	}

	@Test
	// @RepeatedTest(3)
	void deleteItemInventoryByIdTest() {
		log.debug("\n\ndeleteItemInventoryByIdTest INT Test id=" + TEST_ITEM_ID);

		itemInventoryRepo.deleteById(TEST_ITEM_ID).block();

		Flux<ItemInventoryEntity> itemInventoryEntityFlux = itemInventoryRepo.findAllByOrderByNameAsc();

		StepVerifier.create(itemInventoryEntityFlux).expectNextCount(TEST_LIST_SIZE - 1).verifyComplete();
	}

	@Test
	// @RepeatedTest(3)
	void findByNameTest() {
		log.debug("\n\nfindByNameTest INT Test testItemName={}", testItemName.toLowerCase());

		Flux<ItemInventoryEntity> itemInventoryEntityFlux = itemInventoryRepo
				.findByNameIgnoreCase(testItemName.toLowerCase());

		itemInventoryEntityFlux.subscribe(SandboxUtils::prettyPrintObjectToJson);

		StepVerifier.create(itemInventoryEntityFlux).assertNext(i -> {
			assertEquals(testItemName, i.getName());
		}).verifyComplete();
	}

	@Test
	// @RepeatedTest(3)
	void findByIdIgnoreCaseAndNameIgnoreCase() {
		log.debug("\n\nfindByIdIgnoreCaseAndNameIgnoreCase INT Test testItemId={} testItemName={}",
				TEST_ITEM_ID.toUpperCase(),
				testItemName.toLowerCase());

		Mono<ItemInventoryEntity> itemInventoryEntityMono = itemInventoryRepo
				.findByIdIgnoreCaseAndNameIgnoreCase(TEST_ITEM_ID.toUpperCase(), testItemName.toLowerCase());

		itemInventoryEntityMono.subscribe(SandboxUtils::prettyPrintObjectToJson);

		StepVerifier.create(itemInventoryEntityMono).assertNext(i -> {
			assertEquals(testItemName, i.getName());
			assertEquals(TEST_ITEM_ID, i.getId());

		}).verifyComplete();
	}

	@Test
	// @RepeatedTest(3)
	void findByPriceBetween() {
		double maxPrice = 75;

		log.debug("\n\nfindByPriceBetween INT Test price1={} price2={}", maxPrice - 25, maxPrice);

		Flux<ItemInventoryEntity> itemInventoryEntityFlux = itemInventoryRepo.findByPriceBetween(maxPrice - 25,
				maxPrice);

		Long resultSetSize = itemInventoryEntityFlux.count().block();

		itemInventoryEntityFlux.subscribe(SandboxUtils::prettyPrintObjectToJson);

		assertTrue(1 <= resultSetSize);

		List<ItemInventoryEntity> itemInventoryEntityList = itemInventoryEntityFlux.collectList().block();

		itemInventoryEntityList.forEach(i -> {
			assertTrue(ItemInventoryTestUtil.isBetweenTwoNums(i.getPrice(), maxPrice - 25, maxPrice));
		});

//		StepVerifier.create(itemInventoryEntityFlux).assertNext(i -> {
//
//			boolean isBetween = ItemInventoryTestUtil.isBetweenTwoNums(i.getPrice(), maxPrice - 25, maxPrice);
//
//			try {
//				TimeUnit.SECONDS.sleep(3);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			assertTrue(isBetween);
//		}).verifyComplete();

	}

	@Test
	// @RepeatedTest(3)
	void findByQuantityBetween() {
		int maxQuantity = 25;

		log.debug("\n\nfindByQuantityBetween INT Test quantity1={} quantity2={}", maxQuantity - 15, maxQuantity);

		Flux<ItemInventoryEntity> itemInventoryEntityFlux = itemInventoryRepo.findByQuantityBetween(maxQuantity - 15,
				maxQuantity);

		Long resultSetSize = itemInventoryEntityFlux.count().block();

		itemInventoryEntityFlux.subscribe(SandboxUtils::prettyPrintObjectToJson);

		assertTrue(1 <= resultSetSize);

		List<ItemInventoryEntity> itemInventoryEntityList = itemInventoryEntityFlux.collectList().block();

		itemInventoryEntityList.forEach(i -> {
			assertTrue(ItemInventoryTestUtil.isBetweenTwoNums(i.getQuantity(), maxQuantity - 15, maxQuantity));
		});

//		StepVerifier.create(itemInventoryEntityFlux).assertNext(i -> {
//			boolean isBetween = ItemInventoryTestUtil.isBetweenTwoNums(i.getQuantity(), maxQuantity - 15, maxQuantity);
//
//			try {
//				TimeUnit.SECONDS.sleep(3);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//
//			assertTrue(isBetween);
//		}).verifyComplete();
	}
}
