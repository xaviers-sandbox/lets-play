package com.item.review.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
@JsonInclude(Include.NON_NULL)
public class ItemReviewDTOResponse extends ResponseDTO {
	private Integer resultSetSize;
	private List<ItemReviewDTO> itemReviewDTOList;

}