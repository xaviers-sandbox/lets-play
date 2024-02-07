package com.sandbox.util;

import org.apache.commons.lang3.ObjectUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SandboxUtils {
	private static ObjectMapper mapper;

	public static ObjectMapper getMapper() {
		if (ObjectUtils.isEmpty(mapper)) {
			mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
		}

		return mapper;
	}

	public static void prettyPrintObjectToJson(Object anyObject) {
		try {

			log.debug(getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(anyObject));

		} catch (JsonProcessingException e) {

			log.error("prettyPrintObjectToJson JsonProcessingException message={}", e.getLocalizedMessage());
		}
	}

	public static String getPrettyPrintJsonFromObject(Object anyObject) {
		try {

			return getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(anyObject);

		} catch (JsonProcessingException e) {

			log.error("getPrettyPrintJsonFromObject JsonProcessingException message={}", e.getLocalizedMessage());

		}

		return "";
	}

	public static String convertObjectToString(Object anyObject) {
		try {
			return getMapper().writeValueAsString(anyObject);
		} catch (JsonProcessingException e) {
			log.error("convertObjectToString JsonProcessingException message={}", e.getLocalizedMessage());

			return "";
		}
	}

	public static <T> Object mapStringToObject(String anyString, Class<T> anyClass) {
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
