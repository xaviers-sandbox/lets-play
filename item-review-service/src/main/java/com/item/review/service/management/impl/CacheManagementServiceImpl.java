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
import com.item.review.model.request.ItemReviewDTORequest;
import com.item.review.model.response.ItemReviewDTOResponse;
import com.item.review.service.cache.ItemReviewCacheService;
import com.item.review.service.management.CacheManagementService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CacheManagementServiceImpl implements CacheManagementService {
	private ItemReviewCacheService itemReviewCacheService;

	public CacheManagementServiceImpl(ItemReviewCacheService itemReviewCacheService) {
		this.itemReviewCacheService = itemReviewCacheService;
	}

	@Override
	public Mono<ServerResponse> addNewItemReviewToCacheManagement(ItemReviewDTORequest newItemReviewDTOrequest) {
		log.debug("addNewItemReviewToCacheManagement - newItemReviewDTOrequest=", newItemReviewDTOrequest);

		ItemReviewEntity itemReviewEntity = ItemReviewMapper
				.mapItemReviewDTORequestToItemReviewEntity(newItemReviewDTOrequest);

		Mono<ItemReviewEntity> itemReviewEntityMono = itemReviewCacheService.addNewItemReviewToCache(itemReviewEntity);

		return processItemReviewEntityMono(itemReviewEntityMono, HttpStatus.CREATED);
	}

	@Override
	public Mono<ServerResponse> getAllItemReviewsFromCacheManagement() {
		log.debug("getAllItemReviewsFromCacheManagement");

		Flux<ItemReviewEntity> itemReviewEntityFlux = itemReviewCacheService.getAllItemReviewsFromCache();

		return processItemReviewEntityFlux(itemReviewEntityFlux, HttpStatus.OK);
	}

	@Override
	public Mono<ServerResponse> getItemReviewByItemInventoryIdFromCacheManagement(String itemInventoryId) {
		log.debug("getItemReviewByItemInventoryIdFromCacheManagement itemInventoryId={}", itemInventoryId);

		Flux<ItemReviewEntity> itemReviewEntityFlux = itemReviewCacheService
				.getItemReviewByItemInventoryIdFromCache(itemInventoryId);

		return processItemReviewEntityFlux(itemReviewEntityFlux, HttpStatus.OK);
	}

	@Override
	public Mono<ServerResponse> getItemReviewByIdFromCacheManagement(String id) {
		log.debug("getItemReviewByIdFromCacheManagement id={}", id);

		Mono<ItemReviewEntity> itemReviewEntityMono = itemReviewCacheService.getItemReviewyByIdFromCache(id);

		return processItemReviewEntityMono(itemReviewEntityMono, HttpStatus.OK)
				.switchIfEmpty(ItemReviewMapper.generateItemNotFoundResponse());
	}

	@Override
	public Mono<ServerResponse> rebuildItemReviewCacheFromDBManagement() {
		log.debug("rebuildItemReviewCacheFromDBManagement");

		Flux<ItemReviewEntity> itemReviewEntityFlux = itemReviewCacheService.rebuildItemReviewCacheFromDB();

		return processItemReviewEntityFlux(itemReviewEntityFlux, HttpStatus.OK);
	}

	@Override
	public Mono<ServerResponse> deleteItemReviewCacheManagement() {
		log.debug("deleteItemReviewCacheManagement");

		itemReviewCacheService.deleteItemReviewCache().subscribe();

		return Mono.empty();
	}

	@Override
	public Mono<ServerResponse> deleteItemReviewByIdFromCacheManagement(String id) {
		log.debug("deleteItemReviewByIdFromCacheManagement - id={}", id);

		itemReviewCacheService.deleteItemReviewByIdFromCache(id).subscribe();

		return Mono.empty();
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
