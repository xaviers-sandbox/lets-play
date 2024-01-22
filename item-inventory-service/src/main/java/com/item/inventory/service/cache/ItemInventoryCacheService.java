package com.item.inventory.service.cache;

import com.item.inventory.entity.ItemInventoryEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemInventoryCacheService {
	public Mono<ItemInventoryEntity> addNewItemInventoryToCache(ItemInventoryEntity newItemInventoryEntity);

	public Flux<ItemInventoryEntity> getAllItemInventoriesFromCache();

	public Mono<ItemInventoryEntity> getItemInventoryByIdFromCache(String id);

	public Flux<ItemInventoryEntity> rebuildItemInventoryCacheFromDB();

	public Mono<Void> deleteItemInventoryCache();

	public Mono<Void> deleteItemInventoryByIdFromCache(String id);
}
