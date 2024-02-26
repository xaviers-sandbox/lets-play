package com.design.pattern;

import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SingletonPattern {
	private static ObjectMapper mapper;

	// disable instantiating
	private SingletonPattern() {
	}

	public static ObjectMapper getMapper() {
		if (ObjectUtils.isEmpty(mapper)) {
			mapper = new ObjectMapper();
		}

		return mapper;
	}
}