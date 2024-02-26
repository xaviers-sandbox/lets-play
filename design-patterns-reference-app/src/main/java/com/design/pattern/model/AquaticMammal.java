package com.design.pattern.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AquaticMammal extends Mammal {
	private int numberOfFins;

	@Builder
	public AquaticMammal(String name, int numberOfFins) {
		super(name);
		this.numberOfFins = numberOfFins;
	}

	public String swim() {
		return "The " + getName() + " is swimming.";
	}

	@Override
	public String toString() {
		return "AquaticMammal [name=" + getName() + ", numberOfFins=" + numberOfFins + ", " + swim() + "]";
	}
}
