package com.item.inventory.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
@OpenAPIDefinition
public class SwaggerConfigs {
	@Bean
	OpenAPI config() {
		License license = new License().name("Apache 2.0").url("www.Apache 2.0.com");
		Contact contact = new Contact().name("Xavier's Sandbox").email("xavier@sandbox.com");

		return new OpenAPI().info(new Info().title("Item Inventory Service")
				.version("v1.0.0")
				.description("Crud Operations For an Item's Iventory")
				.license(license)
				.contact(contact))
				.servers(List.of(new Server().url("http://localhost:1111")));
	}
}