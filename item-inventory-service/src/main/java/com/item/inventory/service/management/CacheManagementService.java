package com.item.inventory.service.management;

import org.springframework.http.ResponseEntity;

import com.item.inventory.model.request.ItemInventoryDTORequest;
import com.item.inventory.model.response.ResponseDTO;

import reactor.core.publisher.Mono;

public interface CacheManagementService {

	Mono<ResponseEntity<ResponseDTO>> addNewItemInventoryToCacheManagement(
			ItemInventoryDTORequest newItemInventoryDTOrequest);

	Mono<ResponseEntity<ResponseDTO>> getAllItemInventoriesFromCacheManagement();

	Mono<ResponseEntity<ResponseDTO>> getItemInventoryByIdFromCacheManagement(String id);

	Mono<ResponseEntity<ResponseDTO>> rebuildItemInventoryCacheFromDBManagement();

	Mono<Void> deleteItemInventoryCacheManagement();

	Mono<Void> deleteItemInventoryByIdFromCacheManagement(String id);

}
