package com.item.inventory.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.item.inventory.entity.ItemInventoryEntity;
import com.item.inventory.model.request.ItemInventoryDTORequest;

import reactor.core.publisher.Mono;

public interface ItemInventoryService {
	Mono<ResponseEntity<?>> addNewItemInventory(ItemInventoryDTORequest newItemInventoryDTOrequest);

	Mono<ResponseEntity<?>> getItemInventoryById(String id);

	Mono<ResponseEntity<?>> getAllItemInventories();

	Mono<ResponseEntity<?>> updateItemInventoryById(String id, ItemInventoryDTORequest updatedItemInventoryDTOrequest);

	Mono<Void> deleteItemInventoryById(String id);

	Mono<Void> deleteAllItemInventories();

	Mono<ResponseEntity<?>> processItemInventoryEntityMono(Mono<ItemInventoryEntity> itemInventoryEntityMono,
			HttpStatus httpStatus);

	Mono<ResponseEntity<?>> initTestDataBySize(String testDataSize);

	Mono<ResponseEntity<?>> buildTestDataBySize(int testDataSize);

	Mono<ResponseEntity<?>> buildBadRequestResponseEntity(String errorMessage);
}
