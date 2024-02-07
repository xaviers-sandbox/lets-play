package com.inventory.producer.mapper;

import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import com.inventory.producer.enums.InventoryEventType;
import com.inventory.producer.model.InventoryEventDTO;
import com.inventory.producer.model.ItemDTO;
import com.inventory.producer.model.request.InventoryEventDTORequest;
import com.inventory.producer.model.response.ErrorDTOResponse;
import com.inventory.producer.model.response.InventoryEventDTOResponse;
import com.inventory.producer.model.response.ResponseDTO;
import com.inventory.producer.record.InventoryEventRecord;
import com.inventory.producer.record.ItemRecord;
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

	public static ProducerRecord<String, String> buildProducerRecord(InventoryEventRecord inventoryEventRecord, String topicName) {
		String key = inventoryEventRecord.eventId();
		String value = SandboxUtils.convertObjectToString(inventoryEventRecord);

		return new ProducerRecord<String, String>(topicName, null, key, value, buildKafkaHeaders());
	}

	public static Iterable<Header> buildKafkaHeaders() {

		return List.of(new RecordHeader("generic-header", "happyCoding".getBytes()),
				new RecordHeader("dummy-header", "kafkaRocks".getBytes()));
	}

	public static InventoryEventRecord mapInventoryEventDTORequestToInventoryEventRecord(
			InventoryEventDTORequest inventoryEventDTORequest) {

		InventoryEventType inventoryEventType = InventoryEventType.NEW;

		int randonNum = getFaker().number().numberBetween(100000, 10000000);

		if (ObjectUtils.isEmpty(inventoryEventDTORequest.getEventType())) {
			inventoryEventType = (randonNum % 2 == 0) ? InventoryEventType.NEW : InventoryEventType.UPDATE;
		}

		return InventoryEventRecord.builder()
				.eventId(buildUniqueId())
				.eventType(inventoryEventType)
				.item(mapInventoryEventDTORequestToItemRecord(inventoryEventDTORequest))
				.build();
	}

	public static InventoryEventDTO mapInventoryEventRecordToInventoryEventDTO(InventoryEventRecord inventoryEventRecord) {

		return InventoryEventDTO.builder()
				.eventId(inventoryEventRecord.eventId())
				.eventType(inventoryEventRecord.eventType())
				.itemDTO(mapItemRecordToItemDTO(inventoryEventRecord.item()))
				.build();
	}

	public static InventoryEventRecord mapUpdatedInventoryEventDTORequestToInventoryEventRecord(String eventId,
			InventoryEventDTORequest updateInventoryEventDTORequest) {

		ItemRecord updatedItem = ItemRecord.builder()
				.itemId(updateInventoryEventDTORequest.getItem().getItemId())
				.name(updateInventoryEventDTORequest.getItem().getName())
				.price(updateInventoryEventDTORequest.getItem().getPrice())
				.quantity(updateInventoryEventDTORequest.getItem().getQuantity())
				.build();

		return InventoryEventRecord.builder().eventId(eventId).eventType(InventoryEventType.UPDATE).item(updatedItem).build();
	}

	public static ItemRecord mapInventoryEventDTORequestToItemRecord(InventoryEventDTORequest inventoryEventDTORequest) {

		return ObjectUtils.isEmpty(inventoryEventDTORequest) ? ItemRecord.builder().build()
				: ItemRecord.builder()
						.itemId(buildUniqueId())
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

	public static String buildUniqueId() {
		String lorem1 = getFaker().lorem().characters(5);
		int num1 = getFaker().number().numberBetween(1000, 9999);
		String lorem2 = getFaker().lorem().characters(5);
		long num2 = getFaker().number().numberBetween(1000, 9999);

		return new StringBuilder().append(lorem1)
				.append("-")
				.append(num1)
				.append("-")
				.append(lorem2)
				.append("-")
				.append(num2)
				.toString();
	}
}
