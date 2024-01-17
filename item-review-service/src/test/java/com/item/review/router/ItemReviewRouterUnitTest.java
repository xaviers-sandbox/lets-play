package com.item.review.router;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.item.review.handler.ItemReviewHandler;
import com.item.review.mapper.ItemReviewMapper;
import com.item.review.model.ItemReviewDTO;
import com.item.review.model.request.ItemReviewDTORequest;
import com.item.review.model.response.ItemReviewDTOResponse;
import com.item.review.service.ItemReviewService;
import com.item.review.util.ItemReviewTestUtil;
import com.item.review.util.ItemReviewUtil;
import com.item.review.validation.ItemReviewValidator;

import lombok.extern.slf4j.Slf4j;

@AutoConfigureWebTestClient
@WebFluxTest
@Slf4j
@ActiveProfiles("test")
@ContextConfiguration(classes = { ItemReviewRouter.class, ItemReviewHandler.class, ItemReviewValidator.class })
public class ItemReviewRouterUnitTest {
	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private ItemReviewService itemReviewService;

	static final String ITEM_REVIEWS_URL = "/v1/itemreviews";

	private List<ItemReviewDTO> ItemReviewDTOListMock;

	static final int RANGE_MAX = 2;

	static final String TEST_ITEM_ID = "123456";

	@BeforeEach
	void initData() {
//		log.debug("\n\nInitializing Unit Test Data");
//		ItemReviewDTOListMock = ItemReviewTestUtil.generateItemReviewDTOList(RANGE_MAX);
//
//		ItemReviewDTOListMock.get(RANGE_MAX - 1).setId(TEST_ITEM_ID);
//
//		log.debug("Initialize Test Data Complete size={}", RANGE_MAX);
//		ItemReviewDTOListMock.forEach(i -> log.debug(i.toString()));
	}

	// @Test(expected = Test.None.class)
	// @RepeatedTest(3)
	@Test
	public void addNewItemInventoryTest() {
		log.debug("\n\naddNewItemInventoryTest Unit Test");

		ItemReviewDTORequest itemReviewDTORequestMock = ItemReviewTestUtil.buildMockItemReviewDTORequest();
		itemReviewDTORequestMock.setId(TEST_ITEM_ID);

		ItemReviewDTOResponse itemReviewDTOResponseMock = ItemReviewTestUtil.buildMockItemReviewDTOResponse(1);
		itemReviewDTOResponseMock.getItemReviewDTOList().stream().findFirst().get().setId(TEST_ITEM_ID);

		when(itemReviewService.addNewItemReview(any())).thenReturn(
				ItemReviewMapper.buildServerResponseMonoWithDTOResponse(itemReviewDTOResponseMock, HttpStatus.CREATED));

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
					assertEquals(itemReviewDTOResponseMock.getResultSetSize(),
							itemReviewDTOResponse.getResultSetSize());
					assertEquals(itemReviewDTOResponseMock.getItemReviewDTOList().size(),
							itemReviewDTOResponse.getItemReviewDTOList().size());
					assertEquals(itemReviewDTOResponseMock.getItemReviewDTOList().stream().findFirst().get().getId(),
							itemReviewDTOResponse.getItemReviewDTOList().stream().findFirst().get().getId());
				})
				.value(ItemReviewUtil::prettyPrintObjectToJson);
	}

	@Test
	// @RepeatedTest(3)
	public void getAllItemReviewsTest() {
		log.debug("\n\ngetAllItemReviewsTest Unit Test");

		ItemReviewDTOResponse itemReviewDTOResponseMock = ItemReviewTestUtil.buildMockItemReviewDTOResponse(5);

		when(itemReviewService.getAllItemReviews()).thenReturn(
				ItemReviewMapper.buildServerResponseMonoWithDTOResponse(itemReviewDTOResponseMock, HttpStatus.OK));

		webTestClient.get()
				.uri(ITEM_REVIEWS_URL)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(ItemReviewDTOResponse.class)
				.consumeWith(response -> {
					ItemReviewDTOResponse itemReviewDTOResponse = response.getResponseBody();

					assertNotNull(itemReviewDTOResponse);
					assertEquals(5, itemReviewDTOResponse.getResultSetSize());
					assertEquals(itemReviewDTOResponseMock.getResultSetSize(),
							itemReviewDTOResponse.getResultSetSize());
					assertEquals(itemReviewDTOResponseMock.getItemReviewDTOList().size(),
							itemReviewDTOResponse.getItemReviewDTOList().size());
					assertEquals(itemReviewDTOResponseMock.getItemReviewDTOList().get(4).getId(),
							itemReviewDTOResponse.getItemReviewDTOList().get(4).getId());
				})
				.value(ItemReviewUtil::prettyPrintObjectToJson);
	}

	@Test
	// @RepeatedTest(3)
	public void getItemReviewByIdTest() {
		log.debug("\n\ngetItemReviewByIdTest Unit Test- id=" + TEST_ITEM_ID);

		ItemReviewDTOResponse itemReviewDTOResponseMock = ItemReviewTestUtil.buildMockItemReviewDTOResponse(1);
		itemReviewDTOResponseMock.getItemReviewDTOList().stream().findFirst().get().setId(TEST_ITEM_ID);

		when(itemReviewService.getItemReviewById(any())).thenReturn(
				ItemReviewMapper.buildServerResponseMonoWithDTOResponse(itemReviewDTOResponseMock, HttpStatus.OK));

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
					assertEquals(itemReviewDTOResponseMock.getResultSetSize(),
							itemReviewDTOResponse.getResultSetSize());
					assertEquals(itemReviewDTOResponseMock.getItemReviewDTOList().size(),
							itemReviewDTOResponse.getItemReviewDTOList().size());
					assertEquals(TEST_ITEM_ID,
							itemReviewDTOResponse.getItemReviewDTOList().stream().findFirst().get().getId());
					assertEquals(itemReviewDTOResponseMock.getItemReviewDTOList().stream().findFirst().get().getId(),
							itemReviewDTOResponse.getItemReviewDTOList().stream().findFirst().get().getId());
				})
				.value(ItemReviewUtil::prettyPrintObjectToJson);
	}

	@Test
	// @RepeatedTest(3)
	public void updateItemReviewByIdTest() {
		log.debug("\n\nupdateItemReviewByIdTest Unit Test - id=" + TEST_ITEM_ID);

		ItemReviewDTORequest updatedItemInventoryDTORequestMock = ItemReviewTestUtil.buildMockItemReviewDTORequest();
		updatedItemInventoryDTORequestMock.setId(TEST_ITEM_ID);

		ItemReviewDTOResponse itemReviewDTOResponseMock = ItemReviewTestUtil.buildMockItemReviewDTOResponse(1);
		itemReviewDTOResponseMock.getItemReviewDTOList().stream().findFirst().get().setId(TEST_ITEM_ID);

		when(itemReviewService.updateItemReviewById(any(), any())).thenReturn(
				ItemReviewMapper.buildServerResponseMonoWithDTOResponse(itemReviewDTOResponseMock, HttpStatus.OK));

		webTestClient.put()
				.uri(ITEM_REVIEWS_URL + "/{id}", TEST_ITEM_ID)
				.bodyValue(updatedItemInventoryDTORequestMock)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(ItemReviewDTOResponse.class)
				.consumeWith(response -> {
					ItemReviewDTOResponse itemReviewDTOResponse = response.getResponseBody();

					assertNotNull(itemReviewDTOResponse);
					assertEquals(1, itemReviewDTOResponse.getResultSetSize());
					assertEquals(itemReviewDTOResponseMock.getResultSetSize(),
							itemReviewDTOResponse.getResultSetSize());
					assertEquals(itemReviewDTOResponseMock.getItemReviewDTOList().size(),
							itemReviewDTOResponse.getItemReviewDTOList().size());
					assertEquals(TEST_ITEM_ID,
							itemReviewDTOResponse.getItemReviewDTOList().stream().findFirst().get().getId());
					assertEquals(itemReviewDTOResponseMock.getItemReviewDTOList().stream().findFirst().get().getId(),
							itemReviewDTOResponse.getItemReviewDTOList().stream().findFirst().get().getId());
				})
				.value(ItemReviewUtil::prettyPrintObjectToJson);
	}

	@Test
	// @RepeatedTest(3)
	public void deleteItemInventoryByIdTest() {
		log.debug("\n\ndeleteItemInventoryByIdTest - id=" + TEST_ITEM_ID);

		ItemReviewDTOResponse itemReviewDTOResponseMock = ItemReviewTestUtil.buildMockItemReviewDTOResponse(1);

		when(itemReviewService.deleteItemReviewById(any())).thenReturn(ItemReviewMapper
				.buildServerResponseMonoWithDTOResponse(itemReviewDTOResponseMock, HttpStatus.NO_CONTENT));

		webTestClient.delete()
				.uri(ITEM_REVIEWS_URL + "/{id}", TEST_ITEM_ID)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(Void.class);
	}

	@Test
	// @RepeatedTest(3)
	public void addNewItemReviewTest_empty_BAD_REQUESTS() {
		log.debug("\n\naddNewItemReviewTest_empty_BAD_REQUESTS - ");

		ItemReviewDTORequest itemReviewDTORequestMock = ItemReviewTestUtil.buildMockItemReviewDTORequest();
		itemReviewDTORequestMock.setId(TEST_ITEM_ID);

		ItemReviewDTOResponse itemReviewDTOResponseMock = ItemReviewTestUtil.buildMockItemReviewDTOResponse(1);
		itemReviewDTOResponseMock.getItemReviewDTOList().stream().findFirst().get().setId(TEST_ITEM_ID);

		when(itemReviewService.addNewItemReview(any())).thenReturn(
				ItemReviewMapper.buildServerResponseMonoWithDTOResponse(itemReviewDTOResponseMock, HttpStatus.CREATED));

		// empty itemReviewId test
		// itemReviewDTORequestMock.setItemReviewId("oiasfjaoifj");

		webTestClient.post()
				.uri(ITEM_REVIEWS_URL)
				.bodyValue(itemReviewDTORequestMock)
				.exchange()
				.expectStatus()

				.is5xxServerError();

		// .isBadRequest();
//				.expectBody(String.class);
//				.consumeWith(response -> {
//
//					assertTrue(StringUtils.isNotEmpty(response.getResponseBody()));
//					assertTrue(response.getResponseBody().contains("ItemReviewId Cannot Be Empty or Null"));
//				}).value(ItemReviewUtil::prettyPrintObjectToJson);
	}

//	@Test
//	// @RepeatedTest(3)
//	public void addNewItemInventoryTest_null_BAD_REQUESTS() {
//		log.debug("\n\naddNewItemInventoryTest_null_BAD_REQUESTS - ");
//
//		ItemReviewDTORequest itemInventoryDTORequestMock = ItemReviewTestUtil.buildMockItemInventoryDTORequest();
//
//		// null name test
//		itemInventoryDTORequestMock.setName(null);
//
//		webTestClient.post().uri(ITEM_INVENTORIES_URL).bodyValue(itemInventoryDTORequestMock).exchange().expectStatus()
//				.isBadRequest().expectBody(ErrorDTOResponse.class).consumeWith(response -> {
//					ErrorDTOResponse errorDTOResponse = response.getResponseBody();
//
//					assertNotNull(errorDTOResponse);
//					assertEquals(400, errorDTOResponse.getErrorCode());
//					assertEquals("Name Cannot Be Empty or Null", errorDTOResponse.getErrorMessage());
//					assertEquals("BAD_REQUEST", errorDTOResponse.getStatus());
//				}).value(ItemReviewUtil::prettyPrintObjectToJson);
//
//		itemInventoryDTORequestMock = ItemReviewTestUtil.buildMockItemInventoryDTORequest();
//
//		// null quantity test
//		itemInventoryDTORequestMock.setQuantity(null);
//
//		webTestClient.post().uri(ITEM_INVENTORIES_URL).bodyValue(itemInventoryDTORequestMock).exchange().expectStatus()
//				.isBadRequest().expectBody(ErrorDTOResponse.class).consumeWith(response -> {
//					ErrorDTOResponse errorDTOResponse = response.getResponseBody();
//
//					assertNotNull(errorDTOResponse);
//					assertEquals(400, errorDTOResponse.getErrorCode());
//					assertEquals("Quantity Cannot Be Empty or Null", errorDTOResponse.getErrorMessage());
//					assertEquals("BAD_REQUEST", errorDTOResponse.getStatus());
//				}).value(ItemReviewUtil::prettyPrintObjectToJson);
//
//		itemInventoryDTORequestMock = ItemReviewTestUtil.buildMockItemInventoryDTORequest();
//
//		// null price test
//		itemInventoryDTORequestMock.setPrice(null);
//
//		webTestClient.post().uri(ITEM_INVENTORIES_URL).bodyValue(itemInventoryDTORequestMock).exchange().expectStatus()
//				.isBadRequest().expectBody(ErrorDTOResponse.class).consumeWith(response -> {
//
//					ErrorDTOResponse errorDTOResponse = response.getResponseBody();
//
//					assertNotNull(errorDTOResponse);
//					assertEquals(400, errorDTOResponse.getErrorCode());
//					assertEquals("Price Cannot Be Empty or Null", errorDTOResponse.getErrorMessage());
//					assertEquals("BAD_REQUEST", errorDTOResponse.getStatus());
//				}).value(ItemReviewUtil::prettyPrintObjectToJson);
//	}
//
//	@Test
//	// @RepeatedTest(3)
//	public void addNewItemInventoryTest_negative_BAD_REQUESTS() {
//		log.debug("\n\naddNewItemInventoryTest_negative_BAD_REQUESTS - ");
//
//		ItemReviewDTORequest itemReviewDTORequest = ItemReviewTestUtil.buildMockItemInventoryDTORequest();
//
//		// negative price test
//		itemReviewDTORequest.setPrice(-10.0);
//
//		webTestClient.post().uri(ITEM_INVENTORIES_URL).bodyValue(itemReviewDTORequest).exchange().expectStatus()
//				.isBadRequest().expectBody(ErrorDTOResponse.class)
//
//				.consumeWith(response -> {
//					ErrorDTOResponse errorResponseDTO = response.getResponseBody();
//
//					assertNotNull(errorResponseDTO);
//					assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponseDTO.getErrorCode());
//					assertEquals("Price Cannot Be Negative", errorResponseDTO.getErrorMessage());
//					assertEquals(HttpStatus.BAD_REQUEST.name(), errorResponseDTO.getStatus());
//				}).value(ItemReviewUtil::prettyPrintObjectToJson);
//
//		itemReviewDTORequest = ItemReviewTestUtil.buildMockItemInventoryDTORequest();
//
//		// negative quantity test
//		itemReviewDTORequest.setQuantity(-5);
//
//		webTestClient.post().uri(ITEM_INVENTORIES_URL).bodyValue(itemReviewDTORequest).exchange().expectStatus()
//				.isBadRequest().expectBody(ErrorDTOResponse.class)
//
//				.consumeWith(response -> {
//					ErrorDTOResponse errorResponseDTO = response.getResponseBody();
//
//					assertNotNull(errorResponseDTO);
//					assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponseDTO.getErrorCode());
//					assertEquals("Quantity Cannot Be Negative", errorResponseDTO.getErrorMessage());
//					assertEquals(HttpStatus.BAD_REQUEST.name(), errorResponseDTO.getStatus());
//				}).value(ItemReviewUtil::prettyPrintObjectToJson);
//	}
//
//	@Test
//	// @RepeatedTest(3)
//	public void isBetweenTester() {
//		assertFalse(ItemReviewTestUtil.isBetweenTwoNums(9, 10, 15));
//		assertFalse(ItemReviewTestUtil.isBetweenTwoNums(16, 10, 15));
//		assertTrue(ItemReviewTestUtil.isBetweenTwoNums(10, 10, 15));
//		assertTrue(ItemReviewTestUtil.isBetweenTwoNums(15, 10, 15));
//	}
}
