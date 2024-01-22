package com.item.inventory.service.management.impll;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.item.inventory.entity.ItemInventoryEntity;
import com.item.inventory.mapper.ItemInventoryMapper;
import com.item.inventory.model.request.ItemInventoryDTORequest;
import com.item.inventory.model.response.ResponseDTO;
import com.item.inventory.service.ProcessItemInventoryEntity;
import com.item.inventory.service.cache.ItemInventoryCacheService;
import com.item.inventory.service.management.CacheManagementService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CacheManagementServiceImpl extends ProcessItemInventoryEntity implements CacheManagementService {

	private ItemInventoryCacheService itemInventoryCacheService;

	public CacheManagementServiceImpl(ItemInventoryCacheService itemInventoryCacheService) {
		this.itemInventoryCacheService = itemInventoryCacheService;
	}

	@Override
	public Mono<ResponseEntity<ResponseDTO>> addNewItemInventoryToCacheManagement(
			ItemInventoryDTORequest newItemInventoryDTOrequest) {
		log.debug("addNewItemInventoryToCacheManagement - newItemInventoryDTOrequest=", newItemInventoryDTOrequest);

		ItemInventoryEntity itemInventoryEntity = ItemInventoryMapper
				.mapItemInventoryDTOrequestToItemInventoryEntity(newItemInventoryDTOrequest);

		Mono<ItemInventoryEntity> itemInventoryEntityMono = itemInventoryCacheService
				.addNewItemInventoryToCache(itemInventoryEntity);

		return processItemInventoryEntityMono(itemInventoryEntityMono, HttpStatus.CREATED);
	}

	@Override
	public Mono<ResponseEntity<ResponseDTO>> getAllItemInventoriesFromCacheManagement() {
		log.debug("getAllItemInventoriesFromCacheManagement");

		Flux<ItemInventoryEntity> itemInventoryEntityFlux = itemInventoryCacheService.getAllItemInventoriesFromCache();

		return processItemInventoryEntityFlux(itemInventoryEntityFlux);
	}

	@Override
	public Mono<ResponseEntity<ResponseDTO>> getItemInventoryByIdFromCacheManagement(String id) {
		log.debug("getItemInventoryByIdFromCacheManagement - id=", id);

		Mono<ItemInventoryEntity> itemInventoryEntityMono = itemInventoryCacheService.getItemInventoryByIdFromCache(id);

		return processItemInventoryEntityMono(itemInventoryEntityMono, HttpStatus.OK)
				.switchIfEmpty(Mono.just(ItemInventoryMapper.generateItemNotFoundResponse()));
	}

	@Override
	public Mono<ResponseEntity<ResponseDTO>> rebuildItemInventoryCacheFromDBManagement() {
		log.debug("rebuildItemInventoryCacheFromDBManagement");

		Flux<ItemInventoryEntity> itemInventoryEntityFlux = itemInventoryCacheService.rebuildItemInventoryCacheFromDB();

		return processItemInventoryEntityFlux(itemInventoryEntityFlux);
	}

	@Override
	public Mono<Void> deleteItemInventoryCacheManagement() {
		log.debug("deleteItemInventoryCacheManagement");

		itemInventoryCacheService.deleteItemInventoryCache().subscribe();

		return Mono.empty();
	}

	@Override
	public Mono<Void> deleteItemInventoryByIdFromCacheManagement(String id) {
		log.debug("deleteItemInventoryByIdFromCacheManagement - id=", id);

		itemInventoryCacheService.deleteItemInventoryByIdFromCache(id).subscribe();

		return Mono.empty();
	}
}
