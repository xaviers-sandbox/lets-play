package com.item.review.service.management.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.item.review.entity.ItemReviewEntity;
import com.item.review.mapper.ItemReviewMapper;
import com.item.review.model.request.ItemReviewDTORequest;
import com.item.review.service.ProcessItemReviewEntity;
import com.item.review.service.cache.ItemReviewCacheService;
import com.item.review.service.management.CacheManagementService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CacheManagementServiceImpl extends ProcessItemReviewEntity implements CacheManagementService {
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
}
