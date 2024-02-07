package com.inventory.producer.mapper;

import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import com.inventory.producer.enums.InventoryEventType;
import com.inventory.producer.model.ErrorDTOResponse;
import com.inventory.producer.model.InventoryEventDTO;
import com.inventory.producer.model.InventoryEventDTORequest;
import com.inventory.producer.model.InventoryEventDTOResponse;
import com.inventory.producer.model.ItemDTO;
import com.inventory.producer.model.ResponseDTO;
import com.inventory.producer.record.InventoryEvent;
import com.inventory.producer.record.Item;
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

	public static ProducerRecord<String, String> buildProducerRecord(InventoryEvent inventoryEvent, String topicName) {
		String key = inventoryEvent.eventId();
		String value = SandboxUtils.convertObjectToString(inventoryEvent);

		return new ProducerRecord<String, String>(topicName, null, key, value, buildKafkaHeaders());
	}

	public static Iterable<Header> buildKafkaHeaders() {

		return List.of(new RecordHeader("generic-header", "happyCoding".getBytes()),
				new RecordHeader("dummy-header", "kafkaRocks".getBytes()));
	}

	public static InventoryEvent mapInventoryEventDTORequestToInventoryEvent(
			InventoryEventDTORequest inventoryEventDTORequest) {

		InventoryEventType inventoryEventType = InventoryEventType.NEW;

		int eventId = getFaker().number().numberBetween(100000, 10000000);

		if (ObjectUtils.isEmpty(inventoryEventDTORequest.getEventType())) {
			inventoryEventType = (eventId % 2 == 0) ? InventoryEventType.NEW : InventoryEventType.UPDATE;
		}

		return InventoryEvent.builder()
				.eventId(buildUniqueId())
				.eventType(inventoryEventType)
				.item(mapInventoryEventDTORequestToItem(inventoryEventDTORequest))
				.build();
	}

	public static InventoryEventDTO mapInventoryEventToInventoryEventDTO(InventoryEvent inventoryEvent) {

		return InventoryEventDTO.builder()
				.eventId(inventoryEvent.eventId())
				.eventType(inventoryEvent.eventType())
				.itemDTO(mapItemToItemDTO(inventoryEvent.item()))
				.build();
	}

	public static InventoryEvent mapUpdatedInventoryItemDTORequestToInventoryEvent(String eventId,
			InventoryEventDTORequest updateInventoryEventDTORequest) {

		Item updatedItem = Item.builder()
				.itemId(updateInventoryEventDTORequest.getItem().getItemId())
				.name(updateInventoryEventDTORequest.getItem().getName())
				.price(updateInventoryEventDTORequest.getItem().getPrice())
				.quantity(updateInventoryEventDTORequest.getItem().getQuantity())
				.build();
		
		return InventoryEvent.builder()
				.eventId(eventId)
				.eventType(InventoryEventType.UPDATE)
				.item(updatedItem)
				.build();
	}

	public static Item mapInventoryEventDTORequestToItem(InventoryEventDTORequest inventoryEventDTORequest) {

		return ObjectUtils.isEmpty(inventoryEventDTORequest) ? Item.builder().build()
				: Item.builder()
						.itemId(buildUniqueId())
						.price(inventoryEventDTORequest.getItem().getPrice())
						.name(inventoryEventDTORequest.getItem().getName())
						.quantity(inventoryEventDTORequest.getItem().getQuantity())
						.build();
	}

	public static ItemDTO mapItemToItemDTO(Item item) {

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
