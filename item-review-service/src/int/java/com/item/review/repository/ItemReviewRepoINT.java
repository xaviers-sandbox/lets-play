package com.item.review.repository;

import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@DataMongoTest
@ActiveProfiles("test")
@Slf4j
public class ItemReviewRepoINT {
//
//	@Autowired
//	private ItemReviewRepository itemInventoryRepo;
//
//	static final int TEST_LIST_SIZE = 4;
//
//	static final String TEST_ITEM_ID = "123aBc";
//
//	static final Double TEST_PRICE = 65.0;
//
//	static final int TEST_QUANTITY = 14;
//
//	private String testItemName;
//
//	Faker f;
//
//	@BeforeEach
//	void initData() {
//		f = new Faker();
//
//		log.debug("\n\nInitializing Test Data");
//
//		List<ItemReviewEntity> itemInventoryEntityList = ItemReviewTestUtil
//				.generateItemInventoryEntityList(TEST_LIST_SIZE);
//
//		itemInventoryEntityList.get(TEST_LIST_SIZE - 1).setId(TEST_ITEM_ID);
//
//		itemInventoryEntityList.get(TEST_LIST_SIZE - 1).setPrice(TEST_PRICE);
//
//		itemInventoryEntityList.get(TEST_LIST_SIZE - 1).setQuantity(TEST_QUANTITY);
//
//		testItemName = itemInventoryEntityList.get(TEST_LIST_SIZE - 1).getName();
//
//		itemInventoryEntityList.get(TEST_LIST_SIZE - 1).setId(TEST_ITEM_ID);
//
//		log.debug("Initialize Test Data Complete size={}", TEST_LIST_SIZE);
//		itemInventoryEntityList.forEach(i -> log.debug(i.toString()));
//
//		// block the thread to ensure this takes place before the test runs
//		itemInventoryRepo.deleteAll().block();
//
//		// block the thread to ensure this takes place before the test runs
//		itemInventoryRepo.saveAll(itemInventoryEntityList).blockLast();
//	}
//
//	@AfterEach
//	void deleteData() {
//		// block the thread to ensure this takes place before the test runs
//		itemInventoryRepo.deleteAll().block();
//	}
//
//	@Test
//	// @RepeatedTest(3)
//	void findAllTest() {
//		log.debug("\n\nfindAllByOrderByNameAsc Test INT Test");
//		Flux<ItemReviewEntity> itemInventoryEntityFlux = itemInventoryRepo.findAllByOrderByNameAsc();
//
//		itemInventoryEntityFlux.subscribe(ItemReviewUtil::prettyPrintObjectToJson);
//
//		StepVerifier.create(itemInventoryEntityFlux).expectNextCount(TEST_LIST_SIZE).verifyComplete();
//	}
//
//	@Test
//	// @RepeatedTest(3)
//	void findByIdTest() {
//		log.debug("\n\nfindByIdTest INT Test id=" + TEST_ITEM_ID);
//		Mono<ItemReviewEntity> itemInventoryEntityMono = itemInventoryRepo.findById(TEST_ITEM_ID);
//
//		itemInventoryEntityMono.subscribe(ItemReviewUtil::prettyPrintObjectToJson);
//
//		StepVerifier.create(itemInventoryEntityMono).assertNext(i -> {
//			assertEquals(testItemName, i.getName());
//
//		}).verifyComplete();
//	}
//
//	@Test
//	// @RepeatedTest(3)
//	void addNewItemInventoryTest() {
//		log.debug("\n\naddNewItemInventoryTest INT Test");
//		ItemReviewEntity itemInventoryEntityMock = ItemReviewTestUtil.buildMockItemInventoryEntity();
//
//		String itemName = itemInventoryEntityMock.getName();
//		String itemId = f.phoneNumber().cellPhone();
//
//		itemInventoryEntityMock.setId(itemId);
//
//		Mono<ItemReviewEntity> savedItemInventoryMono = itemInventoryRepo.save(itemInventoryEntityMock);
//
//		savedItemInventoryMono.subscribe(ItemReviewUtil::prettyPrintObjectToJson);
//
//		StepVerifier.create(savedItemInventoryMono).assertNext(i -> { //
//			assertEquals(itemName, i.getName());
//			assertEquals(itemId, i.getId());
//		}).verifyComplete();
//	}
//
//	@Test
//	// @RepeatedTest(3)
//	void updateItemInventoryByIdTest() {
//		log.debug("\n\nupdateItemInventoryByIdTest INT Test id=" + TEST_ITEM_ID);
//		ItemReviewEntity retrievedItemInventoryEntity = itemInventoryRepo.findById(TEST_ITEM_ID).block();
//		retrievedItemInventoryEntity.setPrice(100.00);
//
//		Mono<ItemReviewEntity> updatedItemInventoryMono = itemInventoryRepo.save(retrievedItemInventoryEntity);
//
//		updatedItemInventoryMono.subscribe(ItemReviewUtil::prettyPrintObjectToJson);
//
//		StepVerifier.create(updatedItemInventoryMono).assertNext(i -> { //
//			assertEquals(100.00, i.getPrice());
//		}).verifyComplete();
//	}
//
//	@Test
//	// @RepeatedTest(3)
//	void deleteItemInventoryByIdTest() {
//		log.debug("\n\ndeleteItemInventoryByIdTest INT Test id=" + TEST_ITEM_ID);
//
//		itemInventoryRepo.deleteById(TEST_ITEM_ID).block();
//
//		Flux<ItemReviewEntity> itemInventoryEntityFlux = itemInventoryRepo.findAllByOrderByNameAsc();
//
//		StepVerifier.create(itemInventoryEntityFlux).expectNextCount(TEST_LIST_SIZE - 1).verifyComplete();
//	}
//
//	@Test
//	// @RepeatedTest(3)
//	void findByNameTest() {
//		log.debug("\n\nfindByNameTest INT Test testItemName={}", testItemName.toLowerCase());
//
//		Flux<ItemReviewEntity> itemInventoryEntityFlux = itemInventoryRepo
//				.findByNameIgnoreCase(testItemName.toLowerCase());
//
//		itemInventoryEntityFlux.subscribe(ItemReviewUtil::prettyPrintObjectToJson);
//
//		StepVerifier.create(itemInventoryEntityFlux).assertNext(i -> {
//			assertEquals(testItemName, i.getName());
//		}).verifyComplete();
//	}
//
//	@Test
//	// @RepeatedTest(3)
//	void findByIdIgnoreCaseAndNameIgnoreCase() {
//		log.debug("\n\nfindByIdIgnoreCaseAndNameIgnoreCase INT Test testItemId={} testItemName={}",
//				TEST_ITEM_ID.toUpperCase(), testItemName.toLowerCase());
//
//		Mono<ItemReviewEntity> itemInventoryEntityMono = itemInventoryRepo
//				.findByIdIgnoreCaseAndNameIgnoreCase(TEST_ITEM_ID.toUpperCase(), testItemName.toLowerCase());
//
//		itemInventoryEntityMono.subscribe(ItemReviewUtil::prettyPrintObjectToJson);
//
//		StepVerifier.create(itemInventoryEntityMono).assertNext(i -> {
//			assertEquals(testItemName, i.getName());
//			assertEquals(TEST_ITEM_ID, i.getId());
//
//		}).verifyComplete();
//	}
//
//	@Test
//	// @RepeatedTest(3)
//	void findByPriceBetween() {
//		double maxPrice = 75;
//
//		log.debug("\n\nfindByPriceBetween INT Test price1={} price2={}", maxPrice - 25, maxPrice);
//
//		Flux<ItemReviewEntity> itemInventoryEntityFlux = itemInventoryRepo.findByPriceBetween(maxPrice - 25,
//				maxPrice);
//
//		Long resultSetSize = itemInventoryEntityFlux.count().block();
//
//		itemInventoryEntityFlux.subscribe(ItemReviewUtil::prettyPrintObjectToJson);
//
//		assertTrue(1 <= resultSetSize);
//
//		List<ItemReviewEntity> itemInventoryEntityList = itemInventoryEntityFlux.collectList().block();
//
//		itemInventoryEntityList.forEach(i -> {
//			assertTrue(ItemReviewTestUtil.isBetweenTwoNums(i.getPrice(), maxPrice - 25, maxPrice));
//		});
//
////		StepVerifier.create(itemInventoryEntityFlux).assertNext(i -> {
////
////			boolean isBetween = ItemInventoryTestUtil.isBetweenTwoNums(i.getPrice(), maxPrice - 25, maxPrice);
////
////			try {
////				TimeUnit.SECONDS.sleep(3);
////			} catch (InterruptedException e) {
////				e.printStackTrace();
////			}
////			assertTrue(isBetween);
////		}).verifyComplete();
//
//	}
//
//	@Test
//	// @RepeatedTest(3)
//	void findByQuantityBetween() {
//		int maxQuantity = 25;
//
//		log.debug("\n\nfindByQuantityBetween INT Test quantity1={} quantity2={}", maxQuantity - 15, maxQuantity);
//
//		Flux<ItemReviewEntity> itemInventoryEntityFlux = itemInventoryRepo.findByQuantityBetween(maxQuantity - 15,
//				maxQuantity);
//
//		Long resultSetSize = itemInventoryEntityFlux.count().block();
//
//		itemInventoryEntityFlux.subscribe(ItemReviewUtil::prettyPrintObjectToJson);
//
//		assertTrue(1 <= resultSetSize);
//
//		List<ItemReviewEntity> itemInventoryEntityList = itemInventoryEntityFlux.collectList().block();
//
//		itemInventoryEntityList.forEach(i -> {
//			assertTrue(ItemReviewTestUtil.isBetweenTwoNums(i.getQuantity(), maxQuantity - 15, maxQuantity));
//		});
//
////		StepVerifier.create(itemInventoryEntityFlux).assertNext(i -> {
////			boolean isBetween = ItemInventoryTestUtil.isBetweenTwoNums(i.getQuantity(), maxQuantity - 15, maxQuantity);
////
////			try {
////				TimeUnit.SECONDS.sleep(3);
////			} catch (InterruptedException e) {
////				e.printStackTrace();
////			}
////
////			assertTrue(isBetween);
////		}).verifyComplete();
//	}
}
