package com.inventory.producer.model;

import com.inventory.producer.enums.InventoryEventType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryEventDTO {
	String eventId;
	InventoryEventType eventType;
	ItemDTO itemDTO;
}
