package com.item.inventory.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class DeleteDTOResponse extends ResponseDTO {

	private String message;

	@Builder
	public DeleteDTOResponse(String message) {
		this.message = message;
	}
}
