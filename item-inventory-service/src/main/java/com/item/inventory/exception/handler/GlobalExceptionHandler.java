package com.item.inventory.exception.handler;

import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.item.inventory.model.response.ErrorDTOResponse;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(WebExchangeBindException.class)
	public ResponseEntity<ErrorDTOResponse> handleRequestError(WebExchangeBindException ex) {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		log.error("classMethod={} - GlobalExceptionHandler Caught Exception reason={}", methodName, ex.getReason());

		String errorMessages = ex.getBindingResult()
				.getAllErrors()
				.stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage)
				.sorted()
				.collect(Collectors.joining(", "));

		log.error("classMethod={} - errors={}", methodName, errorMessages);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ErrorDTOResponse.builder()
						.errorCode(HttpStatus.BAD_REQUEST.value())
						.errorMessage(errorMessages)
						.status(HttpStatus.BAD_REQUEST.name())
						.build());
	}
}
