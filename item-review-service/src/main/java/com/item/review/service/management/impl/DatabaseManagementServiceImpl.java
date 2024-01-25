package com.item.review.service.management.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.item.review.entity.ItemReviewEntity;
import com.item.review.repository.ItemReviewRepository;
import com.item.review.service.ProcessItemReviewEntityService;
import com.item.review.service.management.DatabaseManagementService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class DatabaseManagementServiceImpl extends ProcessItemReviewEntityService implements DatabaseManagementService {
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
}
