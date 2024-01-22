package com.item.inventory.service.impl;

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
import com.item.inventory.model.response.ErrorDTOResponse;
import com.item.inventory.model.response.ItemInventoryDTOResponse;
import com.item.inventory.model.response.ResponseDTO;
import com.item.inventory.repository.ItemInventoryRepository;
import com.item.inventory.service.ItemInventoryService;
import com.item.inventory.service.cache.ItemInventoryCacheService;
import com.item.inventory.util.ItemInventoryUtil;
import com.item.inventory.validator.ItemInventoryValidator;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ItemInventoryServiceImpl implements ItemInventoryService {
	private ItemInventoryValidator itemInventoryValidator;

	private ItemInventoryRepository itemInventoryRepository;

	private ItemInventoryCacheService itemInventoryCacheService;

	public ItemInventoryServiceImpl(ItemInventoryValidator itemInventoryValidator,
			ItemInventoryRepository itemInventoryRepository, ItemInventoryCacheService itemInventoryCacheService) {

		this.itemInventoryValidator = itemInventoryValidator;
		this.itemInventoryRepository = itemInventoryRepository;
		this.itemInventoryCacheService = itemInventoryCacheService;
	}

	// this needs to update the cache by 1 after the save
	// also add update the entire cache
	public Mono<ResponseEntity<ResponseDTO>> addNewItemInventory(ItemInventoryDTORequest newItemInventoryDTOrequest) {
		log.debug("addNewItemInventory - newItemInventoryDTOrequest=", newItemInventoryDTOrequest);
		ItemInventoryEntity itemInventoryEntity = ItemInventoryMapper
				.mapItemInventoryDTOrequestToItemInventoryEntity(newItemInventoryDTOrequest);

		Mono<ItemInventoryEntity> itemInventoryEntityMono = itemInventoryRepository.save(itemInventoryEntity);

		itemInventoryCacheService.addNewItemInventoryToCache(itemInventoryEntity).subscribe();

		return processItemInventoryEntityMono(itemInventoryEntityMono, HttpStatus.CREATED);
	}

	public Mono<ResponseEntity<ResponseDTO>> getAllItemInventories() {
		log.debug("getAllItemInventories");

		Flux<ItemInventoryEntity> itemInventoryEntityFlux = itemInventoryCacheService.getAllItemInventoriesFromCache();

		return processItemInventoryEntityFlux(itemInventoryEntityFlux);

	}

	public Mono<ResponseEntity<ResponseDTO>> getItemInventoryById(String id) {
		log.debug("getItemInventoryById - id=", id);

		Mono<ItemInventoryEntity> itemInventoryEntityMono = itemInventoryCacheService.getItemInventoryByIdFromCache(id);

		return processItemInventoryEntityMono(itemInventoryEntityMono, HttpStatus.OK)
				.switchIfEmpty(Mono.just(ItemInventoryMapper.generateItemNotFoundResponse()));
	}

	public Mono<ResponseEntity<ResponseDTO>> updateItemInventoryById(String id,
			ItemInventoryDTORequest updatedItemInventoryDTOrequest) {
		log.debug("updateItemInventoryById - id={} newItemInventoryDTOrequest=", id, updatedItemInventoryDTOrequest);

		Mono<ItemInventoryEntity> itemInventoryEntityMono = itemInventoryRepository.findById(id);

		return itemInventoryEntityMono.flatMap(origItemInventoryEntity -> {

			ItemInventoryEntity updatedItemInventoryEntity = ItemInventoryMapper
					.buildUpdatedItemInventoryEntityWithOrigEntityAndNewDTO(origItemInventoryEntity,
							updatedItemInventoryDTOrequest);

			Mono<ItemInventoryEntity> updatedItemInventoryEntityMono = itemInventoryRepository
					.save(updatedItemInventoryEntity);

			itemInventoryCacheService.deleteItemInventoryByIdFromCache(id).subscribe();

			itemInventoryCacheService.addNewItemInventoryToCache(updatedItemInventoryEntity);

			return processItemInventoryEntityMono(updatedItemInventoryEntityMono, HttpStatus.OK);
		}).switchIfEmpty(Mono.just(ItemInventoryMapper.generateItemNotFoundResponse()));
	}

	public Mono<Void> deleteItemInventoryById(String id) {
		log.debug("deleteItemInventoryById - id=", id);

		itemInventoryRepository.deleteById(id).subscribe();

		itemInventoryCacheService.deleteItemInventoryByIdFromCache(id).subscribe();

		return Mono.empty();
	}

	public Mono<Void> deleteAllItemInventories() {
		log.debug("deleteAllItemInventories");

		itemInventoryRepository.deleteAll().subscribe();

		itemInventoryCacheService.deleteItemInventoryCache().subscribe();

		return Mono.empty();
	}

	// this uses streams. the others to streams
	public Mono<ResponseEntity<ResponseDTO>> processItemInventoryEntityMono(
			Mono<ItemInventoryEntity> itemInventoryEntityMono, HttpStatus httpStatus) {
		log.debug("processItemInventoryEntityMono");

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
		log.debug("processItemInventoryEntityFlux");

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

	@Override
	public Mono<ResponseEntity<ResponseDTO>> initTestDataBySize(String testDataSize) {
		log.debug("initTestDataBySize - testDataSize={}", testDataSize);
		int initTestDataSize = itemInventoryValidator.checkInitTestDataSize(testDataSize);

		return 0 < initTestDataSize ? buildTestDataBySize(initTestDataSize)
				: buildBadRequestResponseEntity("initTestDataSize has to be 1 or greater testDataSize=" + testDataSize);
	}

	@Override
	public Mono<ResponseEntity<ResponseDTO>> buildTestDataBySize(int testDataSize) {

		List<ItemInventoryEntity> itemInventoryEntityListMock = ItemInventoryUtil
				.generateItemInventoryEntityList(testDataSize);

		Flux<ItemInventoryEntity> itemInventoryEntityFlux = itemInventoryRepository
				.saveAll(itemInventoryEntityListMock);

		return processItemInventoryEntityFlux(itemInventoryEntityFlux);
	}

	@Override
	public Mono<ResponseEntity<ResponseDTO>> buildBadRequestResponseEntity(String errorMessage) {
		log.debug("buildBadRequestResponseEntity errorMessages={}", errorMessage);

		ErrorDTOResponse errorDTOResponse = ItemInventoryMapper.buildErrorDTOResponse(errorMessage,
				HttpStatus.BAD_REQUEST);

		ResponseEntity<ResponseDTO> responseEntity = ItemInventoryMapper
				.buildResponseEntityWithDTOResponse(errorDTOResponse, HttpStatus.BAD_REQUEST);

		return Mono.just(responseEntity);
	}
}