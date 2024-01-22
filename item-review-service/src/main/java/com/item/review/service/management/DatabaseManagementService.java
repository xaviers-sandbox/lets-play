package com.item.review.service.management;

import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

public interface DatabaseManagementService {
	Mono<ServerResponse> getAllItemReviewsFromDBManagement();

	Mono<ServerResponse> getItemReviewByItemInventoryIdFromDBManagement(String itemInventoryId);

	Mono<ServerResponse> getItemReviewByIdFromDBManagement(String id);
}
