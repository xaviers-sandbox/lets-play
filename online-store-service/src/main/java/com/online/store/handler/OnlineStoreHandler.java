package com.online.store.handler;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.online.store.service.OnlineStoreService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class OnlineStoreHandler {
	private OnlineStoreService onlineStoreService;

	public OnlineStoreHandler(OnlineStoreService onlineStoreService) {
		this.onlineStoreService = onlineStoreService;
	}

	public Mono<ServerResponse> handleGetItemDetailsByItemInventoryId(ServerRequest serverRequest) {
		String itemInventoryId = serverRequest.pathVariable("itemInventoryId");

		log.debug("handleGetItemDetailsByItemInventoryId itemInventoryId={}", itemInventoryId);

		return onlineStoreService.getItemDetailsByItemInventoryId(itemInventoryId);
	}
	
	public Mono<ServerResponse> handleGetAllItemReviews(ServerRequest serverRequest) {
		log.debug("handleGetAllItemReviews");

		Optional<String> itemInventoryIdOPT = serverRequest.queryParam("itemInventoryId");

		return itemInventoryIdOPT.isPresent()
				? onlineStoreService.getItemReviewsByItemInventoryId(itemInventoryIdOPT.get())
				: onlineStoreService.getAllItemReviews();
	}

	public Mono<ServerResponse> handleGetItemReviewById(ServerRequest serverRequest) {
		String id = serverRequest.pathVariable("id");

		log.debug("handleGetItemReviewById id={}", id);

		return onlineStoreService.getItemReviewById(id);
	}

	public Mono<ServerResponse> handleGetAllItemInventories(ServerRequest serverRequest) {
		log.debug("handleGetAllItemInventories");

		return onlineStoreService.getAllItemInventories();
	}

	public Mono<ServerResponse> handleGetItemInventoryById(ServerRequest serverRequest) {
		String id = serverRequest.pathVariable("id");

		log.debug("handleGetItemInventoryById id={}", id);

		return onlineStoreService.getItemInventoryById(id);
	}
}
