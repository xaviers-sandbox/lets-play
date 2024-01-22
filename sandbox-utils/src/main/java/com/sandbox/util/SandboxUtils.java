package com.sandbox.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SandboxUtils {
	public static void prettyPrintObjectToJson(Object anyObject) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			log.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(anyObject));

		} catch (JsonProcessingException e) {

			log.error("JsonProcessingException message={}", e.getLocalizedMessage());
		}
	}

	public static String getPrettyPrintJsonFromObject(Object anyObject) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(anyObject);

		} catch (JsonProcessingException e) {

			log.error("JsonProcessingException message={}", e.getLocalizedMessage());

			return "";
		}
	}
}
