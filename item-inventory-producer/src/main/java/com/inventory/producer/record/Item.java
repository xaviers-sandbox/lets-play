package com.inventory.producer.record;

import lombok.Builder;

@Builder
public record Item(
		String itemId,
		String name,
		Double price,
		Integer quantity) {
}
