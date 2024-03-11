package com.aws.playground.caller;

import java.nio.ByteBuffer;
import java.util.concurrent.Future;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambdaAsync;
import com.amazonaws.services.lambda.AWSLambdaAsyncClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.aws.lambda.model.RequestDTO;
import com.aws.playground.handler.AsyncLambdaHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class LambdaCaller {
	private static ObjectMapper mapper;

	public static ObjectMapper getMapper() {
		if (ObjectUtils.isEmpty(mapper)) {
			mapper = new ObjectMapper();
		}

		return mapper;
	}

	public static void main(String[] args) {
		SpringApplication.run(LambdaCaller.class, args);

		String function_name = "hello-world-function";

		AWSLambdaAsync lambda = AWSLambdaAsyncClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		
		String requestStr = objectToJsonString(new RequestDTO("slim jim"));

		InvokeRequest req = new InvokeRequest().withFunctionName(function_name)
				.withPayload(ByteBuffer.wrap(requestStr.getBytes()));

		Future<InvokeResult> future_res = lambda.invokeAsync(req, new AsyncLambdaHandler());

		System.out.println("Waiting for callback");

		while (!future_res.isDone() && !future_res.isCancelled()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.err.println("Thread.sleep() interrupted!");
				System.exit(0);
			}
		}
	}

	public static String objectToJsonString(RequestDTO requestDTO) {
		try {
			return getMapper().writeValueAsString(requestDTO);
		} catch (JsonProcessingException e) {
			return "";
		}
	}
}
