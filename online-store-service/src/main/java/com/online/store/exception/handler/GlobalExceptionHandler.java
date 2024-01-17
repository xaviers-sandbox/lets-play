package com.online.store.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.item.inventory.model.response.ErrorDTOResponse;
import com.online.store.exception.ExternalClient400Exception;
import com.online.store.exception.ExternalClient500Exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ExternalClient400Exception.class)
	public ResponseEntity<ErrorDTOResponse> handleExternalClient400Exception(ExternalClient400Exception ex) {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		log.error("classMethod={} - GlobalExceptionHandler Caught Exception ex.getErrorMessage()={}",
				methodName,
				ex.getErrorMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(ErrorDTOResponse.builder()
						.errorCode(HttpStatus.NOT_FOUND.value())
						.errorMessage(ex.getErrorMessage())
						.status(HttpStatus.NOT_FOUND.name())
						.build());
	}

	@ExceptionHandler(ExternalClient500Exception.class)
	public ResponseEntity<ErrorDTOResponse> handleExternalClient500Exception(ExternalClient500Exception ex) {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		log.error("classMethod={} - GlobalExceptionHandler Caught Exception ex.getErrorMessage()={}",
				methodName,
				ex.getErrorMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ErrorDTOResponse.builder()
						.errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
						.errorMessage(ex.getErrorMessage())
						.status(HttpStatus.INTERNAL_SERVER_ERROR.name())
						.build());
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorDTOResponse> handleRuntimeException(RuntimeException ex) {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		log.error("classMethod={} - GlobalExceptionHandler Caught Exception ex.getLocalizedMessage()={}",
				methodName,
				ex.getLocalizedMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ErrorDTOResponse.builder()
						.errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
						.errorMessage(ex.getLocalizedMessage())
						.status(HttpStatus.INTERNAL_SERVER_ERROR.name())
						.build());
	}
}
