package com.inventory.consumer.consumer;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.TestPropertySource;

import com.inventory.consumer.entity.InventoryEvent;
import com.inventory.consumer.repo.InventoryEventRepo;
import com.inventory.consumer.service.impl.InventoryEventServiceImpl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@EmbeddedKafka(topics = { "${spring.kafka.topic-name}" }, partitions = 3)
@TestPropertySource(properties = { "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
		"spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}" })
@Slf4j
public class InventoryEventConsumerWithManualOffsetImplINT {

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	@Autowired
	private KafkaTemplate<Integer, String> kafkaTemplate;

	@Autowired
	private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

	@SpyBean
	private InventoryEventConsumerWithManualOffsetImpl inventoryEventConsumerSpy;

	@SpyBean
	private InventoryEventServiceImpl InventoryEventServiceImplSpy;

	@Autowired
	private InventoryEventRepo inventoryEventRepo;

	@BeforeEach
	public void configureConsumerBeforeTest() {
		MockitoAnnotations.openMocks(this);

		for (MessageListenerContainer msgListenerContainer : kafkaListenerEndpointRegistry.getListenerContainers()) {
			ContainerTestUtils.waitForAssignment(msgListenerContainer, embeddedKafkaBroker.getPartitionsPerTopic());

		}

		kafkaTemplate.setDefaultTopic("inventory-events");
	}

	@AfterEach
	@Transactional
	public void deleteAllInserts() {

		//inventoryEventRepo.deleteAll();
	}
	
	@Test
	void deleteAllData() {
		inventoryEventRepo.deleteAll();
	
	}

	@SuppressWarnings("unchecked")
	@Test
	void publishNewInventoryItemEvent() throws InterruptedException, ExecutionException {
		String inventoryEventStr = "{\"eventId\":null,\"eventType\":\"NEW\",\"item\":{\"itemId\":null,\"name\":\"Intelligent Silk Clock\",\"price\":66.8,\"quantity\":9}}";

		kafkaTemplate.sendDefault(inventoryEventStr).get();

		CountDownLatch cdl = new CountDownLatch(1);
		cdl.await(5, TimeUnit.SECONDS);

		// verify(inventoryEventConsumerSpy).onMessage(any(ConsumerRecord.class),
		// any(Acknowledgment.class));
		// verify(InventoryEventServiceImplSpy).processConsumerRecord(any());

		List<InventoryEvent> inventoryEventList = inventoryEventRepo.findAll();
		System.out.println("feb 2024 - inventoryEventList=" + inventoryEventList);

	}

}
