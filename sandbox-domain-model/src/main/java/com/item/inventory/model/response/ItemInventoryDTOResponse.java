package com.item.inventory.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.item.inventory.model.ItemInventoryDTO;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(Include.NON_NULL)
public class ItemInventoryDTOResponse extends ResponseDTO {

	private Integer resultSetSize;
	private List<ItemInventoryDTO> itemInventoryDTOList;

	@Builder
	public ItemInventoryDTOResponse(Integer resultSetSize, List<ItemInventoryDTO> itemInventoryDTOList) {
		this.resultSetSize = resultSetSize;
		this.itemInventoryDTOList = itemInventoryDTOList;
	}
}
