package com.item.review.handler.management;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.item.review.service.management.DatabaseManagementService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class DatabaseManagementHandler {
	private DatabaseManagementService databaseManagementService;

	public DatabaseManagementHandler(DatabaseManagementService databaseManagementService) {
		this.databaseManagementService = databaseManagementService;
	}

	public Mono<ServerResponse> handleGetAllItemReviewsFromDBManagement(ServerRequest serverRequest) {
		log.debug("handleGetAllItemReviewsFromCacheManagement");

		Optional<String> itemInventoryIdOPT = serverRequest.queryParam("itemInventoryId");

		return itemInventoryIdOPT.isPresent()
				? databaseManagementService.getItemReviewByItemInventoryIdFromDBManagement(itemInventoryIdOPT.get())
				: databaseManagementService.getAllItemReviewsFromDBManagement();
	}

	public Mono<ServerResponse> handleGetItemReviewByIdFromDBManagement(ServerRequest serverRequest) {
		String id = serverRequest.pathVariable("id");

		log.debug("handleGetItemReviewByIdFromDBManagement id={}", id);

		return databaseManagementService.getItemReviewByIdFromDBManagement(id);
	}
}
