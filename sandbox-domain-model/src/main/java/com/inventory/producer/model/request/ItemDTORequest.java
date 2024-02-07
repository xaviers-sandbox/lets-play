package com.inventory.producer.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTORequest {
	
	private String itemId;
	
	@NotBlank(message = "Name Cannot Be Empty or Null")
	private String name;
	
	@NotNull(message = "Price Cannot Be Empty or Null")
	@Positive(message = "Price Cannot Be Negative")
	private Double price;
	
	@NotNull(message = "Quantity Cannot Be Empty or Null")
	@Positive(message = "Quantity Cannot Be Negative")
	private Integer quantity;
}
