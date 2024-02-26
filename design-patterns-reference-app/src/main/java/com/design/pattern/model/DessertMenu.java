package com.design.pattern.model;

import java.util.Arrays;
import java.util.List;

import lombok.Data;

@Data
public class DessertMenu implements Menu{
	List<String> dessertItems ;
	
	public DessertMenu() {
		this.dessertItems = Arrays.asList("Ice Cream", "Carrot Cake", "Hot Chocolate");
	}
}
