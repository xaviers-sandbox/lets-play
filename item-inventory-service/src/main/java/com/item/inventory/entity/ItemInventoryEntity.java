package com.item.inventory.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("ItemInventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemInventoryEntity {
	@Id
	private String id;
	private Double price;
	private String name;
	private Integer quantity;
}
