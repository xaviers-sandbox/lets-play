package com.item.review.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.item.review.entity.ItemReviewEntity;
import com.item.review.model.request.ItemReviewDTORequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemReviewService {
	Mono<ServerResponse> addNewItemReview(ItemReviewDTORequest newItemReviewDTOrequest);

	Mono<ServerResponse> getAllItemReviews();

	Mono<ServerResponse> getItemReviewById(String id);

	Mono<ServerResponse> getItemReviewsByItemInventoryId(String itemInventoryId);

	Mono<ServerResponse> updateItemReviewById(String id, ItemReviewDTORequest updatedItemReviewDTOrequest);

	Mono<ServerResponse> deleteAllItemReviews();

	Mono<ServerResponse> deleteItemReviewById(String id);

	Mono<ServerResponse> processItemReviewEntityMono(Mono<ItemReviewEntity> itemReviewEntityMono,
			HttpStatus httpStatus);

	Mono<ServerResponse> processItemReviewEntityFlux(Flux<ItemReviewEntity> itemReviewEntityFlux,
			HttpStatus httpStatus);

	Mono<ServerResponse> buildBadRequestServerResponseMono(String errorMessages);

	Mono<ServerResponse> initTestDataBySize(int initDataSize);
}
