package com.online.store.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OnlineStoreUtil {
	public static void prettyPrintObjectToJson(Object anyObject) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			mapper.writeValueAsString(anyObject);

			log.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(anyObject));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}
