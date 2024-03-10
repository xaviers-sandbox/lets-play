package com.aws.lambda.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.aws.lambda.model.RequestDTO;
import com.aws.lambda.model.ResponseDTO;

public class HelloHandler implements RequestHandler<RequestDTO, ResponseDTO> {

	@Override
	public ResponseDTO handleRequest(RequestDTO requestDTO, Context context) {
		context.getLogger().log(requestDTO.getName() + " is speaking");

		return ResponseDTO.builder()
				.message("Hello " + requestDTO.getName() + ". Let's Lambda")
				.functionName(context.getFunctionName())
				.memoryLimit(context.getMemoryLimitInMB())
				.build();
	}
}
