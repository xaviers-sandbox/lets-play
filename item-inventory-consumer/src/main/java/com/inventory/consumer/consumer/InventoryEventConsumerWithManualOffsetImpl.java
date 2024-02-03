package com.inventory.consumer.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.inventory.producer.model.InventoryEventDTO;
import com.sandbox.util.SandboxUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InventoryEventConsumerWithManualOffsetImpl implements AcknowledgingMessageListener<Integer, String> {

	@Override
	@KafkaListener(topics = "${spring.kafka.topic}", id = "${spring.kafka.topic}")
	// , autoStartup="${spring.kafka.auto-startup}")
	// @KafkaListener(topics =
	// "${kafka.consumer.topics}",autoStartup="${kafka.consumer.autoStartup}",
	// id="${kafka.consumer.id}")
	public void onMessage(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment) {
		log.debug("onMessage - On Message Started for topic={} - partition={} - offset={}",
				consumerRecord.topic(),
				consumerRecord.partition(),
				consumerRecord.offset());

		acknowledgment.acknowledge();

		InventoryEventDTO inventoryEvent = (InventoryEventDTO) SandboxUtils.mapStringToObject(consumerRecord.value(),
				InventoryEventDTO.class);

		SandboxUtils.prettyPrintObjectToJson(inventoryEvent);
	}
}
