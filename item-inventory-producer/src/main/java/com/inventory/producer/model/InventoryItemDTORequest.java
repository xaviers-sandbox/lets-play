package com.inventory.producer.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class InventoryItemDTORequest {
	Integer id;
	
	@NotBlank(message = "Name Cannot Be Empty or Null")
	String name;
	
	@NotNull(message = "Price Cannot Be Empty or Null")
	@Positive(message = "Price Cannot Be Negative")
	Double price;
	
	@NotNull(message = "Quantity Cannot Be Empty or Null")
	@Positive(message = "Quantity Cannot Be Negative")
	Integer quantity;
}
