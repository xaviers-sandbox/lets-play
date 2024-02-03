package com.inventory.producer.mapper;
import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.inventory.producer.enums.InventoryEventType;
import com.inventory.producer.model.ErrorDTOResponse;
import com.inventory.producer.model.InventoryEventDTO;
import com.inventory.producer.model.InventoryEventDTOResponse;
import com.inventory.producer.model.InventoryItemDTORequest;
import com.inventory.producer.model.ItemDTO;
import com.inventory.producer.model.ResponseDTO;
import com.inventory.producer.record.InventoryEvent;
import com.inventory.producer.record.Item;
import com.sandbox.util.SandboxUtils;

import net.datafaker.Faker;

public class InventoryEventMapper {
	private static Faker f = new Faker();
	
	public static ProducerRecord<Integer, String> buildProducerRecord(InventoryEvent inventoryEvent, String topicName) {
		Integer key = inventoryEvent.eventId();
		String value = SandboxUtils.convertObjectToString(inventoryEvent);

		return new ProducerRecord<Integer, String>(topicName, null, key, value, buildKafkaHeaders());
	}

	public static Iterable<Header> buildKafkaHeaders() {
		return List.of(new RecordHeader("generic-header", "happyCoding".getBytes()),
				new RecordHeader("dummy-header", "kafkaRocks".getBytes()));

	}

	public static InventoryEvent mapInventoryItemDTORequestToInventoryEvent(
			InventoryItemDTORequest newInventoryItemDTORequest) {
		
		Item item = Item.builder()
				.id(f.number().numberBetween(100000, 10000000))
				.price(newInventoryItemDTORequest.getPrice())
				.name(newInventoryItemDTORequest.getName())
				.quantity(newInventoryItemDTORequest.getQuantity())
				.build();
		
		InventoryEventType inventoryEventType = (item.id() % 2 == 0) ? InventoryEventType.NEW : InventoryEventType.UPDATE;

		return InventoryEvent.builder()
				.eventId(f.number().numberBetween(100000, 10000000))
				.eventType(inventoryEventType)
				.item(item)
				.build();
	}

	public static ResponseDTO buildResponseDTO(List<InventoryEventDTO> itemEventDTOList) {
	
		
		return InventoryEventDTOResponse.builder().resultSetSize(itemEventDTOList.size()).itemInventoryDTOList(itemEventDTOList).build();
	}
	
	public static InventoryEventDTO buildInventoryEventDTO(
			InventoryEvent inventoryEvent) {
		
		ItemDTO itemDTO = ItemDTO.builder()
				.id(inventoryEvent.item().id())
				.price(inventoryEvent.item().price())
				.name(inventoryEvent.item().name())
				.quantity(inventoryEvent.item().quantity())
				.build();
		
		return InventoryEventDTO.builder()
				.eventId(inventoryEvent.eventId())
				.eventType(inventoryEvent.eventType())
				.itemDTO(itemDTO)
				.build();
	}

	public static InventoryEvent mapUpdatedInventoryItemDTORequestToInventoryEvent(Integer eventId,
			InventoryItemDTORequest updatedInventoryItemDTORequest) {
		
		Item item = Item.builder()
				.id(f.number().numberBetween(100000, 10000000))
				.price(updatedInventoryItemDTORequest.getPrice())
				.name(updatedInventoryItemDTORequest.getName())
				.quantity(updatedInventoryItemDTORequest.getQuantity())
				.build();
		
		return InventoryEvent.builder()
				.eventId(eventId)
				.eventType(InventoryEventType.UPDATE)
				.item(item)
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
