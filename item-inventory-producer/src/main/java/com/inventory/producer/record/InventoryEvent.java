package com.inventory.producer.record;

import com.inventory.producer.enums.InventoryEventType;

import lombok.Builder;


@Builder
public record InventoryEvent(
		Integer eventId,
		InventoryEventType eventType,
		Item item) {
}
