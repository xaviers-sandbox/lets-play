package com.online.store.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ExternalClient400Exception extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String errorMessage;

	public ExternalClient400Exception(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
}
