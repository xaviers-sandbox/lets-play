package com.inventory.producer.record;

import lombok.Builder;

@Builder
public record ItemRecord(
		String itemId,
		String name,
		Double price,
		Integer quantity) {
}
