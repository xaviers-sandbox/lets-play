package com.design.pattern.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class ClothesItemImpl implements ClothesItem  {
	private ClothesSizeType size;
	private String name;

	public ClothesItemImpl(ClothesSizeType size, String name) {
		this.size = size;
		this.name = name;
	}

	@Override
	public ClothesItemImpl clone() {
		return new ClothesItemImpl(this.size, this.name);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
}
