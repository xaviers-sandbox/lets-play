package com.aws.lambda.handler;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.aws.lambda.model.RequestDTO;
import com.aws.lambda.model.ResponseDTO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HelloHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
	private ObjectMapper mapper;

	public ObjectMapper getMapper() {
		if (ObjectUtils.isEmpty(mapper)) {
			mapper = new ObjectMapper();
		}

		return mapper;
	}
	
	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
		RequestDTO requestDTO = (RequestDTO) mapStringToObject(request.getBody(), RequestDTO.class);
		
		context.getLogger().log(requestDTO.getName() + " is speaking");


		return new APIGatewayProxyResponseEvent().withStatusCode(200)
				.withBody(ResponseDTO.builder()
						.message("Hello " + requestDTO.getName() + ". Let's Lambda!")
						.functionName(context.getFunctionName())
						.memoryLimit(context.getMemoryLimitInMB())
						.build()
						.toString());
	}

	public <T> Object mapStringToObject(String anyString, Class<T> anyClass) {
		try {

			return getMapper().readValue(anyString, anyClass);

		} catch (JsonParseException e) {

			log.error("mapStringToObject JsonParseException message={}", e.getLocalizedMessage());

		} catch (JsonMappingException e) {

			log.error("mapStringToObject JsonMappingException message={}", e.getLocalizedMessage());

		} catch (JsonProcessingException e) {

			log.error("mapStringToObject JsonProcessingException message={}", e.getLocalizedMessage());
		}

		return null;
	}
}
