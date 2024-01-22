package com.item.review.service.management.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.item.review.entity.ItemReviewEntity;
import com.item.review.mapper.ItemReviewMapper;
import com.item.review.model.ItemReviewDTO;
import com.item.review.model.response.ItemReviewDTOResponse;
import com.item.review.repository.ItemReviewRepository;
import com.item.review.service.management.DatabaseManagementService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class DatabaseManagementServiceImpl implements DatabaseManagementService {
	private ItemReviewRepository itemReviewRepository;

	public DatabaseManagementServiceImpl(ItemReviewRepository itemReviewRepository) {
		this.itemReviewRepository = itemReviewRepository;
	}

	@Override
	public Mono<ServerResponse> getAllItemReviewsFromDBManagement() {
		log.debug("getAllItemReviewsFromDBManagement");

		Flux<ItemReviewEntity> itemReviewEntityFlux = itemReviewRepository.findAll();

		return processItemReviewEntityFlux(itemReviewEntityFlux, HttpStatus.OK);
	}

	@Override
	public Mono<ServerResponse> getItemReviewByItemInventoryIdFromDBManagement(String itemInventoryId) {
		log.debug("getItemReviewByItemInventoryIdFromDBManagement itemInventoryId={}", itemInventoryId);

		Flux<ItemReviewEntity> itemReviewEntityFlux = itemReviewRepository.findByItemInventoryId(itemInventoryId);

		return processItemReviewEntityFlux(itemReviewEntityFlux, HttpStatus.OK);
	}

	@Override
	public Mono<ServerResponse> getItemReviewByIdFromDBManagement(String id) {
		log.debug("getItemReviewByIdFromDBManagement id={}", id);

		Mono<ItemReviewEntity> itemReviewEntityMono = itemReviewRepository.findById(id);

		return processItemReviewEntityMono(itemReviewEntityMono, HttpStatus.OK);
	}

	public Mono<ServerResponse> processItemReviewEntityMono(Mono<ItemReviewEntity> itemReviewEntityMono,
			HttpStatus httpStatus) {
		log.debug("processItemReviewEntityMono");

		return itemReviewEntityMono.flatMap(returnedItemReviewEntity -> {
			Mono<ServerResponse> serverResponseMono = Stream
					.of(ItemReviewMapper.mapItemReviewEntityToItemReviewDTO(returnedItemReviewEntity))
					.map(itemReviewDTO -> {
						List<ItemReviewDTO> itemReviewDTOList = new ArrayList<>(Arrays.asList(itemReviewDTO));
						return ItemReviewMapper.buildItemReviewDTOResponse(itemReviewDTOList);
					})
					.map(itemReviewDTOResponse -> ItemReviewMapper
							.buildServerResponseMonoWithDTOResponse(itemReviewDTOResponse, httpStatus))
					.findAny()
					.get();

			return serverResponseMono;
		});
	}

	public Mono<ServerResponse> processItemReviewEntityFlux(Flux<ItemReviewEntity> itemReviewEntityFlux,
			HttpStatus httpStatus) {
		log.debug("processItemReviewEntityFlux");

		return itemReviewEntityFlux.collectList().flatMap(itemReviewEntityList -> {

			List<ItemReviewDTO> itemReviewDTOList = ItemReviewMapper
					.mapItemReviewEntityListToItemReviewDTOList(itemReviewEntityList);

			ItemReviewDTOResponse itemReviewDTOResponse = ItemReviewMapper
					.buildItemReviewDTOResponse(itemReviewDTOList);

			return ItemReviewMapper.buildServerResponseMonoWithDTOResponse(itemReviewDTOResponse, httpStatus);
		});
	}

}
