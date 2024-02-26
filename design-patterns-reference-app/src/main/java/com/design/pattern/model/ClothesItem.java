package com.design.pattern.model;

public interface ClothesItem extends Cloneable {
    String getName();
    void setName(String name);
    ClothesItemImpl clone();
}
