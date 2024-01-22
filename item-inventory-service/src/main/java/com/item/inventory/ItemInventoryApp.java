package com.item.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ItemInventoryApp {

	public static void main(String[] args) {
		SpringApplication.run(ItemInventoryApp.class, args);
	}

}
