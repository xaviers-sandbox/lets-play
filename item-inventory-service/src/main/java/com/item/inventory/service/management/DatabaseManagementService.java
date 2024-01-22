package com.item.inventory.service.management;

import org.springframework.http.ResponseEntity;

import com.item.inventory.model.response.ResponseDTO;

import reactor.core.publisher.Mono;

public interface DatabaseManagementService {
	Mono<ResponseEntity<ResponseDTO>> getAllItemInventoriesFromDBManagement();

	Mono<ResponseEntity<ResponseDTO>> getItemInventoryByIdFromDBManagement(String id);
}
