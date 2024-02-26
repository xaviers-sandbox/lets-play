package com.design.pattern.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import lombok.ToString;

@ToString
public class ZooImpl implements Zoo {
	List<AquaticMammal> aquaticMammalList;
	List<LandMammal> landMammalList;

	@Override
	public void setAquaticMammals(List<AquaticMammal> aquaticMammalList) {
		this.aquaticMammalList = aquaticMammalList;
	}

	@Override
	public List<AquaticMammal> getAquaticMammals() {
		return this.aquaticMammalList;
	}

	@Override
	public void addNewAquaticMammal(AquaticMammal aquaticMammal) {
		if (CollectionUtils.isEmpty(aquaticMammalList))
			aquaticMammalList = new ArrayList<>();

		aquaticMammalList.add(aquaticMammal);
	}

	@Override
	public void setLandMammals(List<LandMammal> landMammalList) {
		this.landMammalList = landMammalList;

	}

	@Override
	public List<LandMammal> getLandMammals() {
		return this.landMammalList;
	}

	@Override
	public void addNewLandMammal(LandMammal landMammal) {
		if (CollectionUtils.isEmpty(landMammalList))
			landMammalList = new ArrayList<>();

		landMammalList.add(landMammal);
	}
}
