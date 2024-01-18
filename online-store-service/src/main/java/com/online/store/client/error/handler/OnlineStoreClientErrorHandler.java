package com.online.store.client.error.handler;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;

import com.online.store.exception.ExternalClient400Exception;
import com.online.store.exception.ExternalClient500Exception;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OnlineStoreClientErrorHandler {
	private static final String ITEM_INvENTORY_5xx_ERROR = "5xxServerError During Item Inventory Api Call.";
	private static final String ITEM_INvENTORY_4xx_ERROR = "4xxClientError During Item Inventory Api Call. Item(s) not Found.";

	private static final String ITEM_REVIEW_5xx_ERROR = "5xxServerError During Item Review Api Call";
	private static final String ITEM_REVIEW_4xx_ERROR = "4xxClientError During Item Review Api Call";

	public Mono<Throwable> processItemIventory4xxClientError(ClientResponse clientResponse) {
		String logString = String.format("processItemIventory4xxClientError %s statusCode=%s",
				ITEM_INvENTORY_4xx_ERROR,
				clientResponse.statusCode().value());

		log.debug(logString);

		if (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
			return clientResponse.bodyToMono(String.class)
					.flatMap(responseMessage -> Mono.error(new ExternalClient400Exception(logString)));

		}

		return clientResponse.bodyToMono(String.class)
				.flatMap(responseMessage -> Mono.error(new ExternalClient400Exception(responseMessage)));
	}

	public Mono<Throwable> processItemIventory4xxClientError(ClientResponse clientResponse, String itemInventoryId) {
		String logString = String.format("%s statusCode=%s - itemInventoryId=%s",
				ITEM_INvENTORY_4xx_ERROR,
				clientResponse.statusCode().value(),
				itemInventoryId);

		log.debug(logString);

		if (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
			return clientResponse.bodyToMono(String.class)
					.flatMap(responseMessage -> Mono.error(new ExternalClient400Exception(logString)));

		}

		return clientResponse.bodyToMono(String.class)
				.flatMap(responseMessage -> Mono.error(new ExternalClient400Exception(responseMessage)));
	}

	public Mono<Throwable> processItemInventory5xxServerError(ClientResponse clientResponse) {
		log.debug("processItemInventory5xxServerError {} - statusCode={}",
				ITEM_INvENTORY_5xx_ERROR,
				clientResponse.statusCode().value());

		return clientResponse.bodyToMono(String.class)
				.flatMap(responseMessage -> Mono.error(new ExternalClient500Exception(ITEM_INvENTORY_5xx_ERROR)));
	}

	public Mono<Throwable> processItemReview4xxClientError(ClientResponse clientResponse) {
		log.debug("processItemReview4xxClientError {} - statusCode={}",
				ITEM_REVIEW_4xx_ERROR,
				clientResponse.statusCode().value());

		return clientResponse.statusCode().equals(HttpStatus.NOT_FOUND) ? Mono.empty()
				: clientResponse.bodyToMono(String.class)
						.flatMap(responseMessage -> Mono.error(new ExternalClient400Exception(responseMessage)));
	}

	public Mono<Throwable> processItemReview5xxServerError(ClientResponse clientResponse) {
		log.debug("processItemReview5xxServerError {} - statusCode={}",
				ITEM_REVIEW_5xx_ERROR,
				clientResponse.statusCode().value());

		return clientResponse.bodyToMono(String.class)
				.flatMap(responseMessage -> Mono.error(new ExternalClient500Exception(ITEM_REVIEW_5xx_ERROR)));
	}
}
