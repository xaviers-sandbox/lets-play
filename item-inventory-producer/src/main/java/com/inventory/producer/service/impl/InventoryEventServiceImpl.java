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
import com.inventory.producer.model.record.InventoryEventRecord;
import com.inventory.producer.model.request.InventoryEventDTORequest;
import com.inventory.producer.model.response.ResponseDTO;
import com.inventory.producer.producer.InventoryEventProducer;
import com.inventory.producer.service.InventoryEventService;
import com.item.inventory.test.utils.ItemInventoryTestUtils;
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
		List<InventoryEventRecord> inventoryEventRecordList = ItemInventoryTestUtils.generateInventoryEventRecordList(size);

		inventoryEventRecordList.forEach(inventoryEventRecord -> {
			inventoryEventProducer.sendEventToTopicAsyncWithProducerRecord(
					InventoryEventMapper.buildProducerRecord(inventoryEventRecord, topicName));
		});

		List<InventoryEventDTO> inventoryEventDTOList = inventoryEventRecordList.stream()
				.map(InventoryEventMapper::mapInventoryEventRecordToInventoryEventDTO)
				.collect(Collectors.toList());
		
		ResponseDTO responseDTO = InventoryEventMapper.buildResponseDTO(inventoryEventDTOList);

		return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
	}

	@Override
	public ResponseEntity<ResponseDTO> addNewItemInventory(InventoryEventDTORequest newInventoryEventDTORequest) {

		InventoryEventRecord inventoryEventRecord = InventoryEventMapper
				.mapInventoryEventDTORequestToInventoryEventRecord(newInventoryEventDTORequest);

		SandboxUtils.prettyPrintObjectToJson(inventoryEventRecord);

		inventoryEventProducer.sendEventToTopicAsyncWithProducerRecord(
				InventoryEventMapper.buildProducerRecord(inventoryEventRecord, topicName));

		InventoryEventDTO inventoryEventDTO = InventoryEventMapper.mapInventoryEventRecordToInventoryEventDTO(inventoryEventRecord);

		ResponseDTO responseDTO = InventoryEventMapper.buildResponseDTO(Arrays.asList(inventoryEventDTO));

		return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
	}

	@Override
	public ResponseEntity<ResponseDTO> updateItemInventory(String eventId,
			InventoryEventDTORequest updatedInventoryItemDTORequest) {
		
		updatedInventoryItemDTORequest.setEventId(eventId);

		InventoryEventRecord updatedInventoryEvent = InventoryEventMapper.mapUpdatedInventoryEventDTORequestToInventoryEventRecord(eventId,
				updatedInventoryItemDTORequest);
		
		SandboxUtils.prettyPrintObjectToJson(updatedInventoryEvent);
		
		inventoryEventProducer.sendEventToTopicAsyncWithProducerRecord(
				InventoryEventMapper.buildProducerRecord(updatedInventoryEvent, topicName));
		
		InventoryEventDTO updatedInventoryEventDTO = InventoryEventMapper.mapInventoryEventRecordToInventoryEventDTO(updatedInventoryEvent);
		
		ResponseDTO responseDTO = InventoryEventMapper.buildResponseDTO(Arrays.asList(updatedInventoryEventDTO));

		return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
	}
}
