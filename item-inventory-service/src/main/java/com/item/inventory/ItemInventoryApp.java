package com.item.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableCaching
@EnableFeignClients(basePackages = { "com.item.inventory.proxy" })
@EnableDiscoveryClient
public class ItemInventoryApp {

	public static void main(String[] args) {
		SpringApplication.run(ItemInventoryApp.class, args);
	}

}
