package com.inventory.consumer.records;

import lombok.Builder;

@Builder
public record ItemRecord(
		String itemId,
		String name,
		Double price,
		Integer quantity) {
}
