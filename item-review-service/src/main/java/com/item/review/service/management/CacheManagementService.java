package com.item.review.service.management;

import org.springframework.web.reactive.function.server.ServerResponse;

import com.item.review.model.request.ItemReviewDTORequest;

import reactor.core.publisher.Mono;

public interface CacheManagementService {
	Mono<ServerResponse> addNewItemReviewToCacheManagement(ItemReviewDTORequest newItemReviewDTOrequest);

	Mono<ServerResponse> getAllItemReviewsFromCacheManagement();

	Mono<ServerResponse> getItemReviewByItemInventoryIdFromCacheManagement(String itemInventoryId);

	Mono<ServerResponse> getItemReviewByIdFromCacheManagement(String id);

	Mono<ServerResponse> rebuildItemReviewCacheFromDBManagement();

	Mono<ServerResponse> deleteItemReviewCacheManagement();

	Mono<ServerResponse> deleteItemReviewByIdFromCacheManagement(String id);
}
