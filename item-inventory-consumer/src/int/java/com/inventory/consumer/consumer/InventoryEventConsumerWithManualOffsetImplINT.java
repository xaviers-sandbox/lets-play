package com.inventory.consumer.consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.TestPropertySource;

import com.inventory.consumer.entity.InventoryEvent;
import com.inventory.consumer.records.InventoryEventRecord;
import com.inventory.consumer.repo.InventoryEventRepo;
import com.inventory.consumer.service.impl.InventoryEventServiceImpl;
import com.inventory.consumer.utils.InventoryConsumerIntUtils;
import com.inventory.producer.enums.InventoryEventType;
import com.sandbox.util.SandboxUtils;

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
	private KafkaTemplate<String, String> kafkaTemplate;

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
	public void cleanUpTests() {

		// inventoryEventRepo.deleteAll();
		kafkaTemplate.flush();
		kafkaTemplate.destroy();
	}

	// @Test
	@Transactional
	void deleteAllData() {
		inventoryEventRepo.deleteAll();

	}

	@Test
	@Transactional
	void deleteInventoryEventById() {
		inventoryEventRepo.deleteById("id_is_a_string_now");

	}

	@SuppressWarnings("unchecked")
	@Transactional
	@RepeatedTest(3)
	void addNewInventoryItemEvent_test() throws InterruptedException, ExecutionException {
		log.debug("addNewInventoryItemEvent_test");

		InventoryEventRecord inventoryEventRecord = InventoryConsumerIntUtils.buildMockInventoryEventRecord();

		SandboxUtils.prettyPrintObjectToJson(inventoryEventRecord);

		String newItemStr = SandboxUtils.convertObjectToString(inventoryEventRecord);

		CompletableFuture<SendResult<String, String>> sendResult = kafkaTemplate.sendDefault(newItemStr);

		sendResult.whenComplete((result, ex) -> {
			if (ObjectUtils.isNotEmpty(ex))
				fail("addNewInventoryItemEvent_test Failed - message=" + ex.getLocalizedMessage());

			verify(inventoryEventConsumerSpy).onMessage(any(ConsumerRecord.class), any(Acknowledgment.class));
			verify(InventoryEventServiceImplSpy).processConsumerRecord(any());

			InventoryEvent newInventoryEvent = inventoryEventRepo.findById(inventoryEventRecord.eventId()).orElse(null);

			assertNotNull(newInventoryEvent);
			assertEquals(inventoryEventRecord.eventId(), newInventoryEvent.getEventId());
			assertEquals(inventoryEventRecord.eventType(), newInventoryEvent.getEventType());

			assertNotNull(newInventoryEvent.getItem());
			assertEquals(inventoryEventRecord.item().itemId(), newInventoryEvent.getItem().getItemId());
			assertEquals(inventoryEventRecord.item().name(), newInventoryEvent.getItem().getName());
			assertEquals(inventoryEventRecord.item().price(), newInventoryEvent.getItem().getPrice());
			assertEquals(inventoryEventRecord.item().quantity(), newInventoryEvent.getItem().getQuantity());

			assertNotNull(newInventoryEvent.getKafkaDetails());
			assertNotNull(newInventoryEvent.getKafkaDetails().getTopicName());
		});

	}

	@SuppressWarnings("unchecked")
	@Transactional
	@RepeatedTest(3)
	void updateInventoryItemEvent_test() throws InterruptedException, ExecutionException {
		log.debug("updateInventoryItemEvent_test");
		InventoryEventRecord inventoryEventRecordMock = InventoryConsumerIntUtils.buildMockInventoryEventRecord();

		SandboxUtils.prettyPrintObjectToJson(inventoryEventRecordMock);

		String newItemStr = SandboxUtils.convertObjectToString(inventoryEventRecordMock);

		CompletableFuture<SendResult<String, String>> sendResult = kafkaTemplate.sendDefault(newItemStr);

		sendResult.whenComplete((result, ex) -> {
			
			if (ObjectUtils.isNotEmpty(ex))
				fail("updateInventoryItemEvent_test Failed - message=" + ex.getLocalizedMessage());

			verify(inventoryEventConsumerSpy).onMessage(any(ConsumerRecord.class), any(Acknowledgment.class));
			verify(InventoryEventServiceImplSpy).processConsumerRecord(any());

			InventoryEvent origInventoryEvent = inventoryEventRepo.findById(inventoryEventRecordMock.eventId())
					.orElse(null);

			SandboxUtils.prettyPrintObjectToJson(origInventoryEvent);

			assertNotNull(origInventoryEvent);
			assertEquals(inventoryEventRecordMock.eventId(), origInventoryEvent.getEventId());
			assertEquals(inventoryEventRecordMock.eventType(), origInventoryEvent.getEventType());

			assertNotNull(origInventoryEvent.getItem());
			assertEquals(inventoryEventRecordMock.item().itemId(), origInventoryEvent.getItem().getItemId());
			assertEquals(inventoryEventRecordMock.item().name(), origInventoryEvent.getItem().getName());
			assertEquals(inventoryEventRecordMock.item().price(), origInventoryEvent.getItem().getPrice());
			assertEquals(inventoryEventRecordMock.item().quantity(), origInventoryEvent.getItem().getQuantity());

			assertNotNull(origInventoryEvent.getKafkaDetails());
			assertNotNull(origInventoryEvent.getKafkaDetails().getTopicName());

			origInventoryEvent.setEventType(InventoryEventType.UPDATED);
			origInventoryEvent.getItem().setName("valentines arrangement");
			origInventoryEvent.getItem().setPrice(125.9);
			origInventoryEvent.getItem().setQuantity(50);

			InventoryEvent updatedInventoryEvent = inventoryEventRepo.save(origInventoryEvent);

			SandboxUtils.prettyPrintObjectToJson(updatedInventoryEvent);

			assertNotNull(updatedInventoryEvent);
			assertEquals(updatedInventoryEvent.getEventId(), origInventoryEvent.getEventId());
			assertEquals(updatedInventoryEvent.getEventType(), origInventoryEvent.getEventType());

			assertNotNull(updatedInventoryEvent.getItem());
			assertEquals(updatedInventoryEvent.getItem().getItemId(), origInventoryEvent.getItem().getItemId());
			assertEquals(updatedInventoryEvent.getItem().getName(), origInventoryEvent.getItem().getName());
			assertEquals(updatedInventoryEvent.getItem().getPrice(), origInventoryEvent.getItem().getPrice());
			assertEquals(updatedInventoryEvent.getItem().getQuantity(), origInventoryEvent.getItem().getQuantity());

			assertNotNull(origInventoryEvent.getKafkaDetails());
			assertNotNull(origInventoryEvent.getKafkaDetails().getTopicName());
			assertEquals(updatedInventoryEvent.getKafkaDetails().getTopicName(),
					origInventoryEvent.getKafkaDetails().getTopicName());
		});
	}
}
