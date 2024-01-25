package com.online.store.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.online.store.handler.OnlineStoreHandler;
import com.online.store.model.OnlineStoreDTOResponse;
import com.online.store.service.OnlineStoreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Configuration
public class OnlineStoreRouter {

	@Bean("onlineStoreRoutes")
	@RouterOperations({
			@RouterOperation(path = "/item/{itemInventoryId}", produces = "application/json", method = {
					RequestMethod.GET }, beanClass = OnlineStoreService.class, beanMethod = "getItemDetailsByItemInventoryId", operation = @Operation(operationId = "getItemDetailsByItemInventoryId", description = "Gets an Item's Details by ItemInventoryId", method = "GET", parameters = @Parameter(name = "itemInventoryId", in = ParameterIn.PATH, style = ParameterStyle.SIMPLE, explode = Explode.FALSE, required = true),responses = {
							@ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = OnlineStoreDTOResponse.class))) })),
			@RouterOperation(path = "/item-reviews", produces = "application/json", method = {
					RequestMethod.GET }, beanClass = OnlineStoreService.class, beanMethod = "getAllItemReviews", operation = @Operation(operationId = "getAllItemReviews", description = "Gets All Item Reviews", method = "GET", responses = {
							@ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = OnlineStoreDTOResponse.class))) })),

			@RouterOperation(path = "/item-reviews/{id}", produces = "application/json", method = {
					RequestMethod.GET }, beanClass = OnlineStoreService.class, beanMethod = "getItemReviewById", operation = @Operation(operationId = "getItemReviewById", description = "Gets an Item Review by ID", method = "GET", parameters = @Parameter(name = "id", in = ParameterIn.PATH, style = ParameterStyle.SIMPLE, explode = Explode.FALSE, required = true), responses = {
							@ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = OnlineStoreDTOResponse.class))) })),

			@RouterOperation(path = "/item-inventories", produces = "application/json", method = {
					RequestMethod.GET }, beanClass = OnlineStoreService.class, beanMethod = "getAllItemInventories", operation = @Operation(operationId = "getAllItemInventories", description = "Gets All Item Inventories", method = "GET", responses = {
							@ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = OnlineStoreDTOResponse.class))) })),

			@RouterOperation(path = "/item-inventories/{id}", produces = "application/json", method = {
					RequestMethod.GET }, beanClass = OnlineStoreService.class, beanMethod = "getItemInventoryById", operation = @Operation(operationId = "getItemInventoryById", description = "Gets an Item Inventory by ID", method = "GET", parameters = @Parameter(name = "id", in = ParameterIn.PATH, style = ParameterStyle.SIMPLE, explode = Explode.FALSE, required = true), responses = {
							@ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = OnlineStoreDTOResponse.class))) })) })
	RouterFunction<ServerResponse> onlineStoreRoutes(OnlineStoreHandler onlineStoreHandler) {
		return route().nest(path("v1/online-store"), builder -> {
			builder.GET("/item/{itemInventoryId}",
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
