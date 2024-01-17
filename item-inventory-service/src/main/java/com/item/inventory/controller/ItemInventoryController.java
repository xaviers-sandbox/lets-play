package com.item.inventory.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.item.inventory.model.request.ItemInventoryDTORequest;
import com.item.inventory.service.ItemInventoryService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("v1/item-inventories")
public class ItemInventoryController {

	private ItemInventoryService itemInventoryService;

	public ItemInventoryController(ItemInventoryService itemInventoryService) {
		this.itemInventoryService = itemInventoryService;
	}

	@PostMapping
	public Mono<ResponseEntity<?>> addNewItemInventory(
			@RequestBody @Valid ItemInventoryDTORequest newItemInventoryDTOrequest) {
		return itemInventoryService.addNewItemInventory(newItemInventoryDTOrequest);
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<?>> getItemInventoryById(@PathVariable String id) {
		return itemInventoryService.getItemInventoryById(id);
	}

	@GetMapping
	public Mono<ResponseEntity<?>> getAllItemInventories() {
		return itemInventoryService.getAllItemInventories();
	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<?>> updateItemInventoryById(@PathVariable String id,
			@RequestBody @Valid ItemInventoryDTORequest updatedItemInventoryDTOrequest) {
		return itemInventoryService.updateItemInventoryById(id, updatedItemInventoryDTOrequest);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteItemInventoryById(@PathVariable String id) {
		return itemInventoryService.deleteItemInventoryById(id);
	}

	@DeleteMapping("/delete/all")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteAllItemInventories() {
		return itemInventoryService.deleteAllItemInventories();
	}

	@PostMapping("/init-test-data/{size}")
	public Mono<ResponseEntity<?>> initTestDataBySize(@PathVariable String size) {
		return itemInventoryService.initTestDataBySize(size);
	}
}
