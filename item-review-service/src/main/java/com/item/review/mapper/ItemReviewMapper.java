package com.item.review.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.item.review.entity.ItemReviewEntity;
import com.item.review.model.ItemReviewDTO;
import com.item.review.model.request.ItemReviewDTORequest;
import com.item.review.model.response.ErrorDTOResponse;
import com.item.review.model.response.ItemReviewDTOResponse;
import com.item.review.model.response.ResponseDTO;

import reactor.core.publisher.Mono;

public class ItemReviewMapper {

	public static ItemReviewEntity mapItemReviewDTORequestToItemReviewEntity(
			ItemReviewDTORequest itemReviewDTORequest) {
		return ItemReviewEntity.builder()
				.id(itemReviewDTORequest.getId())
				.itemInventoryId(itemReviewDTORequest.getItemInventoryId())
				.feedback(itemReviewDTORequest.getFeedback())
				.rating(itemReviewDTORequest.getRating())
				.build();
	}

	public static ItemReviewDTO mapItemReviewEntityToItemReviewDTO(ItemReviewEntity itemReviewEntity) {
		return ItemReviewDTO.builder()
				.id(itemReviewEntity.getId())
				.itemInventoryId(itemReviewEntity.getItemInventoryId())
				.feedback(itemReviewEntity.getFeedback())
				.rating(itemReviewEntity.getRating())
				.build();
	}

	public static List<ItemReviewDTO> mapItemReviewEntityListToItemReviewDTOList(
			List<ItemReviewEntity> itemReviewEntityList) {

		return itemReviewEntityList.parallelStream().map(i -> {
			return mapItemReviewEntityToItemReviewDTO(i);
		}).collect(Collectors.toList());
	}

	public static ItemReviewDTOResponse buildItemReviewDTOResponse(List<ItemReviewDTO> itemReviewDTOList) {
		int resultSetSize = CollectionUtils.isEmpty(itemReviewDTOList) ? 0 : itemReviewDTOList.size();
		List<ItemReviewDTO> list = CollectionUtils.isEmpty(itemReviewDTOList) ? new ArrayList<>() : itemReviewDTOList;

		return ItemReviewDTOResponse.builder().resultSetSize(resultSetSize).itemReviewDTOList(list).build();
	}

	public static Mono<ServerResponse> buildServerResponseMonoWithDTOResponse(ResponseDTO responseDTO,
			HttpStatus httpStatus) {

		return ServerResponse.status(httpStatus).bodyValue(responseDTO);
	}

	public static ItemReviewEntity buildUpdatedItemReviewEntityWithOrigEntityAndNewDTO(
			ItemReviewEntity origItemReviewEntity, ItemReviewDTORequest updatedItemReviewDTOrequest) {

		return ItemReviewEntity.builder()
				.id(origItemReviewEntity.getId())
				.itemInventoryId(updatedItemReviewDTOrequest.getItemInventoryId())
				.feedback(updatedItemReviewDTOrequest.getFeedback())
				.rating(updatedItemReviewDTOrequest.getRating())
				.build();
	}

	public static Mono<ServerResponse> generateItemNotFoundResponse() {

		return buildServerResponseMonoWithDTOResponse(buildItemReviewDTOResponse(new ArrayList<>()),
				HttpStatus.NOT_FOUND);
	}

	public static ErrorDTOResponse buildErrorDTOResponse(String errorMessages, HttpStatus httpStatus) {

		return ErrorDTOResponse.builder()
				.errorCode(httpStatus.value())
				.errorMessage(errorMessages)
				.status(httpStatus.name())
				.build();
	}
}
