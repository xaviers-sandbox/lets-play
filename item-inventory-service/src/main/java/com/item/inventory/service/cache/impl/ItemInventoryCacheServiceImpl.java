package com.item.inventory.service.cache.impl;

import java.time.Duration;
import java.util.List;

import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.stereotype.Service;

import com.item.inventory.entity.ItemInventoryEntity;
import com.item.inventory.repository.ItemInventoryRepository;
import com.item.inventory.service.cache.ItemInventoryCacheService;

import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ItemInventoryCacheServiceImpl implements ItemInventoryCacheService {
	private final String ITEM_INVENTORY_MAP_KEY = "itemInventoryEntity";

	private static Faker f = new Faker();

	private ItemInventoryRepository itemInventoryRepository;

	private RMapReactive<String, ItemInventoryEntity> rMapReactive;

	public ItemInventoryCacheServiceImpl(ItemInventoryRepository itemInventoryRepository,
			RedissonReactiveClient redissonReactiveClient) {
		this.itemInventoryRepository = itemInventoryRepository;
		this.rMapReactive = redissonReactiveClient.getMap(ITEM_INVENTORY_MAP_KEY,
				new TypedJsonJacksonCodec(String.class, ItemInventoryEntity.class));
	}

	@Override
	public Mono<ItemInventoryEntity> addNewItemInventoryToCache(ItemInventoryEntity newItemInventoryEntity) {
		log.debug("addNewItemInventoryToCache - newItemInventoryEntity={}", newItemInventoryEntity);

		int id = f.number().numberBetween(99999, 9999999);
		String name = f.dcComics().name();

		newItemInventoryEntity.setId(String.valueOf(id));
		newItemInventoryEntity.setName(name);

		return rMapReactive.fastPut(newItemInventoryEntity.getId(), newItemInventoryEntity)
				.thenReturn(newItemInventoryEntity)
				.doFinally(signalType -> rMapReactive.expire(Duration.ofHours(100L)).subscribe());

	}

	@Override
	public Flux<ItemInventoryEntity> getAllItemInventoriesFromCache() {
		log.debug("getAllItemInventoriesFromCache");

		Flux<ItemInventoryEntity> itemInventoryEntityFlux = rMapReactive.readAllValues().flatMapMany(i -> {
			List<ItemInventoryEntity> itemInventoryEntityList = i.parallelStream().toList();

			return Flux.fromIterable(itemInventoryEntityList);
		});// .switchIfEmpty(wasThisCalled());
		return itemInventoryEntityFlux;
	}

	@Override
	public Mono<ItemInventoryEntity> getItemInventoryByIdFromCache(String id) {
		log.debug("getItemInventoryByIdFromCache - id={}", id);

		return rMapReactive.get(id);
	}

	@Override
	public Flux<ItemInventoryEntity> rebuildItemInventoryCacheFromDB() {
		log.debug("rebuildItemInventoryCacheFromDB");

		Flux<ItemInventoryEntity> itemInventoryEntityFlux = itemInventoryRepository.findAllByOrderByNameAsc();

		itemInventoryEntityFlux.flatMap(itemInventoryEntity -> {
			updateItemInventoryCacheManagement(itemInventoryEntity).subscribe();

			return Mono.empty();
		}).subscribe();

		return itemInventoryEntityFlux;
	}

	@Override
	public Mono<Void> deleteItemInventoryCache() {
		log.debug("deleteItemInventoryCache");

		rMapReactive.delete().subscribe();

		return Mono.empty();
	}

	@Override
	public Mono<Void> deleteItemInventoryByIdFromCache(String id) {
		log.debug("deleteItemInventoryByIdFromCache - id={}", id);

		rMapReactive.fastRemove(id).subscribe();

		return Mono.empty();
	}

	public Mono<ItemInventoryEntity> updateItemInventoryCacheManagement(ItemInventoryEntity itemInventoryEntity) {
		log.debug("updateItemInventoryCacheManagement - itemInventoryEntity={}", itemInventoryEntity);

		return rMapReactive.fastPut(itemInventoryEntity.getId(), itemInventoryEntity)
				.thenReturn(itemInventoryEntity)
				.doFinally(signalType -> rMapReactive.expire(Duration.ofHours(1L)).subscribe());

	}
}
