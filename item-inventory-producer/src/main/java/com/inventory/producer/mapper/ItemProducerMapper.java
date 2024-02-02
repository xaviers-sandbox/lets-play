package com.inventory.producer.mapper;
import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;

import com.inventory.producer.record.InventoryEvent;
import com.sandbox.util.SandboxUtils;

import lombok.Data;

@Data
public class ItemProducerMapper {
	
	public static ProducerRecord<Integer, String> buildProducerRecord(InventoryEvent inventoryEvent, String topicName) {
		Integer key = inventoryEvent.eventId();
		String value = SandboxUtils.convertObjectToString(inventoryEvent);

		return new ProducerRecord<Integer, String>(topicName, null, key, value, buildKafkaHeaders());
	}

	public static Iterable<Header> buildKafkaHeaders() {
		return List.of(new RecordHeader("generic-header", "happyCoding".getBytes()),
				new RecordHeader("dummy-header", "kafkaRocks".getBytes()));

	}
}
