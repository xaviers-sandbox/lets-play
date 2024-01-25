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

import com.item.review.handler.management.DatabaseManagementHandler;
import com.item.review.model.response.ItemReviewDTOResponse;
import com.item.review.service.management.DatabaseManagementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Configuration
public class DatabaseManagementRouter {

	@Bean("databaseManagementRoutes")
	@RouterOperations({@RouterOperation(path = "/dbs", consumes = "application/json", produces = "application/json", method = {
					RequestMethod.GET }, beanClass = DatabaseManagementService.class, beanMethod = "getAllItemReviewsFromDBManagement", operation = @Operation(operationId = "getAllItemReviewsFromDBManagement", description = "Gets All Item Reviews From the DB", method = "GET", responses = {
							@ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = ItemReviewDTOResponse.class))) })),
			@RouterOperation(path = "/dbs/{id}", consumes = "application/json", produces = "application/json", method = {
					RequestMethod.GET }, beanClass = DatabaseManagementService.class, beanMethod = "getItemReviewByIdFromDBManagement", operation = @Operation(operationId = "getItemReviewByIdFromDBManagement", description = "Gets an Item Review by ID From the DB", method = "GET", parameters = @Parameter(name = "id", in = ParameterIn.PATH, style = ParameterStyle.SIMPLE, explode = Explode.FALSE, required = true), responses = {
							@ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = ItemReviewDTOResponse.class))) })),})
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
