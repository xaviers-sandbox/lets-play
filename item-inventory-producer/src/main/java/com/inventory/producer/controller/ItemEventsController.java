package com.inventory.producer.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.producer.mapper.ItemProducerMapper;
import com.inventory.producer.producer.InventoryEventsProducer;
import com.inventory.producer.record.InventoryEvent;
import com.inventory.producer.util.InventoryProducerUtils;
import com.sandbox.util.SandboxUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("v1/inventory-events")
@Slf4j
public class ItemEventsController {

	private InventoryEventsProducer inventoryEventsProducer;

	@Value("${spring.kafka.topic}")
	private String topicName;

	public ItemEventsController(InventoryEventsProducer inventoryEventsProducer) {
		this.inventoryEventsProducer = inventoryEventsProducer;

	}

	@PostMapping("/init-test-data/{size}")
	public ResponseEntity<List<InventoryEvent>> postItemEvents(@PathVariable String size) {
		if (!StringUtils.isNumeric(size))
			return ResponseEntity.badRequest().build();

		log.debug("postItemEvents size={}",size);

		List<InventoryEvent> itemEventsList = InventoryProducerUtils.generateInventoryEventList(Integer.valueOf(size));

		SandboxUtils.prettyPrintObjectToJson(itemEventsList);

		itemEventsList.forEach(i -> {
			inventoryEventsProducer.sendEventToTopicAsyncWithProducerRecord(ItemProducerMapper.buildProducerRecord(i, topicName));
		});

		return ResponseEntity.status(HttpStatus.CREATED).body(itemEventsList);
	}




}
