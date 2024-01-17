package com.online.store.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.item.inventory.model.response.ItemInventoryDTOResponse;
import com.item.review.model.response.ItemReviewDTOResponse;

import reactor.core.publisher.Mono;

public interface OnlineStoreService {

	Mono<ServerResponse> getAllItemReviews();

	Mono<ServerResponse> getItemReviewById(String id);

	Mono<ServerResponse> getItemReviewsByItemInventoryId(String itemInventoryId);

	Mono<ServerResponse> getAllItemInventories();

	Mono<ServerResponse> getItemInventoryById(String id);

	Mono<ServerResponse> getItemDetailsByItemInventoryId(String itemInventoryId);

	Mono<ServerResponse> processItemReviewDTOResponseMono(Mono<ItemReviewDTOResponse> itemReviewDTOResponseMono,
			HttpStatus httpStatus);

	Mono<ServerResponse> processItemInventoryDTOResponseMono(
			Mono<ItemInventoryDTOResponse> itemInventoryDTOResponseMono, HttpStatus httpStatus);
}
