package com.item.inventory.controller.management;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.item.inventory.model.request.ItemInventoryDTORequest;
import com.item.inventory.model.response.ResponseDTO;
import com.item.inventory.service.management.CacheManagementService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("v1/item-inventories/caches")
public class CacheManagementController {

	private CacheManagementService cacheManagementService;

	public CacheManagementController(CacheManagementService cacheManagementService) {
		this.cacheManagementService = cacheManagementService;
	}

	@PostMapping
	public Mono<ResponseEntity<ResponseDTO>> addNewItemInventoryToCacheManagement(
			@RequestBody @Valid ItemInventoryDTORequest newItemInventoryDTOrequest) {
		return cacheManagementService.addNewItemInventoryToCacheManagement(newItemInventoryDTOrequest);
	}

	@GetMapping
	public Mono<ResponseEntity<ResponseDTO>> getAllItemInventoriesFromCacheManagement() {
		return cacheManagementService.getAllItemInventoriesFromCacheManagement();
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<ResponseDTO>> getItemInventoryByIdFromCacheManagement(@PathVariable String id) {
		return cacheManagementService.getItemInventoryByIdFromCacheManagement(id);
	}

	@PostMapping("/rebuild")
	public Mono<ResponseEntity<ResponseDTO>> rebuildItemInventoryCacheFromDBManagement() {
		return cacheManagementService.rebuildItemInventoryCacheFromDBManagement();
	}

	@DeleteMapping("/delete/all")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteItemInventoryCacheManagement() {
		return cacheManagementService.deleteItemInventoryCacheManagement();
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteItemInventoryByIdFromCacheManagement(@PathVariable String id) {
		return cacheManagementService.deleteItemInventoryByIdFromCacheManagement(id);
	}
}
