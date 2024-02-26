package com.design.pattern;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.design.pattern.model.AquaticMammal;
import com.design.pattern.model.LandMammal;
import com.design.pattern.model.ZooFacadeImpl;


/*
 * Provides a unified interface to a set of interfaces in a subsystem. 
 * Facade Pattern defines a higher-level interface that makes the subsystem easier to use.
 */
public class FacadePatternDemo {
	@Test
	void facadePatternDemo() {
		AquaticMammal aquaticMammal = AquaticMammal.builder().name("Killer Whale").numberOfFins(5).build();
		LandMammal landMammal = LandMammal.builder().name("Leopard").numberOfLegs(4).build();

		ZooFacadeImpl zoo = new ZooFacadeImpl(Collections.singletonList(aquaticMammal), Collections.singletonList(landMammal));
		
		 zoo.displayTheZoosAquaticMammals();
		 zoo.displayTheZoosLandMammals();
	}
}
