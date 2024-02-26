package com.design.pattern;

import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * Creational pattern that restricts the instantiation of a class and ensures that only one 
 * instance of the class exists in the JVM. 
 */
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