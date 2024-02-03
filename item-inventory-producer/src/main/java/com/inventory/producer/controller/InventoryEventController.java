package com.inventory.producer.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.producer.mapper.InventoryEventMapper;
import com.inventory.producer.model.InventoryItemDTORequest;
import com.inventory.producer.model.ResponseDTO;
import com.inventory.producer.service.InventoryEventService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("v1/inventory-events/app")
@Slf4j
public class InventoryEventController {

	private InventoryEventService inventoryEventService;

	public InventoryEventController(InventoryEventService inventoryEventService) {
		this.inventoryEventService = inventoryEventService;
	}

	@PostMapping("/init-test-data/{size}")
	public ResponseEntity<ResponseDTO> addNewMockItems(@PathVariable String size) {

		log.debug("postInventories size={}", size);

		if (!StringUtils.isNumeric(size))
			return InventoryEventMapper.buildBadPathVariableResponse(size);

		return inventoryEventService.addNewMockItems(Integer.valueOf(size));
	}

	@PostMapping("/create")
	public ResponseEntity<ResponseDTO> addNewItemInventory(
			@RequestBody @Valid InventoryItemDTORequest newInventoryItemDTORequest) {

		log.debug("addNewItemInventory newInventoryItemDTORequest={}", newInventoryItemDTORequest);

		return inventoryEventService.addNewItemInventory(newInventoryItemDTORequest);
	}

	@PutMapping("/update/{eventId}")
	public ResponseEntity<ResponseDTO> updateItemInventory(@PathVariable String eventId,
			@RequestBody @Valid InventoryItemDTORequest updatedInventoryItemDTORequest) {

		log.debug("updateItemInventory eventId={} updatedInventoryItemDTORequest={}",
				eventId,
				updatedInventoryItemDTORequest);

		if (!StringUtils.isNumeric(eventId))
			return InventoryEventMapper.buildBadPathVariableResponse(eventId);

		return inventoryEventService.updateItemInventory(Integer.valueOf(eventId), updatedInventoryItemDTORequest);
	}
}
