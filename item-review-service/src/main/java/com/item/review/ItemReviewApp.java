package com.item.review;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ItemReviewApp {

	public static void main(String[] args) {
		SpringApplication.run(ItemReviewApp.class, args);
	}

}
