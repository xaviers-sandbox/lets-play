package com.inventory.producer.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inventory.producer.mapper.InventoryEventMapper;
import com.inventory.producer.model.InventoryEventDTO;
import com.inventory.producer.model.InventoryItemDTORequest;
import com.inventory.producer.model.ResponseDTO;
import com.inventory.producer.producer.InventoryEventProducer;
import com.inventory.producer.record.InventoryEvent;
import com.inventory.producer.service.InventoryEventService;
import com.inventory.producer.util.InventoryEventUtils;

@Service
public class InventoryEventServiceImpl implements InventoryEventService {
	private InventoryEventProducer inventoryEventProducer;

	private String topicName;

	public InventoryEventServiceImpl(InventoryEventProducer inventoryEventProducer,
			@Value("${spring.kafka.topic-name}") String topicName) {
		this.inventoryEventProducer = inventoryEventProducer;
		this.topicName = topicName;
	}

	@Override
	public ResponseEntity<ResponseDTO> addNewMockItems(Integer size) {
		List<InventoryEvent> itemEventsList = InventoryEventUtils.generateInventoryEventList(size);

		itemEventsList.forEach(inventoryEvent -> {
			inventoryEventProducer.sendEventToTopicAsyncWithProducerRecord(
					InventoryEventMapper.buildProducerRecord(inventoryEvent, topicName));
		});

		List<InventoryEventDTO> inventoryEventDTOList = itemEventsList.stream()
				.map(InventoryEventMapper::buildInventoryEventDTO)
				.collect(Collectors.toList());

		ResponseDTO responseDTO = InventoryEventMapper.buildResponseDTO(inventoryEventDTOList);

		return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
	}

	@Override
	public ResponseEntity<ResponseDTO> addNewItemInventory(InventoryItemDTORequest newInventoryItemDTORequest) {

		InventoryEvent inventoryEvent = InventoryEventMapper
				.mapInventoryItemDTORequestToInventoryEvent(newInventoryItemDTORequest);

		inventoryEventProducer.sendEventToTopicAsyncWithProducerRecord(
				InventoryEventMapper.buildProducerRecord(inventoryEvent, topicName));

		InventoryEventDTO inventoryEventDTO = InventoryEventMapper.buildInventoryEventDTO(inventoryEvent);

		ResponseDTO responseDTO = InventoryEventMapper.buildResponseDTO(Arrays.asList(inventoryEventDTO));

		return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
	}

	@Override
	public ResponseEntity<ResponseDTO> updateItemInventory(Integer eventId,
			InventoryItemDTORequest updatedInventoryItemDTORequest) {
		
		InventoryEvent inventoryEvent = InventoryEventMapper
				.mapUpdatedInventoryItemDTORequestToInventoryEvent(eventId, updatedInventoryItemDTORequest);
		
		return null;
	}

}
