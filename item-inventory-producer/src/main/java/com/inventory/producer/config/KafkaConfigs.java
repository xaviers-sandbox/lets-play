package com.inventory.producer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfigs {
	@Value("${spring.kafka.topic}")
	public String topicName;
	
	@Value("${spring.kafka.partitions}")
	public int partitions;
	
	@Value("${spring.kafka.replicas}")
	public int replicas;
	
	@Bean
	NewTopic InventoryEvent() {
		return TopicBuilder.name(topicName).partitions(partitions).replicas(replicas).build();
	}
}
