package com.inventory.consumer.mapper;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.inventory.consumer.entity.InventoryEvent;
import com.inventory.consumer.entity.Item;
import com.inventory.consumer.entity.KafkaDetails;
import com.inventory.producer.enums.InventoryEventType;
import com.sandbox.util.SandboxUtils;

import net.datafaker.Faker;

public class InventoryEventMapper {
	private static Faker faker;

	public static Faker getFaker() {
		if (ObjectUtils.isEmpty(faker)) {
			faker = new Faker();
		}

		return faker;
	}

	public static InventoryEvent buildInventoryEvent(ConsumerRecord<String, String> consumerRecord) {
		InventoryEvent inventoryEvent = (InventoryEvent) SandboxUtils.mapStringToObject(consumerRecord.value(),
				InventoryEvent.class);

		return mapKafkaDetailsToInventoryEvent(inventoryEvent, consumerRecord);
	}
	
	
	public static InventoryEvent mapKafkaDetailsToInventoryEvent(InventoryEvent inventoryEvent,
			ConsumerRecord<String, String> consumerRecord) {

		KafkaDetails kafkaDetails = KafkaDetails.builder()
				.topicName(consumerRecord.topic())
				.partition(consumerRecord.partition())
				.offset(consumerRecord.offset())
				.inventoryEventKafka(inventoryEvent)
				.build();
		
		inventoryEvent.getItem().setInventoryEventItem(inventoryEvent);
		inventoryEvent.setKafkaDetails(kafkaDetails);
		

		return inventoryEvent;
	}
	
	public static InventoryEvent updateOrigItemEventWithUpdatedItemEvent(InventoryEvent updatedInventoryEvent,
			InventoryEvent origInventoryEvent) {
		
		Item updatedItem = updatedInventoryEvent.getItem();

		
		origInventoryEvent.getItem().setName(updatedItem.getName());
		origInventoryEvent.getItem().setPrice(updatedItem.getPrice());
		origInventoryEvent.getItem().setQuantity(updatedItem.getQuantity());
		
		KafkaDetails updatedKafkaDetails = updatedInventoryEvent.getKafkaDetails();
		
		origInventoryEvent.getKafkaDetails().setPreviousEventType(origInventoryEvent.getEventType());
		origInventoryEvent.getKafkaDetails().setPreviousOffset(origInventoryEvent.getKafkaDetails().getOffset());
		origInventoryEvent.getKafkaDetails().setPreviousPartition(origInventoryEvent.getKafkaDetails().getPartition());
		origInventoryEvent.getKafkaDetails().setPreviousTopicName(origInventoryEvent.getKafkaDetails().getTopicName());
		
		origInventoryEvent.getKafkaDetails().setOffset(updatedKafkaDetails.getOffset());
		origInventoryEvent.getKafkaDetails().setPartition(updatedKafkaDetails.getPartition());
		origInventoryEvent.getKafkaDetails().setTopicName(updatedKafkaDetails.getTopicName());	
		
		origInventoryEvent.setEventType(InventoryEventType.UPDATED);
		
		return origInventoryEvent;
	}
}
