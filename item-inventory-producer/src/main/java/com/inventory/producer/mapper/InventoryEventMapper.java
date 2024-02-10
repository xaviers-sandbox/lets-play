package com.inventory.producer.mapper;

import java.util.List;
import java.util.Random;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import com.inventory.producer.enums.InventoryEventType;
import com.inventory.producer.model.InventoryEventDTO;
import com.inventory.producer.model.ItemDTO;
import com.inventory.producer.model.record.InventoryEventRecord;
import com.inventory.producer.model.record.ItemRecord;
import com.inventory.producer.model.request.InventoryEventDTORequest;
import com.inventory.producer.model.request.ItemDTORequest;
import com.inventory.producer.model.response.ErrorDTOResponse;
import com.inventory.producer.model.response.InventoryEventDTOResponse;
import com.inventory.producer.model.response.ResponseDTO;
import com.item.inventory.test.utils.ItemInventoryTestUtils;
import com.sandbox.util.SandboxUtils;

public class InventoryEventMapper {
	public static ProducerRecord<String, String> buildProducerRecord(InventoryEventRecord inventoryEventRecord,
			String topicName) {
		String key = inventoryEventRecord.eventId();
		String value = SandboxUtils.convertObjectToString(inventoryEventRecord);

		return new ProducerRecord<String, String>(topicName, null, key, value, buildKafkaHeaders());
	}

	public static List<Header> buildKafkaHeaders() {

		return List.of(new RecordHeader("generic-header", "happyCoding".getBytes()),
				new RecordHeader("dummy-header", "kafkaRocks".getBytes()));
	}

	public static InventoryEventRecord mapInventoryEventDTORequestToInventoryEventRecord(
			InventoryEventDTORequest inventoryEventDTORequest) {

		InventoryEventType inventoryEventType = InventoryEventType.NEW;

		int randonNum = new Random().nextInt(100000, 10000000);

		if (ObjectUtils.isEmpty(inventoryEventDTORequest.getEventType())) {
			inventoryEventType = (randonNum % 2 == 0) ? InventoryEventType.NEW : InventoryEventType.UPDATE;
		}

		return InventoryEventRecord.builder()
				.eventId(ItemInventoryTestUtils.buildUniqueId())
				.eventType(inventoryEventType)
				.item(mapInventoryEventDTORequestToItemRecord(inventoryEventDTORequest))
				.build();
	}

	public static InventoryEventDTO mapInventoryEventRecordToInventoryEventDTO(
			InventoryEventRecord inventoryEventRecord) {

		return InventoryEventDTO.builder()
				.eventId(inventoryEventRecord.eventId())
				.eventType(inventoryEventRecord.eventType())
				.itemDTO(mapItemRecordToItemDTO(inventoryEventRecord.item()))
				.build();
	}

	public static InventoryEventRecord mapUpdatedInventoryEventDTORequestToInventoryEventRecord(String eventId,
			InventoryEventDTORequest updateInventoryEventDTORequest) {

		return InventoryEventRecord.builder()
				.eventId(eventId)
				.eventType(InventoryEventType.UPDATE)
				.item(mapItemRecordRequestToUpdatedItem(updateInventoryEventDTORequest.getItem()))
				.build();
	}

	public static ItemRecord mapItemRecordRequestToUpdatedItem(
			ItemDTORequest itemDTORequest) {
		
		return ItemRecord.builder()
				.itemId(itemDTORequest.getItemId())
				.name(itemDTORequest.getName())
				.price(itemDTORequest.getPrice())
				.quantity(itemDTORequest.getQuantity())
				.build();
	}

	public static ItemRecord mapInventoryEventDTORequestToItemRecord(
			InventoryEventDTORequest inventoryEventDTORequest) {

		return ObjectUtils.isEmpty(inventoryEventDTORequest) ? ItemRecord.builder().build()
				: ItemRecord.builder()
						.itemId(ItemInventoryTestUtils.buildUniqueId())
						.price(inventoryEventDTORequest.getItem().getPrice())
						.name(inventoryEventDTORequest.getItem().getName())
						.quantity(inventoryEventDTORequest.getItem().getQuantity())
						.build();
	}

	public static ItemDTO mapItemRecordToItemDTO(ItemRecord item) {

		return ObjectUtils.isEmpty(item) ? ItemDTO.builder().build()
				: ItemDTO.builder()
						.itemId(item.itemId())
						.price(item.price())
						.name(item.name())
						.quantity(item.quantity())
						.build();
	}

	public static ResponseDTO buildResponseDTO(List<InventoryEventDTO> itemEventDTOList) {

		return InventoryEventDTOResponse.builder()
				.resultSetSize(itemEventDTOList.size())
				.itemInventoryDTOList(itemEventDTOList)
				.build();
	}

	public static ResponseEntity<ResponseDTO> buildBadPathVariableResponse(String pathVariable) {

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ErrorDTOResponse.builder()
						.errorCode(HttpStatus.BAD_REQUEST.value())
						.errorMessage(pathVariable + " Is Not Numeric")
						.status(HttpStatus.BAD_REQUEST.name())
						.build());
	}
}
