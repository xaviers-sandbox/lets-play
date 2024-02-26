package com.design.pattern.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class ZooFacadeImpl implements Zoo {
	List<AquaticMammal> aquaticMammalList;
	List<LandMammal> landMammalList;
	
	@Override
	public void displayTheZoosAquaticMammals() {
		aquaticMammalList.forEach(System.out::println);
	}

	@Override
	public void displayTheZoosLandMammals() {
		landMammalList.forEach(System.out::println);
	}
}
