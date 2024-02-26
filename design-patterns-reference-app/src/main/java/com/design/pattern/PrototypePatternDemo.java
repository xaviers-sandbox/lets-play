package com.design.pattern;

import org.junit.jupiter.api.Test;

import com.design.pattern.model.ClothesItemImpl;
import com.design.pattern.model.ClothesSizeType;

public class PrototypePatternDemo {

	@Test
	void PrototypeDemoTest() {

		ClothesItemImpl mediumSlacks = new ClothesItemImpl(ClothesSizeType.MEDIUM, "Slacks");
		ClothesItemImpl largeParka = new ClothesItemImpl(ClothesSizeType.LARGE, "Parka");

		ClothesItemImpl mediumClone = mediumSlacks.clone();
		ClothesItemImpl largeClone = largeParka.clone();

		//mediumClone.setName("Hawaiian Shirt");
		//largeClone.setName("Bucket Hat");
		
		System.out.println("Medium Slacks : " + mediumSlacks);
		System.out.println("Large Parka : " + largeParka);
		System.out.println("MediumClone : " + mediumClone);
		System.out.println("LargeClone : " + largeClone);
	}
}
