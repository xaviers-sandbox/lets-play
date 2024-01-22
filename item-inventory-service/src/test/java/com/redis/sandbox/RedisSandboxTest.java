package com.redis.sandbox;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.redisson.api.RAtomicLongReactive;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.item.inventory.ItemInventoryApp;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
@ContextConfiguration(classes = ItemInventoryApp.class)
public class RedisSandboxTest {
	@Autowired
	private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

	@Autowired
	private ReactiveStringRedisTemplate template;

	@Autowired
	private RedissonReactiveClient redissonReactiveClient;

	// 10 ms for 500k records
	@Test
	@RepeatedTest(3)
	public void springbootStarterDataRedisReactivePerformanceTest() {
		log.debug("\n\nspringbootStarterDataRedisReactivPerformanceTest");
		ReactiveValueOperations<String, String> valueOps = reactiveStringRedisTemplate.opsForValue();

		Long startTime = System.currentTimeMillis();

		Mono<Void> tmp = Flux.range(1, 500_000).flatMap(i -> valueOps.increment("user:1:visit")).then();

		StepVerifier.create(tmp).verifyComplete();

		Long endTime = System.currentTimeMillis();

		long duration = endTime - startTime;

		log.debug("500_000 records duration={}s", TimeUnit.MILLISECONDS.toSeconds(duration));
	}

	@Test
	@RepeatedTest(3)
	void redissonPerformanceTest() {
		log.debug("\n\nredissonPerformanceTest");

		RAtomicLongReactive atomicLong = redissonReactiveClient.getAtomicLong("user:2:visit");

		long startTime = System.currentTimeMillis();

		Mono<Void> tmp = Flux.range(1, 500_000).flatMap(i -> atomicLong.incrementAndGet()).then();

		StepVerifier.create(tmp).verifyComplete();

		long endTime = System.currentTimeMillis();

		long duration = endTime - startTime;

		log.debug("500_000 records duration={}s", TimeUnit.MILLISECONDS.toSeconds(duration));
	}

//	@RepeatedTest(3)
//	void springDataRedisTest() {
//		ReactiveValueOperations<String, String> valueOperations = this.template.opsForValue();
//		long before = System.currentTimeMillis();
//		Mono<Void> mono = Flux.range(1, 500_000)
//				.flatMap(i -> valueOperations.increment("user:1:visit")) // incr
//				.then();
//		StepVerifier.create(mono).verifyComplete();
//		long after = System.currentTimeMillis();
//		System.out.println((after - before) + " ms");
//	}

}
