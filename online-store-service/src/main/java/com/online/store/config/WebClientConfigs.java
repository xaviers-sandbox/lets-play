package com.online.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class WebClientConfigs {

	@Bean
	WebClient webClient(ReactiveClientRegistrationRepository clientRegistrationRepo,
			ServerOAuth2AuthorizedClientRepository authorizedClientRepo) {
		
		ServerOAuth2AuthorizedClientExchangeFilterFunction filter = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
				clientRegistrationRepo, authorizedClientRepo);

		filter.setDefaultClientRegistrationId("keycloak");

		return WebClient.builder().filter(filter).build();
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
				// log.info("seafood festival - Request: {}",
				// clientRequest.headers()
				// .forEach((name, values) -> values.forEach(value -> log.info("tracey ross
				// {}={}", name, value)));
			}
			return Mono.just(clientResponse);
		});
	}
}
