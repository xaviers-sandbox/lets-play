package com.inventory.consumer.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface InventoryEventService {

	void processConsumerRecord(ConsumerRecord<String, String> consumerRecord);

}
