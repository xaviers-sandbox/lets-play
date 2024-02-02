package com.sandbox.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SandboxUtils {
	private static ObjectMapper mapper = new ObjectMapper();

	public static void prettyPrintObjectToJson(Object anyObject) {
		try {

			log.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(anyObject));

		} catch (JsonProcessingException e) {

			log.error("prettyPrintObjectToJson JsonProcessingException message={}", e.getLocalizedMessage());
		}
	}

	public static String getPrettyPrintJsonFromObject(Object anyObject) {
		try {

			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(anyObject);

		} catch (JsonProcessingException e) {

			log.error("getPrettyPrintJsonFromObject JsonProcessingException message={}", e.getLocalizedMessage());

		}

		return "";
	}

	public static String convertObjectToString(Object anyObject) {
		try {
			return mapper.writeValueAsString(anyObject);
		} catch (JsonProcessingException e) {
			log.error("convertObjectToString JsonProcessingException message={}", e.getLocalizedMessage());
			
			return "";
		}
	}

	public static <T> Object mapStringToObject(String anyString, Class<T> anyClass) {
		try {

			return mapper.readValue(anyString, anyClass);

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
