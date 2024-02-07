package com.inventory.producer.exception.handler;

import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.inventory.producer.model.response.ErrorDTOResponse;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDTOResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
//		String methodName = new Object() {
//		}.getClass().getEnclosingMethod().getName();

		String errorMessages = ex.getBindingResult()
				.getAllErrors()
				.stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage)
				.sorted()
				.collect(Collectors.joining(", "));

		log.error("handleMethodArgumentNotValidException - MethodArgumentNotValidException Caught - errors={}",
				errorMessages);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ErrorDTOResponse.builder()
						.errorCode(HttpStatus.BAD_REQUEST.value())
						.errorMessage(errorMessages)
						.status(HttpStatus.BAD_REQUEST.name())
						.build());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorDTOResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
//		String methodName = new Object() {
//		}.getClass().getEnclosingMethod().getName();

		String errorMessage = "Invalid Request Body";

		log.error("handleHttpMessageNotReadableException - HttpMessageNotReadableException Caught - error={}",
				errorMessage);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ErrorDTOResponse.builder()
						.errorCode(HttpStatus.BAD_REQUEST.value())
						.errorMessage(errorMessage)
						.status(HttpStatus.BAD_REQUEST.name())
						.build());
	}
}
