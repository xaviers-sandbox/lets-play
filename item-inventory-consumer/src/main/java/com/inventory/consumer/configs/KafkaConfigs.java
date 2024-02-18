package com.inventory.consumer.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

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
	
//	public DefaultErrorHandler errorHandler() {
//		String fbo = new FixedBackOff(1000L, 2);
//		return new DefaultErrorHandler();
//	}

	//manually manage the offset
	@Bean
	@ConditionalOnMissingBean(name = "kafkaListenerContainerFactory")
	ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(
			ConcurrentKafkaListenerContainerFactoryConfigurer configurer, ConsumerFactory<Object, Object> kafkaConsumerFactory) {
		ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<Object, Object>();
		configurer.configure(factory, kafkaConsumerFactory);
		
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
		
		//configure the consumer to process concurrently 
		// this is best practice if the app isnt being hosted in the cloud
		factory.setConcurrency(3);
		return factory;
	}
}