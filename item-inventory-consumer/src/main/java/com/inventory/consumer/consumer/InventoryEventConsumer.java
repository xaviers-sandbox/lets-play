package com.inventory.consumer.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.inventory.producer.model.InventoryEventDTO;
import com.sandbox.util.SandboxUtils;

import lombok.extern.slf4j.Slf4j;

//@Component
@Slf4j
public class InventoryEventConsumer {

	//@KafkaListener(topics = "${spring.kafka.topic}", id = "${spring.kafka.topic}")
	// , autoStartup="${spring.kafka.auto-startup}")
	// @KafkaListener(topics =
	// "${kafka.consumer.topics}",autoStartup="${kafka.consumer.autoStartup}",
	// id="${kafka.consumer.id}")
	public void consumeMessages(ConsumerRecord<Integer, String> consumerRecord) {
		log.debug("consumeMessages - On Message Started for topic={} - partition={} - offset={}",
				consumerRecord.topic(),
				consumerRecord.partition(),
				consumerRecord.offset());

		InventoryEventDTO inventoryEvent = (InventoryEventDTO) SandboxUtils.mapStringToObject(consumerRecord.value(),
				InventoryEventDTO.class);
		
		SandboxUtils.prettyPrintObjectToJson(inventoryEvent);
	}
}
