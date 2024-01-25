package com.item.review.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.item.review.handler.ItemReviewHandler;
import com.item.review.model.request.ItemReviewDTORequest;
import com.item.review.model.response.ItemReviewDTOResponse;
import com.item.review.service.ItemReviewService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration("ItemReviewRouter")
@OpenAPIDefinition(servers = @Server(url = "v1/item-reviews/app"))	
public class ItemReviewRouter {
	@Bean("itemReviewRoutes")
	@RouterOperations({ 
		@RouterOperation(path = "/app", consumes = "application/json", produces = "application/json", method = {
			RequestMethod.POST }, beanClass = ItemReviewService.class, beanMethod = "addNewItemReview", operation = @Operation(operationId = "addNewItemReview", description = "Adds a New Item Review", method = "POST", requestBody = @RequestBody(required = true, description = "Enter Request body as Json Object", content = @Content(schema = @Schema(implementation = ItemReviewDTORequest.class))), responses = {
					@ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = ItemReviewDTOResponse.class)))})),
		@RouterOperation(path = "/app", consumes = "application/json", produces = "application/json", method = {
					RequestMethod.GET }, beanClass = ItemReviewService.class, beanMethod = "getAllItemReviews", operation = @Operation(operationId = "getAllItemReviews", description = "Gets All Item Reviews", method = "GET", responses = {
							@ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = ItemReviewDTOResponse.class)))})),
			@RouterOperation(path = "/app/{id}", consumes = "application/json", produces = "application/json", method = {
					RequestMethod.GET }, beanClass = ItemReviewService.class, beanMethod = "getItemReviewById", operation = @Operation(operationId = "getItemReviewById", description = "Gets an Item Review by ID", method = "GET", parameters = @Parameter(name = "id", in = ParameterIn.PATH, style = ParameterStyle.SIMPLE, explode = Explode.FALSE, required = true), responses = {
							@ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = ItemReviewDTOResponse.class)))})),
			@RouterOperation(path = "/app/{id}", consumes = "application/json", produces = "application/json", method = {
					RequestMethod.PUT }, beanClass = ItemReviewService.class, beanMethod = "updateItemReviewById", operation = @Operation(operationId = "updateItemReviewById", description = "Updates an Item Review by ID", method = "PUT", parameters = @Parameter(name = "id", in = ParameterIn.PATH, style = ParameterStyle.SIMPLE, explode = Explode.FALSE, required = true), requestBody = @RequestBody(required = true, description = "Enter Request body as Json Object", content = @Content(schema = @Schema(implementation = ItemReviewDTORequest.class))), responses = {
							@ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = ItemReviewDTOResponse.class)))})),
			@RouterOperation(path = "/app/delete/all", consumes = "application/json", produces = "application/json", method = {
					RequestMethod.DELETE }, beanClass = ItemReviewService.class, beanMethod = "deleteAllItemReviews", operation = @Operation(operationId = "deleteAllItemReviews", description = "Deletes All Item Reviews", method = "DELETE")),
			@RouterOperation(path = "/app/delete/{id}", consumes = "application/json", produces = "application/json", method = {
					RequestMethod.DELETE }, beanClass = ItemReviewService.class, beanMethod = "deleteItemReviewById", operation = @Operation(operationId = "deleteItemReviewById", description = "Deletes an Item Review by ID", method = "DELETE", parameters = @Parameter(name = "id", in = ParameterIn.PATH, style = ParameterStyle.SIMPLE, explode = Explode.FALSE, required = true))),
			@RouterOperation(path = "/app/init-test-data/{size}", consumes = "application/json", produces = "application/json", method = {
					RequestMethod.POST }, beanClass = ItemReviewService.class, beanMethod = "initTestDataBySize", operation = @Operation(operationId = "initTestDataBySize", description = "Creates Test Data by Size", method = "POST", parameters = @Parameter(name = "size", in = ParameterIn.PATH, style = ParameterStyle.SIMPLE, explode = Explode.FALSE, required = true), responses = {
							@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ItemReviewDTOResponse.class)))}))})
	RouterFunction<ServerResponse> itemReviewRoutes(ItemReviewHandler itemReviewHandler) {
		return route().nest(path("v1/item-reviews/app"), builder -> {
			builder.POST("", (serverRequest -> itemReviewHandler.handleAddNewItemReview(serverRequest)))
					.POST("/init-test-data/{size}",
							(serverRequest -> itemReviewHandler.handleInitTestDataBySize(serverRequest)))
					.GET("", (serverRequest -> itemReviewHandler.handleGetAllItemReviews(serverRequest)))
					.GET("/{id}", (serverRequest -> itemReviewHandler.handleGetItemReviewById(serverRequest)))
					.PUT("/{id}", (serverRequest -> itemReviewHandler.handleUpdateItemReviewById(serverRequest)))
					.DELETE("/delete/all", (serverRequest -> itemReviewHandler.handleDeleteAllItemReviews()))
					.DELETE("/delete/{id}", (serverRequest -> itemReviewHandler.handleDeleteItemReviewById(serverRequest)));
		}).GET("hello", (serverRequest -> ServerResponse.ok().bodyValue("testingTesting123"))).build();
	}
}
