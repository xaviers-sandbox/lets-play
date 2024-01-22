package com.online.store.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.item.inventory.model.response.ItemInventoryDTOResponse;
import com.online.store.client.error.handler.OnlineStoreClientErrorHandler;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ItemInventoyWebClient {
	private WebClient webClient;

	private OnlineStoreClientErrorHandler onlineStoreClientErrorHandler;

	@Value("${webClient.itemInventoriesUrl}")
	private String itemInventoriesUrl;

	public ItemInventoyWebClient(WebClient webClient, OnlineStoreClientErrorHandler onlineStoreClientErrorHandler) {
		this.webClient = webClient;
		this.onlineStoreClientErrorHandler = onlineStoreClientErrorHandler;
	}

	public Mono<ItemInventoryDTOResponse> getItemInventoryByItemInventoryId(String itemInventoryId) {
		log.debug("getItemInventoryByItemInventoryId itemInventoryId={}", itemInventoryId);

		String url = itemInventoriesUrl.concat("/{id}");

		return webClient.get()
				.uri(url, itemInventoryId)
				.retrieve()
				.onStatus(httpStatus -> httpStatus.is4xxClientError(), clientResponse -> {
					log.error(
							"getItemInventoryByItemInventoryId - Returned 4xxClientError - statusCode={} itemInventoryId={} ",
							clientResponse.statusCode(),
							itemInventoryId);

					return onlineStoreClientErrorHandler.processItemIventory4xxClientError(clientResponse,
							itemInventoryId);
				})
				.onStatus(httpStatus -> httpStatus.is5xxServerError(), clientResponse -> {
					log.error(
							"getItemInventoryByItemInventoryId - Returned 5xxServerError - statusCode={} itemInventoryId={} ",
							clientResponse.statusCode(),
							itemInventoryId);

					return onlineStoreClientErrorHandler.processItemInventory5xxServerError(clientResponse);
				})
				.bodyToMono(ItemInventoryDTOResponse.class)
				// .retryWhen(Retry.backoff(3, Duration.ofSeconds(3L)));
				.retry(3);
	}

	public Mono<ItemInventoryDTOResponse> getAllItemInventories() {
		log.debug("getAllItemInventories");

		return webClient.get()
				.uri(itemInventoriesUrl)
				.retrieve()
				.onStatus(httpStatus -> httpStatus.is4xxClientError(), clientResponse -> {
					log.error("getAllItemInventories - Returned 4xxClientError - statusCode={}",
							clientResponse.statusCode());

					return onlineStoreClientErrorHandler.processItemIventory4xxClientError(clientResponse);
				})
				.onStatus(httpStatus -> httpStatus.is5xxServerError(), clientResponse -> {
					log.error("getAllItemInventories - Returned 5xxServerError - statusCode={}",
							clientResponse.statusCode());

					return onlineStoreClientErrorHandler.processItemInventory5xxServerError(clientResponse);
				})
				.bodyToMono(ItemInventoryDTOResponse.class)
				// .retryWhen(Retry.backoff(3, Duration.ofSeconds(3L)));
				.retry(3);
	}
}
