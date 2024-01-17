package com.online.store.mapper;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.item.inventory.model.response.ItemInventoryDTOResponse;
import com.item.review.model.response.ItemReviewDTOResponse;
import com.online.store.service.model.OnlineStoreDTOResponse;
import com.online.store.service.model.ResponseDTO;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class OnlineStoreMapper {

	public static OnlineStoreDTOResponse buildOnlineStoreDTOResponseWithItemReviewDTOResponse(
			ItemReviewDTOResponse itemReviewDTOResponse) {

		return OnlineStoreDTOResponse.builder().itemReviewDTOResponse(itemReviewDTOResponse).build();
	}

	public static OnlineStoreDTOResponse buildOnlineStoreDTOResponsWithItemInventoryDTOResponse(
			ItemInventoryDTOResponse itemInventoryDTOResponse) {

		return OnlineStoreDTOResponse.builder().itemInventoryDTOResponse(itemInventoryDTOResponse).build();
	}

	public static OnlineStoreDTOResponse buildOnlineStoreDTOResponsWithItemInventoryDTOResponseAndItemReviewDTOResponse(
			ItemInventoryDTOResponse itemInventoryDTOResponse, ItemReviewDTOResponse itemReviewDTOResponse) {

		return OnlineStoreDTOResponse.builder()
				.itemInventoryDTOResponse(itemInventoryDTOResponse)
				.itemReviewDTOResponse(itemReviewDTOResponse)
				.build();
	}

	public static Mono<ServerResponse> buildServerResponseMonoWithDTOResponse(ResponseDTO onlineStoreDTOResponse,
			HttpStatus httpStatus) {

		return ServerResponse.status(httpStatus).bodyValue(onlineStoreDTOResponse);
	}

	public static Mono<ServerResponse> buildDefaultEmptyServerResponseMono() {
		log.debug("900000");
		return ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(new OnlineStoreDTOResponse());
	}
}
