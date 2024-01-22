package com.online.store.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.item.review.model.response.ItemReviewDTOResponse;
import com.online.store.client.error.handler.OnlineStoreClientErrorHandler;
import com.online.store.mapper.OnlineStoreMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ItemReviewWebClient {
	private WebClient webClient;

	private OnlineStoreClientErrorHandler onlineStoreClientErrorHandler;

	@Value("${webClient.itemReviewsUrl}")
	private String itemReviewsUrl;

	public ItemReviewWebClient(WebClient webClient, OnlineStoreClientErrorHandler onlineStoreClientErrorHandler) {
		this.webClient = webClient;
		this.onlineStoreClientErrorHandler = onlineStoreClientErrorHandler;
	}

	public Mono<ItemReviewDTOResponse> getItemReviewByItemInventoryId(String itemInventoryId) {
		log.debug("getItemReviewByItemInventoryId itemInventoryId={}", itemInventoryId);

		String uri = UriComponentsBuilder.fromHttpUrl(itemReviewsUrl)
				.queryParam("itemInventoryId", itemInventoryId)
				.buildAndExpand()
				.toUriString();

		return webClient.get()
				.uri(uri)
				.retrieve()
				.onStatus(httpStatus -> httpStatus.is4xxClientError(), clientResponse -> {

					return onlineStoreClientErrorHandler.processItemReview4xxClientError(clientResponse);
				})
				.onStatus(httpStatus -> httpStatus.is5xxServerError(), clientResponse -> {

					return onlineStoreClientErrorHandler.processItemReview5xxServerError(clientResponse);
				})
				.bodyToMono(ItemReviewDTOResponse.class)
				.retryWhen(OnlineStoreMapper.buildRetryBackoffSpec());
		// .retry(3);
		// .retryWhen(Retry.backoff(3, Duration.ofSeconds(3L)));
	}

	public Mono<ItemReviewDTOResponse> getAllItemReviews() {

		return webClient.get()
				.uri(itemReviewsUrl)
				.retrieve()
				.onStatus(httpStatus -> httpStatus.is4xxClientError(), clientResponse -> {

					return onlineStoreClientErrorHandler.processItemReview4xxClientError(clientResponse);
				})
				.onStatus(httpStatus -> httpStatus.is5xxServerError(), clientResponse -> {

					return onlineStoreClientErrorHandler.processItemReview5xxServerError(clientResponse);
				})
				.bodyToMono(ItemReviewDTOResponse.class)
				.retryWhen(OnlineStoreMapper.buildRetryBackoffSpec());
		// .retryWhen(Retry.backoff(3, Duration.ofSeconds(3L)));
		// .retry(3);
	}

	public Mono<ItemReviewDTOResponse> getItemReviewById(String id) {

		String uri = itemReviewsUrl.concat("/{id}");

		return webClient.get()
				.uri(uri, id)
				.retrieve()
				.onStatus(httpStatus -> httpStatus.is4xxClientError(), clientResponse -> {

					return onlineStoreClientErrorHandler.processItemReview4xxClientError(clientResponse);
				})
				.onStatus(httpStatus -> httpStatus.is5xxServerError(), clientResponse -> {

					return onlineStoreClientErrorHandler.processItemReview5xxServerError(clientResponse);
				})
				.bodyToMono(ItemReviewDTOResponse.class)
				.retryWhen(OnlineStoreMapper.buildRetryBackoffSpec());
		// .retryWhen(Retry.backoff(3, Duration.ofSeconds(3L)));
		// .retry(3);
	}
}
