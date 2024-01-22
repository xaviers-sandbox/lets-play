package com.item.review.handler.management;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.item.review.model.request.ItemReviewDTORequest;
import com.item.review.service.ItemReviewService;
import com.item.review.service.management.CacheManagementService;
import com.item.review.validation.ItemReviewValidator;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CacheManagementHandler {
	private ItemReviewValidator itemReviewValidator;
	private CacheManagementService cacheManagementService;
	private ItemReviewService itemReviewService;

	public CacheManagementHandler(ItemReviewValidator itemReviewValidator, ItemReviewService itemReviewService,
			CacheManagementService cacheManagementService) {
		this.itemReviewValidator = itemReviewValidator;
		this.itemReviewService = itemReviewService;
		this.cacheManagementService = cacheManagementService;
	}

	public Mono<ServerResponse> handleAddNewItemReviewToCacheManagement(ServerRequest serverRequest) {

		log.debug("handleAddNewItemReviewToCacheManagement");

		return serverRequest.bodyToMono(ItemReviewDTORequest.class).flatMap(itemReviewDTORequest -> {
			String validations = itemReviewValidator.checkItemReviewDTORequestValidations(itemReviewDTORequest);

			return StringUtils.isEmpty(validations)
					? cacheManagementService.addNewItemReviewToCacheManagement(itemReviewDTORequest)
					: itemReviewService.buildBadRequestServerResponseMono(validations);
		}).switchIfEmpty(itemReviewService.buildBadRequestServerResponseMono("Request Body Cannot Be Empty"));
	}

	public Mono<ServerResponse> handleGetAllItemReviewsFromCacheManagement(ServerRequest serverRequest) {

		log.debug("handleGetAllItemReviewsFromCacheManagement");

		Optional<String> itemInventoryIdOPT = serverRequest.queryParam("itemInventoryId");

		return itemInventoryIdOPT.isPresent()
				? cacheManagementService.getItemReviewByItemInventoryIdFromCacheManagement(itemInventoryIdOPT.get())
				: cacheManagementService.getAllItemReviewsFromCacheManagement();
	}

	public Mono<ServerResponse> handleGetItemReviewByIdFromCacheManagement(ServerRequest serverRequest) {

		String id = serverRequest.pathVariable("id");

		log.debug("handleGetItemReviewByIdFromCacheManagement id={}", id);

		return cacheManagementService.getItemReviewByIdFromCacheManagement(id);
	}

	public Mono<ServerResponse> handleRebuildItemReviewCacheFromDBManagement(ServerRequest serverRequest) {

		log.debug("handleRebuildItemReviewCacheFromDBManagement - is this null=" + cacheManagementService);

		return cacheManagementService.rebuildItemReviewCacheFromDBManagement();
	}

	public Mono<ServerResponse> handleDeleteAllItemReviews() {

		return cacheManagementService.deleteItemReviewCacheManagement();
	}

	public Mono<ServerResponse> handleDeleteItemReviewByIdFromCacheManagement(ServerRequest serverRequest) {
		String id = serverRequest.pathVariable("id");

		log.debug("handleDeleteItemReviewByIdFromCacheManagement id={}", id);

		return cacheManagementService.deleteItemReviewByIdFromCacheManagement(id);
	}

}
