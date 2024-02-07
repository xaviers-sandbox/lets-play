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
import com.inventory.producer.model.InventoryEventDTORequest;
import com.inventory.producer.model.ResponseDTO;
import com.inventory.producer.producer.InventoryEventProducer;
import com.inventory.producer.record.InventoryEvent;
import com.inventory.producer.service.InventoryEventService;
import com.inventory.producer.util.InventoryEventUtils;
import com.sandbox.util.SandboxUtils;

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
				.map(InventoryEventMapper::mapInventoryEventToInventoryEventDTO)
				.collect(Collectors.toList());

		ResponseDTO responseDTO = InventoryEventMapper.buildResponseDTO(inventoryEventDTOList);

		return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
	}

	@Override
	public ResponseEntity<ResponseDTO> addNewItemInventory(InventoryEventDTORequest newInventoryEventDTORequest) {

		InventoryEvent inventoryEvent = InventoryEventMapper
				.mapInventoryEventDTORequestToInventoryEvent(newInventoryEventDTORequest);

		SandboxUtils.prettyPrintObjectToJson(inventoryEvent);

		inventoryEventProducer.sendEventToTopicAsyncWithProducerRecord(
				InventoryEventMapper.buildProducerRecord(inventoryEvent, topicName));

		InventoryEventDTO inventoryEventDTO = InventoryEventMapper.mapInventoryEventToInventoryEventDTO(inventoryEvent);

		ResponseDTO responseDTO = InventoryEventMapper.buildResponseDTO(Arrays.asList(inventoryEventDTO));

		return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
	}

	@Override
	public ResponseEntity<ResponseDTO> updateItemInventory(String eventId,
			InventoryEventDTORequest updatedInventoryItemDTORequest) {
		
		updatedInventoryItemDTORequest.setEventId(eventId);

		InventoryEvent updatedInventoryEvent = InventoryEventMapper.mapUpdatedInventoryItemDTORequestToInventoryEvent(eventId,
				updatedInventoryItemDTORequest);
		
		SandboxUtils.prettyPrintObjectToJson(updatedInventoryEvent);
		
		inventoryEventProducer.sendEventToTopicAsyncWithProducerRecord(
				InventoryEventMapper.buildProducerRecord(updatedInventoryEvent, topicName));
		
		InventoryEventDTO updatedInventoryEventDTO = InventoryEventMapper.mapInventoryEventToInventoryEventDTO(updatedInventoryEvent);
		
		ResponseDTO responseDTO = InventoryEventMapper.buildResponseDTO(Arrays.asList(updatedInventoryEventDTO));

		return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

	}
}
