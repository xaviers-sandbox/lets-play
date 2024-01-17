package com.online.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class WebClientConfigs {

	@Bean
	WebClient buildWebClient(WebClient.Builder webclientBuilder) {
		return webclientBuilder
		// .filters(exchangeFilterFunctions -> {
		// exchangeFilterFunctions.add(logRequest());
		// exchangeFilterFunctions.add(logResponse());
//		})
				.build();

	}

	public ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
			if (log.isDebugEnabled()) {
				log.debug("Logging Request: method={} url={}", clientRequest.method(), clientRequest.url());
			}
			return Mono.just(clientRequest);
		});
	}

	public ExchangeFilterFunction logResponse() {
		return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
			if (log.isDebugEnabled()) {
				clientResponse.bodyToMono(String.class).subscribe(i -> {
					log.debug("Logging Response:: response={}", i);
				});
				// log.info("tracey ross sexy ass - Request: {}",
				// clientRequest.headers()
				// .forEach((name, values) -> values.forEach(value -> log.info("tracey ross
				// {}={}", name, value)));
			}
			return Mono.just(clientResponse);
		});
	}
}
