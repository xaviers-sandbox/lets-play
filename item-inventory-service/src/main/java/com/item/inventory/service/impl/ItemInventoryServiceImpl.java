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
import com.item.inventory.util.ItemInventoryUtil;
import com.item.inventory.validator.ItemInventoryValidator;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ItemInventoryServiceImpl implements ItemInventoryService {
	private ItemInventoryRepository itemInventoryRepo;
	private ItemInventoryValidator itemInventoryValidator;

	public ItemInventoryServiceImpl(ItemInventoryRepository itemInventoryRepo,
			ItemInventoryValidator itemInventoryValidator) {
		this.itemInventoryRepo = itemInventoryRepo;
		this.itemInventoryValidator = itemInventoryValidator;
	}

	public Mono<ResponseEntity<ResponseDTO>> addNewItemInventory(ItemInventoryDTORequest newItemInventoryDTOrequest) {

		ItemInventoryEntity itemInventoryEntity = ItemInventoryMapper
				.mapItemInventoryDTOrequestToItemInventoryEntity(newItemInventoryDTOrequest);

		Mono<ItemInventoryEntity> itemInventoryEntityMono = itemInventoryRepo.save(itemInventoryEntity);

		return processItemInventoryEntityMono(itemInventoryEntityMono, HttpStatus.CREATED);
	}

	public Mono<ResponseEntity<ResponseDTO>> getItemInventoryById(String id) {
		Mono<ItemInventoryEntity> itemInventoryEntityMono = itemInventoryRepo.findById(id);

		return processItemInventoryEntityMono(itemInventoryEntityMono, HttpStatus.OK)
				.switchIfEmpty(Mono.just(ItemInventoryMapper.generateItemNotFoundResponse()));
	}

	public Mono<ResponseEntity<ResponseDTO>> getAllItemInventories() {
		Flux<ItemInventoryEntity> itemInventoryEntityFlux = itemInventoryRepo.findAllByOrderByNameAsc();

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

	public Mono<ResponseEntity<ResponseDTO>> updateItemInventoryById(String id,
			ItemInventoryDTORequest updatedItemInventoryDTOrequest) {
		Mono<ItemInventoryEntity> itemInventoryEntityMono = itemInventoryRepo.findById(id);

		return itemInventoryEntityMono.flatMap(origItemInventoryEntity -> {

			ItemInventoryEntity updatedItemInventoryEntity = ItemInventoryMapper
					.buildUpdatedItemInventoryEntityWithOrigEntityAndNewDTO(origItemInventoryEntity,
							updatedItemInventoryDTOrequest);

			Mono<ItemInventoryEntity> updatedItemInventoryEntityMono = itemInventoryRepo
					.save(updatedItemInventoryEntity);

			return processItemInventoryEntityMono(updatedItemInventoryEntityMono, HttpStatus.OK);
		}).switchIfEmpty(Mono.just(ItemInventoryMapper.generateItemNotFoundResponse()));
	}

	public Mono<Void> deleteItemInventoryById(String id) {
		return itemInventoryRepo.deleteById(id);
	}

	public Mono<Void> deleteAllItemInventories() {
		return itemInventoryRepo.deleteAll();
	}

	// this uses streams. update the others to streams
	public Mono<ResponseEntity<ResponseDTO>> processItemInventoryEntityMono(
			Mono<ItemInventoryEntity> itemInventoryEntityMono, HttpStatus httpStatus) {

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

	@Override
	public Mono<ResponseEntity<ResponseDTO>> initTestDataBySize(String testDataSize) {
		int initTestDataSize = itemInventoryValidator.checkInitTestDataSize(testDataSize);

		return 0 < initTestDataSize ? buildTestDataBySize(initTestDataSize)
				: buildBadRequestResponseEntity("initTestDataSize has to be 1 or greater testDataSize=" + testDataSize);
	}

	@Override
	public Mono<ResponseEntity<ResponseDTO>> buildTestDataBySize(int testDataSize) {

		List<ItemInventoryEntity> itemInventoryEntityListMock = ItemInventoryUtil
				.generateItemInventoryEntityList(testDataSize);

		Flux<ItemInventoryEntity> itemInventoryEntityFlux = itemInventoryRepo.saveAll(itemInventoryEntityListMock);

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
	public Mono<ResponseEntity<ResponseDTO>> buildBadRequestResponseEntity(String errorMessage) {
		log.debug("buildBadRequestResponseEntity errorMessages={}", errorMessage);

		ErrorDTOResponse errorDTOResponse = ItemInventoryMapper.buildErrorDTOResponse(errorMessage,
				HttpStatus.BAD_REQUEST);

		ResponseEntity<ResponseDTO> responseEntity = ItemInventoryMapper
				.buildResponseEntityWithDTOResponse(errorDTOResponse, HttpStatus.BAD_REQUEST);

		return Mono.just(responseEntity);
	}
}