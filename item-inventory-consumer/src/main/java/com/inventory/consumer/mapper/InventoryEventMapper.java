package com.inventory.consumer.mapper;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.inventory.consumer.entity.InventoryEvent;
import com.inventory.consumer.entity.KafkaDetails;
import com.sandbox.util.SandboxUtils;

public class InventoryEventMapper {

	public static InventoryEvent buildInventoryEvent(ConsumerRecord<Integer, String> consumerRecord) {
		
		System.out.println("consumerRecord=" + consumerRecord.value());
		
		InventoryEvent inventoryEvent = (InventoryEvent) SandboxUtils.mapStringToObject(consumerRecord.value(),
				InventoryEvent.class);

		return mapKafkaDetailsToInventoryEvent(inventoryEvent, consumerRecord);
	}
	
	
	public static InventoryEvent mapKafkaDetailsToInventoryEvent(InventoryEvent inventoryEvent,
			ConsumerRecord<Integer, String> consumerRecord) {
		System.out.println("happy bday null or nah = " + inventoryEvent.getEventId());
		
		KafkaDetails kafkaDetails = KafkaDetails.builder()
				//.kafkaDetailsId(inventoryEvent.getEventId())
				.origTopicName(consumerRecord.topic())
				.origPartition(consumerRecord.partition())
				.origOffset(consumerRecord.offset())
				.inventoryEventKafka(inventoryEvent)
				.build();
		
		//inventoryEvent.getItem().setItemId(inventoryEvent.getEventId());
		inventoryEvent.getItem().setInventoryEventItem(inventoryEvent);
		inventoryEvent.setKafkaDetails(kafkaDetails);
		

		return inventoryEvent;
	}
	
	public static InventoryEvent updateOrigItemEventWithUpdatedItemEvent(InventoryEvent consumersInventoryEvent,
			InventoryEvent origInventoryEvent) {
		origInventoryEvent.setEventType(consumersInventoryEvent.getEventType());
		
		origInventoryEvent.setItem(consumersInventoryEvent.getItem());
		
		KafkaDetails updatedKafkaDetails = KafkaDetails.builder()
			//	.id(consumersInventoryEvent.getKafkaDetails().getId())
				.origTopicName(consumersInventoryEvent.getKafkaDetails().getOrigTopicName())
				.origPartition(consumersInventoryEvent.getKafkaDetails().getOrigPartition())
				.origOffset(consumersInventoryEvent.getKafkaDetails().getOrigOffset())
				.createdOn(consumersInventoryEvent.getKafkaDetails().getCreatedOn())
				.build();

		origInventoryEvent.setKafkaDetails(updatedKafkaDetails);

		return origInventoryEvent;
	}
}
