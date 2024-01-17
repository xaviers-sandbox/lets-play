package com.item.inventory.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@AutoConfigureWebTestClient
@WebFluxTest(controllers = MonoAndFluxController.class)
public class MonoAndFluxControllerTest {
	@Autowired
	private WebTestClient webTestClient;

	@Test
	public void getFluxTest() {

		webTestClient.get()
				.uri("/flux")
				.exchange()
				// .expectHeader()
				// .contentType(MediaType.APPLICATION_JSON)
				.expectStatus()
				.is2xxSuccessful()
				.expectBodyList(Integer.class)
				.hasSize(3);
	}

	@Test
	public void getFluxTest2() {

		Flux<Integer> flux = webTestClient.get()
				.uri("/flux")
				.exchange()
				// .expectHeader()
				// .contentType(MediaType.APPLICATION_JSON)
				.expectStatus()
				.is2xxSuccessful()
				.returnResult(Integer.class)
				.getResponseBody();

		StepVerifier.create(flux).expectNextCount(3).verifyComplete();
	}

	@Test
	public void getFluxTest3() {

		webTestClient.get()
				.uri("/flux")
				.exchange()
				// .expectHeader()
				// .contentType(MediaType.APPLICATION_JSON)
				.expectStatus()
				.is2xxSuccessful()
				.expectBodyList(Integer.class)
				.consumeWith(i -> {
					List<Integer> tmp = i.getResponseBody();

					assertTrue(tmp.size() == 3);
				});
	}

	@Test
	public void getMonoTest() {

		webTestClient.get()
				.uri("/mono")
				.exchange()
				// .expectHeader()
				// .contentType(MediaType.APPLICATION_JSON)
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(String.class)
				.isEqualTo("Hello World From Mono");
	}

	@Test
	public void getMonoTest2() {

		webTestClient.get()
				.uri("/mono")
				.exchange()
				// .expectHeader()
				// .contentType(MediaType.APPLICATION_JSON)
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(String.class)
				.consumeWith(i -> {
					String tmp = i.getResponseBody();

					assertTrue(tmp.equalsIgnoreCase("Hello World From Mono"));
				});
	}

	@Test
	public void getMonoTest3() {

		webTestClient.get()
				.uri("/mono")
				.exchange()
				// .expectHeader()
				// .contentType(MediaType.APPLICATION_JSON)
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(String.class)
				.isEqualTo("Hello World From Mono");
	}
}
