package com.inventory.consumer.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.inventory.consumer.service.InventoryEventService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InventoryEventConsumerWithManualOffsetImpl implements AcknowledgingMessageListener<String, String> {
	
	private InventoryEventService inventoryEventService;

	public InventoryEventConsumerWithManualOffsetImpl(InventoryEventService inventoryEventService) {
		this.inventoryEventService = inventoryEventService;
	}

	@Override
	@KafkaListener(topics = "${spring.kafka.topic-name}", id = "${spring.kafka.consumer.group-id}", 
	autoStartup="true")
	public void onMessage(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
		log.debug("onMessage - On Message Started for topic={} - partition={} - offset={}",
				consumerRecord.topic(),
				consumerRecord.partition(),
				consumerRecord.offset());

		acknowledgment.acknowledge();

		inventoryEventService.processConsumerRecord(consumerRecord);
	}
}
