package com.inventory.producer.model;

import com.inventory.producer.record.InventoryEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@JsonInclude(Include.NON_NULL)
public class ItemEventsResponseDTO {
	private InventoryEvent inventoryEvent;

}
