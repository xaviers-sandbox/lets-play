package com.inventory.producer.model.response;

import java.util.List;

import com.inventory.producer.model.InventoryEventDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@JsonInclude(Include.NON_NULL)
@EqualsAndHashCode(callSuper=false)
public class InventoryEventDTOResponse extends ResponseDTO {
	private Integer resultSetSize;
	private List<InventoryEventDTO> itemInventoryDTOList;
}
