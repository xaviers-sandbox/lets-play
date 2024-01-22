package com.item.review.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.item.review.entity.ItemReviewEntity;
import com.item.review.mapper.ItemReviewMapper;
import com.item.review.model.ItemReviewDTO;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public abstract class ProcessItemReviewEntity {
	public Mono<ServerResponse> processItemReviewEntityMono(Mono<ItemReviewEntity> itemReviewEntityMono,
			HttpStatus httpStatus) {
		log.debug("processItemReviewEntityMono");

		return itemReviewEntityMono.flatMap(returnedItemReviewEntity -> {
			return Stream.of(ItemReviewMapper.mapItemReviewEntityToItemReviewDTO(returnedItemReviewEntity))
					.map(itemReviewDTO -> {
						List<ItemReviewDTO> itemReviewDTOList = new ArrayList<>(Arrays.asList(itemReviewDTO));
						return ItemReviewMapper.buildItemReviewDTOResponse(itemReviewDTOList);
					})
					.map(itemReviewDTOResponse -> ItemReviewMapper
							.buildServerResponseMonoWithDTOResponse(itemReviewDTOResponse, httpStatus))
					.findAny()
					.get();
		});
	}

	public Mono<ServerResponse> processItemReviewEntityFlux(Flux<ItemReviewEntity> itemReviewEntityFlux,
			HttpStatus httpStatus) {
		log.debug("processItemReviewEntityFlux");

		return itemReviewEntityFlux.collectList().flatMap(itemReviewEntityList -> {
			return Stream.of(ItemReviewMapper.mapItemReviewEntityListToItemReviewDTOList(itemReviewEntityList))
					.map(ItemReviewMapper::buildItemReviewDTOResponse)
					.map(itemReviewDTOResponse -> ItemReviewMapper
							.buildServerResponseMonoWithDTOResponse(itemReviewDTOResponse, httpStatus))
					.findAny()
					.get();
		});
	}
}
