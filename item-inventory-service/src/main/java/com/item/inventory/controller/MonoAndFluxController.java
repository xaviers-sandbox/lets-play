package com.item.inventory.controller;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class MonoAndFluxController {

	@GetMapping("flux")
	public Flux<Integer> flux() {
		log.debug("brownies flux");

		return Flux.just(2, 4, 6);
	}

	@GetMapping("mono")
	public Mono<String> mono() {
		log.debug("red velvet mono");

		return Mono.just("Hello World From Mono");
	}

	@GetMapping(value = "stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Long> stream() {
		return Flux.interval(Duration.ofSeconds(2));
	}
}
