package com.inventory.consumer.service.impl;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import com.inventory.consumer.entity.InventoryEvent;
import com.inventory.consumer.mapper.InventoryEventMapper;
import com.inventory.consumer.repo.InventoryEventRepo;
import com.inventory.consumer.service.InventoryEventService;
import com.sandbox.util.SandboxUtils;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InventoryEventServiceImpl implements InventoryEventService {
	private InventoryEventRepo inventoryEventRepo;

	public InventoryEventServiceImpl(InventoryEventRepo inventoryEventRepo) {
		this.inventoryEventRepo = inventoryEventRepo;
	}

	
	public void processConsumerRecord(ConsumerRecord<Integer, String> consumerRecord) {
		log.debug("processConsumerRecord - Persisting Inventory Event To DB consumerRecord={}", consumerRecord.value());

		InventoryEvent consumersInventoryEvent = InventoryEventMapper.buildInventoryEvent(consumerRecord);

		SandboxUtils.prettyPrintObjectToJson(consumersInventoryEvent);

		InventoryEvent savedResponse = new InventoryEvent();
		if (consumersInventoryEvent.getEventType().toString().equalsIgnoreCase("new")) {
			savedResponse = saveInventoryEvent(consumersInventoryEvent);
		} else if (consumersInventoryEvent.getEventType().toString().equalsIgnoreCase("update")) {
			savedResponse = inventoryEventRepo.findById(consumersInventoryEvent.getEventId())
					.filter(ObjectUtils::isNotEmpty)
					.map(origInventoryEvent -> {
						return InventoryEventMapper.updateOrigItemEventWithUpdatedItemEvent(consumersInventoryEvent, origInventoryEvent);
					})
					.filter(ObjectUtils::isNotEmpty)
					.map(this::saveInventoryEvent)
					.orElse(new InventoryEvent());
		} else {
			log.debug(
					"processConsumerRecord - Unable to Persist Inventory Event To DB. Invalid Event Type eventType={} consumersInventoryEvent={}",
					consumersInventoryEvent.getEventType(),
					consumersInventoryEvent);
		}

		SandboxUtils.prettyPrintObjectToJson(savedResponse);
	}


	@Transactional
	public InventoryEvent saveInventoryEvent(InventoryEvent consumersInventoryEvent) {
		log.debug("saveInventoryEvent - Persisting Inventory Event To DB consumersInventoryEvent={}",
				consumersInventoryEvent);

		return inventoryEventRepo.save(consumersInventoryEvent);
	}
}
