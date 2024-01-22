package com.item.review.service.cache.impl;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.stereotype.Service;

import com.item.review.entity.ItemReviewEntity;
import com.item.review.repository.ItemReviewRepository;
import com.item.review.service.cache.ItemReviewCacheService;

import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ItemReviewCacheServiceImpl implements ItemReviewCacheService {
	private final String ITEM_REVIEW_MAP_KEY = "itemReviewEntity";

	Faker f = new Faker();

	private ItemReviewRepository itemReviewRepository;

	private RMapReactive<String, ItemReviewEntity> rMapReactive;

	public ItemReviewCacheServiceImpl(ItemReviewRepository itemReviewRepository,
			RedissonReactiveClient redissonReactiveClient) {
		this.itemReviewRepository = itemReviewRepository;
		this.rMapReactive = redissonReactiveClient.getMap(ITEM_REVIEW_MAP_KEY,
				new TypedJsonJacksonCodec(String.class, ItemReviewEntity.class));
	}

	@Override
	public Mono<ItemReviewEntity> addNewItemReviewToCache(ItemReviewEntity newItemReviewEntity) {
		log.debug("addNewItemReviewToCache - newItemReviewEntity={}", newItemReviewEntity);

		int itemInventoryId = f.number().numberBetween(99999, 9999999);
		String name = f.dcComics().name();

		newItemReviewEntity.setId(UUID.randomUUID().toString());
		newItemReviewEntity.setFeedback(name);
		newItemReviewEntity.setItemInventoryId(String.valueOf(itemInventoryId));

		return rMapReactive.fastPut(newItemReviewEntity.getId(), newItemReviewEntity)
				.thenReturn(newItemReviewEntity)
				.doFinally(signalType -> rMapReactive.expire(Duration.ofHours(100L)).subscribe());
	}

	@Override
	public Flux<ItemReviewEntity> getAllItemReviewsFromCache() {
		log.debug("getAllItemReviewsFromCache");

		Flux<ItemReviewEntity> ItemReviewEntityFlux = rMapReactive.readAllValues().flatMapMany(i -> {
			List<ItemReviewEntity> itemReviewEntityList = i.parallelStream().toList();

			return Flux.fromIterable(itemReviewEntityList);
		});// .switchIfEmpty(wasThisCalled());
		return ItemReviewEntityFlux;
	}

	@Override
	public Flux<ItemReviewEntity> getItemReviewByItemInventoryIdFromCache(String itemInventoryId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<ItemReviewEntity> getItemReviewyByIdFromCache(String id) {
		log.debug("getItemReviewyByIdFromCache - id={}", id);

		return rMapReactive.get(id);
	}

	@Override
	public Flux<ItemReviewEntity> rebuildItemReviewCacheFromDB() {
		log.debug("rebuildItemReviewCacheFromDB");

		Flux<ItemReviewEntity> itemReviewEntityFlux = itemReviewRepository.findAll();

		itemReviewEntityFlux.flatMap(itemReviewEntity -> {
			updateItemReviewCacheManagement(itemReviewEntity).subscribe();

			return Mono.empty();
		}).subscribe();

		return itemReviewEntityFlux;
	}

	@Override
	public Mono<Void> deleteItemReviewCache() {
		log.debug("deleteItemReviewCache");

		rMapReactive.delete().subscribe();

		return Mono.empty();
	}

	@Override
	public Mono<Void> deleteItemReviewByIdFromCache(String id) {
		log.debug("deleteItemReviewByIdFromCache - id={}", id);

		rMapReactive.fastRemove(id).subscribe();

		return Mono.empty();
	}

	public Mono<ItemReviewEntity> updateItemReviewCacheManagement(ItemReviewEntity itemReviewEntity) {
		log.debug("updateItemReviewCacheManagement - itemReviewEntity={}", itemReviewEntity);

		return rMapReactive.fastPut(itemReviewEntity.getId(), itemReviewEntity)
				.thenReturn(itemReviewEntity)
				.doFinally(signalType -> rMapReactive.expire(Duration.ofHours(1L)).subscribe());
	}
}
