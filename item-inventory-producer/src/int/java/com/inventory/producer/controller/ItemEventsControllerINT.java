package com.inventory.producer.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.inventory.producer.model.InventoryEventDTO;
import com.inventory.producer.model.record.InventoryEventRecord;
import com.inventory.producer.model.response.InventoryEventDTOResponse;
import com.sandbox.util.SandboxUtils;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@EmbeddedKafka(topics = "inventory-events"
//, brokerProperties = { "log.dir=/Users/craps/git/lets-play/item-inventory-producer/src/int/kafka-logs" }
)
@Slf4j
@TestPropertySource(locations = "classpath:application.yml", properties = {
		"spring.kafka.admin.bootstrap.servers=${spring.embedded.kafka.brokers}",
		"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}" })
public class ItemEventsControllerINT {
	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	private Consumer<Integer, String> kafkaConsumer;

	private static final String INVENTORIES_EVENTS_URL = "/v1/inventory-events/app";

	private static final int TEST_LIST_SIZE = 2;

	@BeforeEach
	void initTest() throws IOException {
		Map<String, Object> kafkaConfigs = new HashMap<>(
				KafkaTestUtils.consumerProps("group1", "true", embeddedKafkaBroker));
		kafkaConfigs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

		kafkaConsumer = new DefaultKafkaConsumerFactory<>(kafkaConfigs, new IntegerDeserializer(),
				new StringDeserializer()).createConsumer();

		embeddedKafkaBroker.consumeFromAllEmbeddedTopics(kafkaConsumer);
	}

	@AfterEach
	void cleanUp() {
		kafkaConsumer.close();
	}

	@Test
	void createNewRecords_Test() {
		log.debug("\n\nInitializing Test Data - size={}", TEST_LIST_SIZE);
	
		
		webTestClient.post()
				.uri(INVENTORIES_EVENTS_URL + "/init-test-data/{size}", TEST_LIST_SIZE)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(InventoryEventDTOResponse.class)
				.consumeWith(response -> {
					InventoryEventDTOResponse inventoryEventDTOResponse = response.getResponseBody();

					List<InventoryEventDTO> responseList = inventoryEventDTOResponse.getItemInventoryDTOList();
					assertNotNull(responseList);
					assertEquals(TEST_LIST_SIZE, responseList.size());

					responseList.forEach(i -> {
						assertNotNull(i.getEventId());
						assertNotNull(i.getEventType());
						assertNotNull(i.getItemDTO());
					});
				})
				.value(SandboxUtils::prettyPrintObjectToJson);

		ConsumerRecords<Integer, String> consumerRecords = KafkaTestUtils.getRecords(kafkaConsumer);

		assertEquals(TEST_LIST_SIZE, consumerRecords.count());

		consumerRecords.forEach(r -> {
			InventoryEventRecord inventoryEventRecord = (InventoryEventRecord) SandboxUtils.mapStringToObject(r.value(),
					InventoryEventRecord.class);
			assertNotNull(inventoryEventRecord);
			assertNotNull(inventoryEventRecord.eventId());
			assertNotNull(inventoryEventRecord.eventType());
			assertNotNull(inventoryEventRecord.item());
		});
	}

	// @Test
	public void testingMapper() {
		String value1 = "{\"eventId\":5811136,\"eventType\":\"UPDATE\",\"item\":{\"id\":5671990,\"name\":\"Mediocre Copper Bottle\",\"price\":89.8,\"quantity\":8}}";
		String value2 = "{\"eventId\":7031164,\"eventType\":\"NEW\",\"item\":{\"id\":8464071,\"name\":\"Lightweight Granite Bench\",\"price\":62.13,\"quantity\":8}}";
		String junk = "osidfjaoijfaoifj";

		System.out.println("\n\n\ntestingMapper");
		Arrays.asList(value1, value2, junk).forEach(i -> {
			InventoryEventRecord tmp = (InventoryEventRecord) SandboxUtils.mapStringToObject(i, InventoryEventRecord.class);
			System.out.println(tmp);
		});
	}
}
