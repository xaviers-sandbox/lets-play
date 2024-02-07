package com.inventory.consumer.records;

import com.inventory.producer.enums.InventoryEventType;

import lombok.Builder;

@Builder
public record InventoryEventRecord(
		String eventId,
		InventoryEventType eventType,
		ItemRecord item) {
}
