package com.item.inventory.service.management.impll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.item.inventory.entity.ItemInventoryEntity;
import com.item.inventory.mapper.ItemInventoryMapper;
import com.item.inventory.model.ItemInventoryDTO;
import com.item.inventory.model.request.ItemInventoryDTORequest;
import com.item.inventory.model.response.ItemInventoryDTOResponse;
import com.item.inventory.model.response.ResponseDTO;
import com.item.inventory.service.cache.ItemInventoryCacheService;
import com.item.inventory.service.management.CacheManagementService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CacheManagementServiceImpl implements CacheManagementService {

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

	// this uses streams. the others to streams
	public Mono<ResponseEntity<ResponseDTO>> processItemInventoryEntityMono(
			Mono<ItemInventoryEntity> itemInventoryEntityMono, HttpStatus httpStatus) {
		log.debug("CacheManagementServiceImpl.processItemInventoryEntityMono");

		return itemInventoryEntityMono.flatMap(returnedItemInventoryEntity -> {
			ResponseEntity<ResponseDTO> responseEntity = Stream
					.of(ItemInventoryMapper.mapItemInventoryEntityToItemInventoryDTO(returnedItemInventoryEntity))
					.map(itemInventoryDTO -> {
						List<ItemInventoryDTO> itemInventoryDTOList = new ArrayList<>(Arrays.asList(itemInventoryDTO));
						return ItemInventoryMapper.buildItemInventoryDTOResponse(itemInventoryDTOList);
					})
					.map(itemInventoryDTOResponse -> ItemInventoryMapper
							.buildResponseEntityWithDTOResponse(itemInventoryDTOResponse, httpStatus))
					.findAny()
					.get();

			return Mono.just(responseEntity);
		});
	}

	public Mono<ResponseEntity<ResponseDTO>> processItemInventoryEntityFlux(
			Flux<ItemInventoryEntity> itemInventoryEntityFlux) {
		log.debug("CacheManagementServiceImpl.processItemInventoryEntityFlux");

		return itemInventoryEntityFlux.collectList().flatMap(itemInventoryEntityList -> {

			List<ItemInventoryDTO> itemInventoryDTOList = ItemInventoryMapper
					.mapItemInventoryEntityListToItemInventoryDTOList(itemInventoryEntityList);

			ItemInventoryDTOResponse itemInventoryDTOResponse = ItemInventoryMapper
					.buildItemInventoryDTOResponse(itemInventoryDTOList);

			ResponseEntity<ResponseDTO> responseEntity = ItemInventoryMapper
					.buildResponseEntityWithDTOResponse(itemInventoryDTOResponse, HttpStatus.OK);

			return Mono.just(responseEntity);
		});
	}

}
