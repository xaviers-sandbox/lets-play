package com.aws.playground.handler;

import java.nio.ByteBuffer;

import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;

public class AsyncLambdaHandler implements AsyncHandler<InvokeRequest, InvokeResult> {
	@Override
	public void onSuccess(InvokeRequest req, InvokeResult resp) {
		System.out.println("\nSuccessful Function Call!");
		ByteBuffer responsePayload = resp.getPayload();
		System.out.println(new String(responsePayload.array()));
		System.exit(0);
	}

	@Override
	public void onError(Exception e) {
		System.out.println(e.getLocalizedMessage());
		System.exit(1);
	}
}