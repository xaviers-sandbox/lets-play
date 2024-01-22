package com.redis.sandbox;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.redisson.api.RedissonReactiveClient;

import com.item.inventory.config.RedissonCacheConfig;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class RootTestClass {
	protected RedissonReactiveClient redissonReactiveClient;

	private final RedissonCacheConfig redissonCacheConfig = new RedissonCacheConfig();

	@BeforeAll
	public void initTestObjects() {
		// redissonReactiveClient = redissonCacheConfig.getRedissonReactiveClient();
	}

	@AfterAll
	public void shutdownTestObjects() {
		// RedissonClient redissonClient = redissonCacheConfig.getRedissonClient();

		// redissonClient.shutdown();

	}

	public void sleep(Long timeInMillie) {
		try {
			Thread.sleep(timeInMillie);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
