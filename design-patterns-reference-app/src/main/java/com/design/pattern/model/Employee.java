package com.design.pattern.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Employee {
	private String name;
	private String title;
	private Double salary;

	public Employee(Builder builder) {
		this.name = builder.name;
		this.title = builder.title;
		this.salary = builder.salary;
	}

	public static class Builder {
		private String name;
		private String title;
		private Double salary;

		public Builder name(String name) {
			this.name = name;
			
			return this;
		}

		public Builder title(String title) {
			this.title = title;
			
			return this;
		}

		public Builder salary(Double salary) {
			this.salary = salary;
			
			return this;
		}

		public Employee build() {
			return new Employee(this);
		}
	}
}
