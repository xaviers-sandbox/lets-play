package com.item.review.router.management;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.item.review.handler.management.DatabaseManagementHandler;

@Configuration
public class DatabaseManagementRouter {

	@Bean("databaseManagementRoutes")
	RouterFunction<ServerResponse> databaseManagementRoutes(DatabaseManagementHandler databaseManagementHandler) {
		return route().nest(path("v1/item-reviews/dbs"), builder -> {
			builder.GET("",
					(serverRequest -> databaseManagementHandler.handleGetAllItemReviewsFromDBManagement(serverRequest)))
					.GET("/{id}",
							(serverRequest -> databaseManagementHandler
									.handleGetItemReviewByIdFromDBManagement(serverRequest)));
		}).GET("hello", (serverRequest -> ServerResponse.ok().bodyValue("testingTesting123"))).build();
	}
}
