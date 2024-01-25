package com.item.review.router.management;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.item.review.handler.management.CacheManagementHandler;
import com.item.review.model.request.ItemReviewDTORequest;
import com.item.review.model.response.ItemReviewDTOResponse;
import com.item.review.service.management.CacheManagementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Configuration
public class CacheManagementRouter {

	@Bean("cacheManagementRoutes")
	@RouterOperations({
			@RouterOperation(path = "/caches", consumes = "application/json", produces = "application/json", method = {
					RequestMethod.POST }, beanClass = CacheManagementService.class, beanMethod = "addNewItemReviewToCacheManagement", operation = @Operation(operationId = "addNewItemReviewToCacheManagement", description = "Adds a New Item Review to the Cache", method = "POST", requestBody = @RequestBody(required = true, description = "Enter Request body as Json Object", content = @Content(schema = @Schema(implementation = ItemReviewDTORequest.class))), responses = {
							@ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = ItemReviewDTOResponse.class))) })),
			@RouterOperation(path = "/caches", consumes = "application/json", produces = "application/json", method = {
					RequestMethod.GET }, beanClass = CacheManagementService.class, beanMethod = "getAllItemReviewsFromCacheManagement", operation = @Operation(operationId = "getAllItemReviewsFromCacheManagement", description = "Gets All Item Reviews From the Cache", method = "GET", responses = {
							@ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = ItemReviewDTOResponse.class))) })),
			@RouterOperation(path = "/caches/{id}", consumes = "application/json", produces = "application/json", method = {
					RequestMethod.GET }, beanClass = CacheManagementService.class, beanMethod = "getItemReviewByIdFromCacheManagement", operation = @Operation(operationId = "getItemReviewByIdFromCacheManagement", description = "Gets an Item Review by ID From the Cache", method = "GET", parameters = @Parameter(name = "id", in = ParameterIn.PATH, style = ParameterStyle.SIMPLE, explode = Explode.FALSE, required = true), responses = {
							@ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = ItemReviewDTOResponse.class))) })),
			@RouterOperation(path = "/caches/delete/all", consumes = "application/json", produces = "application/json", method = {
					RequestMethod.DELETE }, beanClass = CacheManagementService.class, beanMethod = "deleteItemReviewCacheManagement", operation = @Operation(operationId = "deleteItemReviewCacheManagement", description = "Deletes All Item Reviews in the Cache", method = "DELETE")),
			@RouterOperation(path = "/caches/delete/{id}", consumes = "application/json", produces = "application/json", method = {
					RequestMethod.DELETE }, beanClass = CacheManagementService.class, beanMethod = "deleteItemReviewByIdFromCacheManagement", operation = @Operation(operationId = "deleteItemReviewByIdFromCacheManagement", description = "Deletes an Item Review by ID From the Cache", method = "DELETE", parameters = @Parameter(name = "id", in = ParameterIn.PATH, style = ParameterStyle.SIMPLE, explode = Explode.FALSE, required = true))),
			@RouterOperation(path = "/caches/rebuild", consumes = "application/json", produces = "application/json", method = {
					RequestMethod.POST }, beanClass = CacheManagementService.class, beanMethod = "rebuildItemReviewCacheFromDBManagement", operation = @Operation(operationId = "rebuildItemReviewCacheFromDBManagement", description = "Rebuilds the Item Review Cache", method = "POST", responses = {
							@ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = ItemReviewDTOResponse.class))) }))

	})
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
					.DELETE("/delete/all",
							(serverRequest -> cacheManagementHandler.handleDeleteAllItemReviews()))
					.DELETE("/delete/{id}",
							(serverRequest -> cacheManagementHandler
									.handleDeleteItemReviewByIdFromCacheManagement(serverRequest)));
		}).GET("hello", (serverRequest -> ServerResponse.ok().bodyValue("testingTesting123"))).build();
	}
}
