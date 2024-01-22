package com.item.review.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.item.review.handler.ItemReviewHandler;

@Configuration
public class ItemReviewRouter {

	@Bean("itemReviewRoutes")
	RouterFunction<ServerResponse> itemReviewRoutes(ItemReviewHandler itemReviewHandler) {

		return route().nest(path("v1/item-reviews/app"), builder -> {
			builder.POST("", (serverRequest -> itemReviewHandler.handleAddNewItemReview(serverRequest)))
					.POST("/init-test-data/{size}",
							(serverRequest -> itemReviewHandler.handleInitTestDataBySize(serverRequest)))
					.GET("", (serverRequest -> itemReviewHandler.handleGetAllItemReviews(serverRequest)))
					.GET("/{id}", (serverRequest -> itemReviewHandler.handleGetItemReviewById(serverRequest)))
					.PUT("/{id}", (serverRequest -> itemReviewHandler.handleUpdateItemReviewById(serverRequest)))
					.DELETE("/delete/all", (serverRequest -> itemReviewHandler.handleDeleteAllItemReviews()))
					.DELETE("/{id}", (serverRequest -> itemReviewHandler.handleDeleteItemReviewById(serverRequest)));
		}).GET("hello", (serverRequest -> ServerResponse.ok().bodyValue("testingTesting123"))).build();
	}
}
