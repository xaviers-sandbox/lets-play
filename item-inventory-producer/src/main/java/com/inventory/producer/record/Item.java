package com.inventory.producer.record;

import lombok.Builder;


@Builder
//@JsonInclude(Include.NON_NULL)
public record Item(
		Integer id,
		String name,
		Double price,
		Integer quantity
		) {
}
