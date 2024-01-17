package com.item.inventory.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
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
	@NotBlank
	private Double price;
	@NotBlank
	private String name;
	@NotBlank
	private Integer quantity;
}
