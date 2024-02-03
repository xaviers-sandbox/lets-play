package com.inventory.producer.service;

import org.springframework.http.ResponseEntity;

import com.inventory.producer.model.InventoryItemDTORequest;
import com.inventory.producer.model.ResponseDTO;

public interface InventoryEventService{

	ResponseEntity<ResponseDTO> addNewMockItems(Integer size);

	ResponseEntity<ResponseDTO> addNewItemInventory(InventoryItemDTORequest newInventoryItemDTORequest);

	ResponseEntity<ResponseDTO> updateItemInventory(Integer eventId, InventoryItemDTORequest updatedInventoryItemDTORequest);
}
