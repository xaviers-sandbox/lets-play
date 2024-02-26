package com.design.pattern.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AquaticMammal extends Mammal {
	private int numberOfFins;

	@Builder
	public AquaticMammal(String name, int numberOfFins) {
		super(name);
		this.numberOfFins = numberOfFins;
	}

	@Override
	public String toString() {
		return "AquaticMammal [numberOfFins=" + numberOfFins + ", name=" + getName() + "]";
	}
}
