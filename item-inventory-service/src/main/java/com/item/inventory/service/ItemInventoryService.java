package com.item.inventory.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.item.inventory.entity.ItemInventoryEntity;
import com.item.inventory.model.request.ItemInventoryDTORequest;
import com.item.inventory.model.response.ResponseDTO;

import reactor.core.publisher.Mono;

public interface ItemInventoryService {
	Mono<ResponseEntity<ResponseDTO>> addNewItemInventory(ItemInventoryDTORequest newItemInventoryDTOrequest);

	Mono<ResponseEntity<ResponseDTO>> getItemInventoryById(String id);

	Mono<ResponseEntity<ResponseDTO>> getAllItemInventories();

	Mono<ResponseEntity<ResponseDTO>> updateItemInventoryById(String id,
			ItemInventoryDTORequest updatedItemInventoryDTOrequest);

	Mono<Void> deleteItemInventoryById(String id);

	Mono<Void> deleteAllItemInventories();

	Mono<ResponseEntity<ResponseDTO>> processItemInventoryEntityMono(Mono<ItemInventoryEntity> itemInventoryEntityMono,
			HttpStatus httpStatus);

	Mono<ResponseEntity<ResponseDTO>> initTestDataBySize(String testDataSize);

	Mono<ResponseEntity<ResponseDTO>> buildTestDataBySize(int testDataSize);

	Mono<ResponseEntity<ResponseDTO>> buildBadRequestResponseEntity(String errorMessage);
}
