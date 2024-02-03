package com.inventory.producer.record;

import lombok.Builder;

@Builder
public record Item(
		Integer id,
		String name,
		Double price,
		Integer quantity) {
}
