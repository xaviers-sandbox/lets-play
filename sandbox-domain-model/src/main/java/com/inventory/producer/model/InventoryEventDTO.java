package com.inventory.producer.model;

import com.inventory.producer.enums.InventoryEventType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEventDTO {
	Integer eventId;
	InventoryEventType eventType;
	ItemDTO item;
}
