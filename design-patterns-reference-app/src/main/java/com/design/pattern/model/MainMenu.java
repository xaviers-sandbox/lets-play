package com.design.pattern.model;

import java.util.Arrays;
import java.util.List;

import lombok.Data;

@Data
public class MainMenu implements Menu{
	List<String> mainItems;
	
	public MainMenu() {
		this.mainItems = Arrays.asList("Chicken Panini", "Shrimp Quesadilla", "Lamb Kabobs");
	}
}
