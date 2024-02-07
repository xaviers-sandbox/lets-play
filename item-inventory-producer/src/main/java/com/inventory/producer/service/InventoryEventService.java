package com.inventory.producer.service;

import org.springframework.http.ResponseEntity;

import com.inventory.producer.model.InventoryEventDTORequest;
import com.inventory.producer.model.ResponseDTO;

public interface InventoryEventService{

	ResponseEntity<ResponseDTO> addNewMockItems(Integer size);

	ResponseEntity<ResponseDTO> addNewItemInventory(InventoryEventDTORequest newInventoryEventDTORequest);

	ResponseEntity<ResponseDTO> updateItemInventory(String eventId, InventoryEventDTORequest updatedInventoryEventDTORequest);
}
