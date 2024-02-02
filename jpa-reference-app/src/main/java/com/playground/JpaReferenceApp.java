package com.playground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.playground.entity")
@EnableJpaRepositories("com.playground.repo")
public class JpaReferenceApp {

	public static void main(String[] args) {
		SpringApplication.run(JpaReferenceApp.class, args);
	}

}
