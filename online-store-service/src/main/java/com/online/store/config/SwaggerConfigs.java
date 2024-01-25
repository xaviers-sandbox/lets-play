package com.online.store.config;

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

		return new OpenAPI().info(new Info().title("Online Store Service")
				.version("v1.0.0")
				.description("Get Operations For The Stores Inventory")
				.license(license)
				.contact(contact)).servers(List.of(new Server().url("http://localhost:3333/v1/online-store")));
	}
}