package com.item.review.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.item.review.entity.ItemReviewEntity;
import com.item.review.mapper.ItemReviewMapper;
import com.item.review.model.request.ItemReviewDTORequest;
import com.item.review.model.response.ErrorDTOResponse;
import com.item.review.repository.ItemReviewRepository;
import com.item.review.service.ItemReviewService;
import com.item.review.service.ProcessItemReviewEntityService;
import com.item.review.util.ItemReviewUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ItemReviewServiceImpl extends ProcessItemReviewEntityService implements ItemReviewService {
	private ItemReviewRepository itemReviewRepo;

	public ItemReviewServiceImpl(ItemReviewRepository itemReviewRepo) {
		this.itemReviewRepo = itemReviewRepo;
	}

	@Override
	public Mono<ServerResponse> addNewItemReview(ItemReviewDTORequest newItemReviewDTOrequest) {
		log.debug("addNewItemReview newItemReviewDTOrequest={}", newItemReviewDTOrequest);

		ItemReviewEntity itemReviewEntity = ItemReviewMapper
				.mapItemReviewDTORequestToItemReviewEntity(newItemReviewDTOrequest);

		Mono<ItemReviewEntity> itemReviewEntityMono = itemReviewRepo.save(itemReviewEntity);

		return processItemReviewEntityMono(itemReviewEntityMono, HttpStatus.CREATED);
	}

	@Override
	public Mono<ServerResponse> getAllItemReviews() {
		log.debug("getAllItemReviews");

		Flux<ItemReviewEntity> itemReviewEntityFlux = itemReviewRepo.findAll();

		return processItemReviewEntityFlux(itemReviewEntityFlux, HttpStatus.OK);
	}

	@Override
	public Mono<ServerResponse> getItemReviewById(String id) {
		log.debug("getItemReviewById id={}", id);

		Mono<ItemReviewEntity> itemReviewEntityMono = itemReviewRepo.findById(id);

		return processItemReviewEntityMono(itemReviewEntityMono, HttpStatus.OK)
				.switchIfEmpty(ItemReviewMapper.generateItemNotFoundResponse());
	}

	@Override
	public Mono<ServerResponse> getItemReviewsByItemInventoryId(String itemInventoryId) {
		log.debug("getItemReviewsByItemInventoryId itemInventoryId={}", itemInventoryId);

		Flux<ItemReviewEntity> itemReviewEntityFlux = itemReviewRepo.findByItemInventoryId(itemInventoryId);

		return processItemReviewEntityFlux(itemReviewEntityFlux, HttpStatus.OK);
	}

	@Override
	public Mono<ServerResponse> updateItemReviewById(String id, ItemReviewDTORequest updatedItemReviewDTOrequest) {
		log.debug("updateItemReviewById id={} updatedItemReviewDTOrequest={}", id, updatedItemReviewDTOrequest);

		Mono<ItemReviewEntity> itemReviewEntityMono = itemReviewRepo.findById(id);

		return itemReviewEntityMono.flatMap(oigItemReviewEntity -> {

			ItemReviewEntity updatedItemReviewEntity = ItemReviewMapper
					.buildUpdatedItemReviewEntityWithOrigEntityAndNewDTO(oigItemReviewEntity,
							updatedItemReviewDTOrequest);

			Mono<ItemReviewEntity> updatedItemReviewEntityMono = itemReviewRepo.save(updatedItemReviewEntity);

			return processItemReviewEntityMono(updatedItemReviewEntityMono, HttpStatus.OK);

		}).switchIfEmpty(ItemReviewMapper.generateItemNotFoundResponse());
	}

	@Override
	public Mono<ServerResponse> deleteAllItemReviews() {
		log.debug("deleteAllItemReviews");

		return itemReviewRepo.deleteAll()
				.then(ItemReviewMapper.buildServerResponseMonoWithDTOResponse(
						ItemReviewMapper.buildItemReviewDTOResponse(new ArrayList<>()),
						HttpStatus.NO_CONTENT));
	}

	@Override
	public Mono<ServerResponse> deleteItemReviewById(String id) {
		log.debug("deleteItemReviewById id={}", id);

		return itemReviewRepo.deleteById(id)
				.then(ItemReviewMapper.buildServerResponseMonoWithDTOResponse(
						ItemReviewMapper.buildItemReviewDTOResponse(new ArrayList<>()),
						HttpStatus.NO_CONTENT));
	}

	@Override
	public Mono<ServerResponse> buildBadRequestServerResponseMono(String errorMessages) {
		log.debug("buildBadRequestServerResponseMono errorMessages={}", errorMessages);

		ErrorDTOResponse errorDTOResponse = ItemReviewMapper.buildErrorDTOResponse(errorMessages,
				HttpStatus.BAD_REQUEST);

		return ItemReviewMapper.buildServerResponseMonoWithDTOResponse(errorDTOResponse, HttpStatus.BAD_REQUEST);
	}

	@Override
	public Mono<ServerResponse> initTestDataBySize(int initDataSize) {
		log.debug("initTestDataBySize initDataSize={}", initDataSize);

		List<ItemReviewEntity> itemReviewEntityList = ItemReviewUtil.generateItemReviewEntityList(initDataSize);

		Flux<ItemReviewEntity> itemReviewEntityFlux = itemReviewRepo.saveAll(itemReviewEntityList);

		return processItemReviewEntityFlux(itemReviewEntityFlux, HttpStatus.CREATED);
	}
}