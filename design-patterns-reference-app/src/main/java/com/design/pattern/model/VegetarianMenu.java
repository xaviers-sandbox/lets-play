package com.design.pattern.model;

import java.util.Arrays;
import java.util.List;

import lombok.Data;

@Data
public class VegetarianMenu implements Menu{
	List<String> vegetarianItems;
	
	public VegetarianMenu() {
		this.vegetarianItems = Arrays.asList("Sesame Tofu & Broccoli", "Black Bean Tacos", "Indian Butter Chickpeas");
	}
}
