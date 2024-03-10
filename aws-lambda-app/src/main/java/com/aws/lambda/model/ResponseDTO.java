package com.aws.lambda.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDTO {
	private String message;
	private String functionName;
	private int memoryLimit;
}
