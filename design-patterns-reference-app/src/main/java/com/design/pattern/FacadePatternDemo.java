package com.design.pattern;

import org.junit.jupiter.api.Test;

import com.design.pattern.model.AquaticMammal;
import com.design.pattern.model.LandMammal;
import com.design.pattern.model.ZooImpl;

public class FacadePatternDemo {
	@Test
	void facadePatternDemo() {
		AquaticMammal aquaticMammal = AquaticMammal.builder().name("Killer Whale").numberOfFins(5).build();
		LandMammal landMammal = LandMammal.builder().name("Leopard").numberOfLegs(4).build();

		ZooImpl zoo = new ZooImpl();
		
		zoo.addNewAquaticMammal(aquaticMammal);
		zoo.addNewLandMammal(landMammal);
		
		System.out.println("zoo=" + zoo);
	}
}
