package com.inventory.producer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfigs {
	
	public String topicName;

	public int partitions;

	public int replicas;

	public KafkaConfigs(@Value("${spring.kafka.topic-name}") String topicName,
			@Value("${spring.kafka.partitions}") int partitions, @Value("${spring.kafka.replicas}") int replicas) {
		this.topicName = topicName;
		this.partitions = partitions;
		this.replicas = replicas;
	}

	@Bean
	NewTopic InventoryEvent() {
		return TopicBuilder.name(topicName).partitions(partitions).replicas(replicas).build();
	}
}
