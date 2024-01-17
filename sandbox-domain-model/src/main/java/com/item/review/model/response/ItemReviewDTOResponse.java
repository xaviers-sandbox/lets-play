package com.item.review.model.response;

import java.util.List;

import com.item.review.model.ItemReviewDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class ItemReviewDTOResponse extends ResponseDTO {
	private Integer resultSetSize;
	private List<ItemReviewDTO> itemReviewDTOList;

}