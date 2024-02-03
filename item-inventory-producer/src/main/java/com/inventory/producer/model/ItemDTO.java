package com.inventory.producer.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDTO {
	private Integer id;
	private String name;
	private Double price;
	private Integer quantity;
}
