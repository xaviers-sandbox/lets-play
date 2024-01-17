package com.item.inventory.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.item.inventory.entity.ItemInventoryEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ItemInventoryRepository extends ReactiveMongoRepository<ItemInventoryEntity, String> {
	Flux<ItemInventoryEntity> findByNameIgnoreCase(String name);

	Mono<ItemInventoryEntity> findByIdIgnoreCaseAndNameIgnoreCase(String id, String name);

	Flux<ItemInventoryEntity> findByPriceBetween(Double priceGT, Double priceLT);

	Flux<ItemInventoryEntity> findByQuantityBetween(int quantityGT, int quantityLT);

	Flux<ItemInventoryEntity> findAllByOrderByNameAsc();
}
