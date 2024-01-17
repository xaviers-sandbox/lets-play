package com.online.store.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.item.inventory.model.response.ItemInventoryDTOResponse;
import com.item.review.model.response.ItemReviewDTOResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(Include.NON_NULL)
@Builder
public class OnlineStoreDTOResponse extends ResponseDTO {
	private ItemReviewDTOResponse itemReviewDTOResponse;
	private ItemInventoryDTOResponse itemInventoryDTOResponse;
}
