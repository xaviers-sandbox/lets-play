package com.item.review.service.cache;

import com.item.review.entity.ItemReviewEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemReviewCacheService {
	public Mono<ItemReviewEntity> addNewItemReviewToCache(ItemReviewEntity newItemReviewEntity);

	public Flux<ItemReviewEntity> getAllItemReviewsFromCache();

	public Flux<ItemReviewEntity> getItemReviewByItemInventoryIdFromCache(String itemInventoryId);

	public Mono<ItemReviewEntity> getItemReviewyByIdFromCache(String id);

	public Flux<ItemReviewEntity> rebuildItemReviewCacheFromDB();

	public Mono<Void> deleteItemReviewCache();

	public Mono<Void> deleteItemReviewByIdFromCache(String id);
}
