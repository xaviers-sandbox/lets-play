package com.inventory.producer.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper=false)
public class ErrorDTOResponse extends ResponseDTO {
	private int errorCode;
	private String errorMessage;
	private String status;
}
