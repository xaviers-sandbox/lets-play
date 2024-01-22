package com.item.inventory.config;

import org.redisson.api.RedissonClient;

//@Configuration
public class RedissonCacheConfig {
	private RedissonClient redissonClient;

	private final String MAP_KEY = "itemInventoryEntity";
//
//	@Bean
//	CacheManager cacheManager(RedissonClient redissonClient) {
//
//		// RedisSerializer<Object> serializer = new
//		// JdkSerializationRedisSerializer(getClass().getClassLoader());
//		// redissonClient.seri
//		// redissonClient.setDefaultSerializer(serializer);
//		return new RedissonSpringCacheManager(redissonClient);
//	}
//
//	public RedissonClient getRedissonClient() {
//		if (ObjectUtils.isEmpty(redissonClient)) {
//			Config config = new Config();
//			config.useSingleServer().setAddress("redis://127.0.0.1:6379");
//
//			redissonClient = Redisson.create(config);
//		}
//
//		return redissonClient;
//	}
//
//	// @Bean
//	public RedissonReactiveClient getRedissonReactiveClient() {
//		return getRedissonClient().reactive();
//	}
}
