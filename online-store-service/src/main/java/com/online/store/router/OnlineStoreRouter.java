package com.online.store.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.online.store.handler.OnlineStoreHandler;

@Configuration
public class OnlineStoreRouter {

	@Bean
	RouterFunction<ServerResponse> onlineStoreRoutes(OnlineStoreHandler onlineStoreHandler) {

		return route().nest(path("v1/online-store/item"), builder -> {
			builder.GET("/{id}",
					(serverRequest -> onlineStoreHandler.handleGetItemDetailsByItemInventoryId(serverRequest)));
		}).nest(path("v1/online-store/item-reviews"), builder -> {
			builder.GET("", (serverRequest -> onlineStoreHandler.handleGetAllItemReviews(serverRequest)))
					.GET("/{id}", (serverRequest -> onlineStoreHandler.handleGetItemReviewById(serverRequest)));
		}).nest(path("v1/online-store/item-inventories"), builder -> {
			builder.GET("", (serverRequest -> onlineStoreHandler.handleGetAllItemInventories(serverRequest)))
					.GET("/{id}", (serverRequest -> onlineStoreHandler.handleGetItemInventoryById(serverRequest)));
		}).GET("hello", (serverRequest -> ServerResponse.ok().bodyValue("testingTesting123"))).build();
	}
}
