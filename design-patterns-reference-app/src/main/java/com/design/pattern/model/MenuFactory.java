package com.design.pattern.model;

import org.apache.commons.lang3.ObjectUtils;

public class MenuFactory {
	public Menu getMenu(MenuType menuType) {
		if(ObjectUtils.isEmpty(menuType))
			return new MainMenu();
			
		switch (menuType) {
		case MenuType.DESSERT:
			return new DessertMenu();
		case MenuType.VEGETARIAN:
			return new VegetarianMenu();
		default:
			return new MainMenu();
		}
	}
}
