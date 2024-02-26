package com.design.pattern;

import org.junit.jupiter.api.Test;

import com.design.pattern.model.ImmutableObject;

public class ImmutableObjectDemo {
	@Test
	public void immutableTest() {
		ImmutableObject immutableObject = new ImmutableObject("tester", 35);
		System.out.println("name=" + immutableObject.getName().toUpperCase());
		System.out.println("age=" + immutableObject.getAge());
	}
}
