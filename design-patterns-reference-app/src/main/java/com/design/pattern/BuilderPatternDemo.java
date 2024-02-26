package com.design.pattern;

import org.junit.jupiter.api.Test;

import com.design.pattern.model.Employee;

public class BuilderPatternDemo {

	@Test
	void builderPatternDemo() {

		Employee e = new Employee.Builder().name("Joyce Chin").title("Cloud Engineer V").salary(225_000.00).build();

		System.out.println("e - " + e);
	}
}