package com.item.inventory.controller.management;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.item.inventory.model.response.ResponseDTO;
import com.item.inventory.service.management.DatabaseManagementService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("v1/item-inventories/dbs")
public class DatabaseManagementController {
	private DatabaseManagementService databaseManagementService;

	public DatabaseManagementController(DatabaseManagementService databaseManagementService) {
		this.databaseManagementService = databaseManagementService;
	}

	@GetMapping
	public Mono<ResponseEntity<ResponseDTO>> getAllItemInventoriesFromDBManagement() {
		return databaseManagementService.getAllItemInventoriesFromDBManagement();
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<ResponseDTO>> getItemInventoryByIdFromDBManagement(@PathVariable String id) {
		return databaseManagementService.getItemInventoryByIdFromDBManagement(id);
	}
}
