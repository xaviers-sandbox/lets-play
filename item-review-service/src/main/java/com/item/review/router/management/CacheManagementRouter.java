package com.item.review.router.management;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.item.review.handler.management.CacheManagementHandler;

@Configuration
public class CacheManagementRouter {

	@Bean("cacheManagementRoutes")
	RouterFunction<ServerResponse> cacheManagementRoutes(CacheManagementHandler cacheManagementHandler) {

		return route().nest(path("v1/item-reviews/caches"), builder -> {
			builder.POST("",
					(serverRequest -> cacheManagementHandler.handleAddNewItemReviewToCacheManagement(serverRequest)))
					.POST("/rebuild",
							(serverRequest -> cacheManagementHandler
									.handleRebuildItemReviewCacheFromDBManagement(serverRequest)))
					.GET("",
							(serverRequest -> cacheManagementHandler
									.handleGetAllItemReviewsFromCacheManagement(serverRequest)))
					.GET("/{id}",
							(serverRequest -> cacheManagementHandler
									.handleGetItemReviewByIdFromCacheManagement(serverRequest)))
					.DELETE("/delete/all", (serverRequest -> cacheManagementHandler.handleDeleteAllItemReviews()))
					.DELETE("/{id}",
							(serverRequest -> cacheManagementHandler
									.handleDeleteItemReviewByIdFromCacheManagement(serverRequest)));
		}).GET("hello", (serverRequest -> ServerResponse.ok().bodyValue("testingTesting123"))).build();
	}
}
