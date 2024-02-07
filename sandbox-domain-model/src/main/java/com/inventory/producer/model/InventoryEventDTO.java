package com.inventory.producer.model;

import com.inventory.producer.enums.InventoryEventType;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class InventoryEventDTO {
	String eventId;
	InventoryEventType eventType;
	ItemDTO itemDTO;
}
