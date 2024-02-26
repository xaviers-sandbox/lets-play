package com.design.pattern.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class LandMammal extends Mammal{

	private int numberOfLegs;

	@Builder
	public LandMammal(String name, int numberOfLegs) {
		super(name);
		this.numberOfLegs = numberOfLegs;
	}

	@Override
	public String toString() {
		return "LandMammal [numberOfLegs=" + numberOfLegs + ", name=" + getName() + "]";
	}
	
	
}
