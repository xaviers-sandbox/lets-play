package com.item.inventory.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTOResponse extends ResponseDTO {
	private int errorCode;
	private String errorMessage;
	private String status;
}
