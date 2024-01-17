package com.item.review.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.item.review.entity.ItemReviewEntity;

import reactor.core.publisher.Flux;

@Repository
public interface ItemReviewRepository extends ReactiveMongoRepository<ItemReviewEntity, String> {
//	Flux<ItemReviewEntity> findByNameIgnoreCase(String name);
//
//	Mono<ItemReviewEntity> findByIdIgnoreCaseAndNameIgnoreCase(String id, String name);
//
//	Flux<ItemReviewEntity> findByPriceBetween(Double priceGT, Double priceLT);
//
//	Flux<ItemReviewEntity> findByQuantityBetween(int quantityGT, int quantityLT);
//
//	Flux<ItemReviewEntity> findAllByOrderByNameAsc();

	Flux<ItemReviewEntity> findByItemInventoryId(String itemInventoryId);
}
