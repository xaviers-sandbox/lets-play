package com.item.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
@EnableDiscoveryClient
@ComponentScan("com.item.inventory")
public class ItemInventoryApp {

	public static void main(String[] args) {
		SpringApplication.run(ItemInventoryApp.class, args);
	}

}
