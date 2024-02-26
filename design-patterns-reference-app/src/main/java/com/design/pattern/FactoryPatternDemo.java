package com.design.pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.design.pattern.model.Menu;
import com.design.pattern.model.MenuFactory;
import com.design.pattern.model.MenuType;

/*
 * Creates objects without exposing the instantiation logic to 
 * the client and Refers to the newly created object through a 
 * common interface. It is a simplified version of Factory Method.
 */
public class FactoryPatternDemo {

	@Test
	void factoryPatternDemo() {
		MenuFactory menufactory = new MenuFactory();

		List<Menu> menuList = new ArrayList<>(Arrays.asList(menufactory.getMenu(MenuType.DESSERT),
				menufactory.getMenu(MenuType.VEGETARIAN),
				menufactory.getMenu(MenuType.MAIN),
				menufactory.getMenu(MenuType.DEFAULT)));
		
		menuList.forEach(System.out::println);
	}
}
