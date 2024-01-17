package com.item.review.handler;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.item.review.model.request.ItemReviewDTORequest;
import com.item.review.service.ItemReviewService;
import com.item.review.validation.ItemReviewValidator;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ItemReviewHandler {
	private ItemReviewService itemReviewService;

	private ItemReviewValidator itemReviewValidator;

	public ItemReviewHandler(ItemReviewService itemReviewService, ItemReviewValidator itemReviewValidator) {
		this.itemReviewService = itemReviewService;
		this.itemReviewValidator = itemReviewValidator;
	}

	public Mono<ServerResponse> handleAddNewItemReview(ServerRequest serverRequest) {
		log.debug("handleAddNewItemReview");

		return serverRequest.bodyToMono(ItemReviewDTORequest.class).flatMap(itemReviewDTORequest -> {
			String validations = itemReviewValidator.checkItemReviewDTORequestValidations(itemReviewDTORequest);

			return StringUtils.isEmpty(validations) ? itemReviewService.addNewItemReview(itemReviewDTORequest)
					: itemReviewService.buildBadRequestServerResponseMono(validations);
		}).switchIfEmpty(itemReviewService.buildBadRequestServerResponseMono("Request Body Cannot Be Empty"));
	}

	public Mono<ServerResponse> handleGetAllItemReviews(ServerRequest serverRequest) {
		log.debug("handleGetAllItemReviews");

		Optional<String> itemInventoryIdOPT = serverRequest.queryParam("itemInventoryId");

		return itemInventoryIdOPT.isPresent()
				? itemReviewService.getItemReviewsByItemInventoryId(itemInventoryIdOPT.get())
				: itemReviewService.getAllItemReviews();
	}

	public Mono<ServerResponse> handleGetItemReviewById(ServerRequest serverRequest) {
		String id = serverRequest.pathVariable("id");

		log.debug("handleGetItemReviewById id={}", id);

		return itemReviewService.getItemReviewById(id);
	}

	public Mono<ServerResponse> handleUpdateItemReviewById(ServerRequest serverRequest) {
		String id = serverRequest.pathVariable("id");

		log.debug("handleUpdateItemReviewById id={}", id);

		return serverRequest.bodyToMono(ItemReviewDTORequest.class).flatMap(updatedItemReviewDTORequest -> {

			String validations = itemReviewValidator.checkItemReviewDTORequestValidations(updatedItemReviewDTORequest);

			return StringUtils.isEmpty(validations)
					? itemReviewService.updateItemReviewById(id, updatedItemReviewDTORequest)
					: itemReviewService.buildBadRequestServerResponseMono(validations);
		}).switchIfEmpty(itemReviewService.buildBadRequestServerResponseMono("Request Body Cannot Be Empty"));
	}

	public Mono<ServerResponse> handleDeleteAllItemReviews() {
		log.debug("handleDeleteAllItemReviews");

		return itemReviewService.deleteAllItemReviews();
	}

	public Mono<ServerResponse> handleDeleteItemReviewById(ServerRequest serverRequest) {
		String id = serverRequest.pathVariable("id");

		log.debug("handleDeleteItemReviewById id={}", id);

		return itemReviewService.deleteItemReviewById(id);
	}

	public Mono<ServerResponse> handleInitTestDataBySize(ServerRequest serverRequest) {
		String size = serverRequest.pathVariable("size");

		log.debug("handleInitTestDataBySize size={}", size);

		int initTestDataSize = itemReviewValidator.checkInitTestDataSize(size);
		return 0 < initTestDataSize ? itemReviewService.initTestDataBySize(initTestDataSize)
				: itemReviewService.buildBadRequestServerResponseMono(
						"initTestDataSize has to be 1 or greater initTestDataSize=" + initTestDataSize);
	}
}