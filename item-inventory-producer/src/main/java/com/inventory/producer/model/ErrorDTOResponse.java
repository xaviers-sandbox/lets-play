package com.inventory.producer.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class ErrorDTOResponse extends ResponseDTO {
	private int errorCode;
	private String errorMessage;
	private String status;
}
