package com.design.pattern.model;

import java.util.List;

public interface Zoo {
	public void setAquaticMammals(List<AquaticMammal> aquaticMammalList);

	public List<AquaticMammal> getAquaticMammals();

	public void addNewAquaticMammal(AquaticMammal aquaticMammal);

	public void setLandMammals(List<LandMammal> aquaticMammalList);

	public List<LandMammal> getLandMammals();

	public void addNewLandMammal(LandMammal landMammal);
}
