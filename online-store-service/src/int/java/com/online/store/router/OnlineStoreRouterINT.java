package com.online.store.router;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.item.inventory.model.response.ErrorDTOResponse;
import com.online.store.service.model.OnlineStoreDTOResponse;
import com.sandbox.util.SandboxUtils;

import lombok.extern.slf4j.Slf4j;

@ActiveProfiles("test")
@AutoConfigureWireMock(port = 1234)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "webClient.itemInventoriesUrl=http://localhost:1234/v1/item-inventories",
		"webClient.itemReviewsUrl=http://localhost:1234/v1/item-reviews" })
@Slf4j
public class OnlineStoreRouterINT {
	@Autowired
	private WebTestClient webTestClient;

	static final String ONLINE_STORE_URL = "/v1/online-store/item/{id}";

	static final String TEST_INVENTORY_ID = "123456";

	@Test
	public void getItemDetailsByItemInventoryId_Test() {
		log.debug("\n\ngetItemDetailsByItemInventoryId_Test INT Test");

		stubFor(get(urlEqualTo("/v1/item-inventories/" + TEST_INVENTORY_ID))
				.willReturn(aResponse().withHeader("Content-type", "application/json")
						.withBodyFile("GetItemInventoryByItemInventoryID.JSON")));

		stubFor(get(urlPathEqualTo("/v1/item-reviews"))
				.willReturn(aResponse().withHeader("Content-type", "application/json")
						.withBodyFile("GetItemReviewByItemInventoryID.json")));

		webTestClient.get()
				.uri(ONLINE_STORE_URL, TEST_INVENTORY_ID)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(OnlineStoreDTOResponse.class)
				.consumeWith(apiResponse -> {
					OnlineStoreDTOResponse onlineStoreDTOResponse = apiResponse.getResponseBody();

					assertNotNull(onlineStoreDTOResponse);
					assertEquals("aws cloud developerrrrrrr",
							onlineStoreDTOResponse.getItemInventoryDTOResponse()
									.getItemInventoryDTOList()
									.stream()
									.findFirst()
									.get()
									.getName());
					assertEquals("enterprise api developerrrr",
							onlineStoreDTOResponse.getItemReviewDTOResponse()
									.getItemReviewDTOList()
									.stream()
									.findFirst()
									.get()
									.getFeedback());

				})
				.value(SandboxUtils::prettyPrintObjectToJson);
	}

	@Test
	public void getItemDetailsByItemInventoryId_item_inventories400_Test() {
		log.debug("\n\ngetItemDetailsByItemInventoryId_item_inventories400_Test INT Test");

		stubFor(get(urlEqualTo("/v1/item-inventories/" + TEST_INVENTORY_ID))
				.willReturn(aResponse().withStatus(404).withBodyFile("MockExternalClientException.json")));

		webTestClient.get()
				.uri(ONLINE_STORE_URL, TEST_INVENTORY_ID)
				.exchange()
				.expectStatus()
				.is4xxClientError()
				.expectBody(ErrorDTOResponse.class)
				.consumeWith(apiResponse -> {
					ErrorDTOResponse errorDTOResponse = apiResponse.getResponseBody();

					assertNotNull(errorDTOResponse);
					assertEquals(404, errorDTOResponse.getErrorCode());
					assertEquals(
							"4xxClientError During Item Inventory Api Call. Item(s) not Found. statusCode=404 - itemInventoryId=123456",
							errorDTOResponse.getErrorMessage());
					assertEquals("NOT_FOUND", errorDTOResponse.getStatus());

				})
				.value(SandboxUtils::prettyPrintObjectToJson);
	}

	@Test
	public void getItemDetailsByItemInventoryId_item_reviews400_Test() {
		log.debug("\n\ngetItemDetailsByItemInventoryId_item_reviews400_Test INT Test");

		stubFor(get(urlEqualTo("/v1/item-inventories/" + TEST_INVENTORY_ID))
				.willReturn(aResponse().withHeader("Content-type", "application/json")
						.withBodyFile("GetItemInventoryByItemInventoryID.JSON")));

		stubFor(get(urlPathEqualTo("/v1/item-reviews")).willReturn(aResponse().withStatus(404)));

		webTestClient.get()
				.uri(ONLINE_STORE_URL, TEST_INVENTORY_ID)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(OnlineStoreDTOResponse.class)
				.consumeWith(apiResponse -> {
					OnlineStoreDTOResponse onlineStoreDTOResponse = apiResponse.getResponseBody();

					assertNotNull(onlineStoreDTOResponse);
					assertEquals("aws cloud developerrrrrrr",
							onlineStoreDTOResponse.getItemInventoryDTOResponse()
									.getItemInventoryDTOList()
									.stream()
									.findFirst()
									.get()
									.getName());
					assertNull(onlineStoreDTOResponse.getItemReviewDTOResponse());

				})
				.value(SandboxUtils::prettyPrintObjectToJson);
	}

	@Test
	public void getItemDetailsByItemInventoryId_item_inventories500_Test() {
		log.debug("\n\ngetItemDetailsByItemInventoryId_item_inventories500_Test INT Test");

		stubFor(get(urlEqualTo("/v1/item-inventories/" + TEST_INVENTORY_ID))
				.willReturn(aResponse().withStatus(500).withBodyFile("MockExternalClientException.json")));

		webTestClient.get()
				.uri(ONLINE_STORE_URL, TEST_INVENTORY_ID)
				.exchange()
				.expectStatus()
				.is5xxServerError()
				.expectBody(ErrorDTOResponse.class)
				.consumeWith(apiResponse -> {
					ErrorDTOResponse errorDTOResponse = apiResponse.getResponseBody();

					assertNotNull(errorDTOResponse);
					assertEquals(500, errorDTOResponse.getErrorCode());
					assertEquals("5xxServerError During Item Inventory Api Call.", errorDTOResponse.getErrorMessage());
					assertEquals("INTERNAL_SERVER_ERROR", errorDTOResponse.getStatus());

				})
				.value(SandboxUtils::prettyPrintObjectToJson);
	}

	@Test
	public void getItemDetailsByItemInventoryId_item_reviews500_Test() {
		log.debug("\n\ngetItemDetailsByItemInventoryId_item_reviews500_Test INT Test");

		stubFor(get(urlEqualTo("/v1/item-inventories/" + TEST_INVENTORY_ID))
				.willReturn(aResponse().withHeader("Content-type", "application/json")
						.withBodyFile("GetItemInventoryByItemInventoryID.JSON")));

		stubFor(get(urlPathEqualTo("/v1/item-reviews")).willReturn(aResponse().withStatus(500)));

		webTestClient.get()
				.uri(ONLINE_STORE_URL, TEST_INVENTORY_ID)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(OnlineStoreDTOResponse.class)
				.consumeWith(apiResponse -> {
					OnlineStoreDTOResponse onlineStoreDTOResponse = apiResponse.getResponseBody();

					assertNotNull(onlineStoreDTOResponse);
					assertEquals("aws cloud developerrrrrrr",
							onlineStoreDTOResponse.getItemInventoryDTOResponse()
									.getItemInventoryDTOList()
									.stream()
									.findFirst()
									.get()
									.getName());
					assertNull(onlineStoreDTOResponse.getItemReviewDTOResponse());

				})
				.value(SandboxUtils::prettyPrintObjectToJson);
	}
}
