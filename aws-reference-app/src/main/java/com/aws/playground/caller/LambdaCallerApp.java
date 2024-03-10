package com.aws.playground.caller;

import java.nio.ByteBuffer;
import java.util.concurrent.Future;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambdaAsync;
import com.amazonaws.services.lambda.AWSLambdaAsyncClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;

@SpringBootApplication
public class LambdaCallerApp {

	private static class AsyncLambdaHandler implements AsyncHandler<InvokeRequest, InvokeResult> {
		@Override
		public void onSuccess(InvokeRequest req, InvokeResult res) {
			System.out.println("\nLambda function returned:");
			ByteBuffer response_payload = res.getPayload();
			System.out.println(new String(response_payload.array()));
			System.exit(0);
		}

		@Override
		public void onError(Exception e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(LambdaCallerApp.class, args);

		String function_name = "hello-world-function";

		String function_input = "{\"name\":\"gold-bears\"}";

		AWSLambdaAsync lambda = AWSLambdaAsyncClientBuilder.standard()
				.withRegion(Regions.fromName("us-east-1"))
				.build();

		InvokeRequest req = new InvokeRequest().withFunctionName(function_name)
				.withPayload(ByteBuffer.wrap(function_input.getBytes()));

		Future<InvokeResult> future_res = lambda.invokeAsync(req, new AsyncLambdaHandler());

		System.out.print("Waiting for async callback");
		while (!future_res.isDone() && !future_res.isCancelled()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.err.println("Thread.sleep() was interrupted!");
				System.exit(0);
			}
			System.out.print(".");
		}
	}

}
