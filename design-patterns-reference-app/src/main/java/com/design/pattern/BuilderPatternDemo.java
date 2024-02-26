package com.design.pattern;

import org.junit.jupiter.api.Test;

import com.design.pattern.model.Employee;


/*
 * A creational design pattern that constructs complex objects step by step. It solves the 
 * problem of having constructors with many parameters
 */
public class BuilderPatternDemo {

	@Test
	void builderPatternDemo() {

		Employee e = new Employee.Builder().name("Joyce Chin").title("Cloud Archictect II").salary(225_000.00).build();

		System.out.println("e - " + e);
	}
}