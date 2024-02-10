package com.inventory.producer.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Collections;
import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

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

import lombok.extern.slf4j.Slf4j;

@ActiveProfiles("test")
@Slf4j
public class InventoryEventMapperTest {
	private MockedStatic<InventoryEventMapper> inventoryEventMapperMock;

	private MockedStatic<SandboxUtils> sandboxUtilsMock;

	private String topicNameMock;

	@Spy
	private List<Header> headerList;

	@BeforeEach
	public void setup() {

		MockitoAnnotations.openMocks(this);

		inventoryEventMapperMock = Mockito.mockStatic(InventoryEventMapper.class);

		sandboxUtilsMock = Mockito.mockStatic(SandboxUtils.class);

		topicNameMock = "unit-test-topic-name";
	}

	@AfterEach
	public void tearDown() {
		inventoryEventMapperMock.close();

		sandboxUtilsMock.close();
	}

	@Test
	void buildProducerRecord_test() {
		log.debug("buildProducerRecord_test");

		InventoryEventRecord inventoryEventRecordMock = ItemInventoryTestUtils.buildMockInventoryEventRecord();

		SandboxUtils.prettyPrintObjectToJson(inventoryEventRecordMock);

		String inventoryEventRecordStr = "{\"eventId\":\"unit-test-event-id\",\"eventType\":\"UPDATE\",\"item\":{\"itemId\":\"r6t9e-3944-vo41d-3521\",\"name\":\"Gorgeous Rubber Gloves\",\"price\":54.29,\"quantity\":6}}";

		headerList = List.of(new RecordHeader("test-header", "producerHeader".getBytes()));

		inventoryEventMapperMock.when(
				() -> InventoryEventMapper.buildProducerRecord(any(InventoryEventRecord.class), anyString()))
				.thenCallRealMethod();

		sandboxUtilsMock.when(() -> SandboxUtils.convertObjectToString(any(InventoryEventRecord.class)))
				.thenReturn(inventoryEventRecordStr);

		inventoryEventMapperMock.when(() -> InventoryEventMapper.buildKafkaHeaders()).thenReturn(headerList);

		ProducerRecord<String, String> producerRecord = InventoryEventMapper
				.buildProducerRecord(inventoryEventRecordMock, topicNameMock);

		assertNotNull(producerRecord);
		assertEquals(inventoryEventRecordStr, producerRecord.value());
		assertEquals(topicNameMock, producerRecord.topic());
		assertEquals(inventoryEventRecordMock.eventId(), producerRecord.key());

		producerRecord.headers().forEach(header -> {
			assertEquals("test-header", header.key());
			assertEquals("producerHeader".getBytes().length, header.value().length);
		});

		inventoryEventMapperMock.verify(
				() -> InventoryEventMapper.buildProducerRecord(any(InventoryEventRecord.class), anyString()));

		sandboxUtilsMock.verify(() -> SandboxUtils.convertObjectToString(any(InventoryEventRecord.class)));

		inventoryEventMapperMock.verify(() -> InventoryEventMapper.buildKafkaHeaders());
	}

	@Test
	void buildKafkaHeaders_test() {
		log.debug("buildKafkaHeaders_test");

		inventoryEventMapperMock.when(() -> InventoryEventMapper.buildKafkaHeaders()).thenCallRealMethod();

		List<Header> headersList = InventoryEventMapper.buildKafkaHeaders();

		assertNotNull(headersList);
		assertEquals(2, headersList.size());

		List<String> headerKeysList = headersList.stream().map(Header::key).toList();
		assertTrue(headerKeysList.contains("generic-header"));
		assertTrue(headerKeysList.contains("dummy-header"));

		inventoryEventMapperMock.verify(() -> InventoryEventMapper.buildKafkaHeaders());
	}

	@Test
	void mapInventoryEventDTORequestToInventoryEventRecord_test() {
		log.debug("mapInventoryEventDTORequestToInventoryEventRecord_test");

		ItemRecord itemRecordMock = ItemInventoryTestUtils.buildMockItemRecord();

		inventoryEventMapperMock.when(
				() -> InventoryEventMapper.mapInventoryEventDTORequestToItemRecord(any(InventoryEventDTORequest.class)))
				.thenReturn(itemRecordMock);

		inventoryEventMapperMock
				.when(() -> InventoryEventMapper
						.mapInventoryEventDTORequestToInventoryEventRecord(any(InventoryEventDTORequest.class)))
				.thenCallRealMethod();

		InventoryEventDTORequest inventoryEventDTORequest = InventoryEventDTORequest.builder()
				.eventType(InventoryEventType.NEW)
				.build();

		InventoryEventRecord inventoryEventRecord = InventoryEventMapper
				.mapInventoryEventDTORequestToInventoryEventRecord(inventoryEventDTORequest);

		assertNotNull(inventoryEventRecord);
		assertNotNull(inventoryEventRecord.eventId());
		assertNotNull(inventoryEventRecord.item());
		assertEquals(itemRecordMock.itemId(), inventoryEventRecord.item().itemId());
		assertEquals(itemRecordMock.name(), inventoryEventRecord.item().name());
		assertEquals(itemRecordMock.price(), inventoryEventRecord.item().price());
		assertEquals(itemRecordMock.quantity(), inventoryEventRecord.item().quantity());

		inventoryEventMapperMock.verify(() -> InventoryEventMapper
				.mapInventoryEventDTORequestToItemRecord(any(InventoryEventDTORequest.class)));

		inventoryEventMapperMock.verify(() -> InventoryEventMapper
				.mapInventoryEventDTORequestToInventoryEventRecord(any(InventoryEventDTORequest.class)));
	}

	@Test
	void mapInventoryEventRecordToInventoryEventDTO_test() {
		log.debug("mapInventoryEventRecordToInventoryEventDTO_test");

		inventoryEventMapperMock.when(
				() -> InventoryEventMapper.mapInventoryEventRecordToInventoryEventDTO(any(InventoryEventRecord.class)))
				.thenCallRealMethod();

		inventoryEventMapperMock.when(() -> InventoryEventMapper.mapItemRecordToItemDTO(any(ItemRecord.class)))
				.thenCallRealMethod();

		InventoryEventRecord inventoryEventRecordMock = ItemInventoryTestUtils.buildMockInventoryEventRecord();

		InventoryEventDTO inventoryEventDTO = InventoryEventMapper
				.mapInventoryEventRecordToInventoryEventDTO(inventoryEventRecordMock);

		assertNotNull(inventoryEventDTO);
		assertEquals(inventoryEventRecordMock.eventId(), inventoryEventDTO.getEventId());
		assertEquals(inventoryEventRecordMock.eventType(), inventoryEventDTO.getEventType());
		assertNotNull(inventoryEventDTO.getItemDTO());

		assertEquals(inventoryEventRecordMock.item().itemId(), inventoryEventDTO.getItemDTO().getItemId());
		assertEquals(inventoryEventRecordMock.item().name(), inventoryEventDTO.getItemDTO().getName());
		assertEquals(inventoryEventRecordMock.item().price(), inventoryEventDTO.getItemDTO().getPrice());
		assertEquals(inventoryEventRecordMock.item().quantity(), inventoryEventDTO.getItemDTO().getQuantity());

		inventoryEventMapperMock.verify(
				() -> InventoryEventMapper.mapInventoryEventRecordToInventoryEventDTO(any(InventoryEventRecord.class)));

		inventoryEventMapperMock.verify(() -> InventoryEventMapper.mapItemRecordToItemDTO(any(ItemRecord.class)));
	}

	@Test
	void mapUpdatedInventoryEventDTORequestToInventoryEventRecord_test() {
		log.debug("mapUpdatedInventoryEventDTORequestToInventoryEventRecord_test");

		InventoryEventDTORequest updatedInventoryEventDTORequest = ItemInventoryTestUtils
				.buildMockInventoryEventDTORequest();

		inventoryEventMapperMock.when(
				() -> InventoryEventMapper.mapUpdatedInventoryEventDTORequestToInventoryEventRecord(anyString(),
						any(InventoryEventDTORequest.class)))
				.thenCallRealMethod();

		inventoryEventMapperMock
				.when(() -> InventoryEventMapper.mapItemRecordRequestToUpdatedItem(any(ItemDTORequest.class)))
				.thenCallRealMethod();

		InventoryEventRecord inventoryEventDTO = InventoryEventMapper
				.mapUpdatedInventoryEventDTORequestToInventoryEventRecord("testing-event-id",
						updatedInventoryEventDTORequest);

		assertNotNull(inventoryEventDTO);
		assertEquals("testing-event-id", inventoryEventDTO.eventId());
		assertEquals(InventoryEventType.UPDATE, inventoryEventDTO.eventType());
		assertNotNull(inventoryEventDTO.item());

		assertEquals(inventoryEventDTO.item().itemId(), updatedInventoryEventDTORequest.getItem().getItemId());
		assertEquals(inventoryEventDTO.item().name(), updatedInventoryEventDTORequest.getItem().getName());
		assertEquals(inventoryEventDTO.item().price(), updatedInventoryEventDTORequest.getItem().getPrice());
		assertEquals(inventoryEventDTO.item().quantity(), updatedInventoryEventDTORequest.getItem().getQuantity());

		inventoryEventMapperMock.verify(
				() -> InventoryEventMapper.mapUpdatedInventoryEventDTORequestToInventoryEventRecord(anyString(),
						any(InventoryEventDTORequest.class)));

		inventoryEventMapperMock
				.verify(() -> InventoryEventMapper.mapItemRecordRequestToUpdatedItem(any(ItemDTORequest.class)));
	}

	@Test
	void mapItemRecordRequestToUpdatedItem_test() {
		log.debug("mapItemRecordRequestToUpdatedItem");

		ItemDTORequest itemDTORequest = ItemInventoryTestUtils.buildMockItemDTORequest();

		inventoryEventMapperMock
				.when(() -> InventoryEventMapper.mapItemRecordRequestToUpdatedItem(any(ItemDTORequest.class)))
				.thenCallRealMethod();

		ItemRecord itemRecord = InventoryEventMapper.mapItemRecordRequestToUpdatedItem(itemDTORequest);

		assertNotNull(itemRecord);
		assertEquals(itemRecord.itemId(), itemDTORequest.getItemId());
		assertEquals(itemRecord.name(), itemDTORequest.getName());
		assertEquals(itemRecord.price(), itemDTORequest.getPrice());
		assertEquals(itemRecord.quantity(), itemDTORequest.getQuantity());

		inventoryEventMapperMock
				.verify(() -> InventoryEventMapper.mapItemRecordRequestToUpdatedItem(any(ItemDTORequest.class)));
	}

	@Test
	void mapInventoryEventDTORequestToItemRecord_test() {
		log.debug("mapInventoryEventDTORequestToItemRecord_test");

		inventoryEventMapperMock.when(
				() -> InventoryEventMapper.mapInventoryEventDTORequestToItemRecord(any(InventoryEventDTORequest.class)))
				.thenCallRealMethod();

		InventoryEventDTORequest inventoryEventDTORequest = ItemInventoryTestUtils.buildMockInventoryEventDTORequest();

		ItemRecord itemRecord = InventoryEventMapper.mapInventoryEventDTORequestToItemRecord(inventoryEventDTORequest);

		assertNotNull(itemRecord);
		assertNotNull(itemRecord.itemId());
		assertEquals(itemRecord.name(), inventoryEventDTORequest.getItem().getName());
		assertEquals(itemRecord.price(), inventoryEventDTORequest.getItem().getPrice());
		assertEquals(itemRecord.quantity(), inventoryEventDTORequest.getItem().getQuantity());

		inventoryEventMapperMock.verify(() -> InventoryEventMapper
				.mapInventoryEventDTORequestToItemRecord(any(InventoryEventDTORequest.class)));
	}

	@Test
	void mapItemRecordToItemDTO_test() {
		log.debug("mapItemRecordToItemDTO_test");

		inventoryEventMapperMock.when(() -> InventoryEventMapper.mapItemRecordToItemDTO(any(ItemRecord.class)))
				.thenCallRealMethod();

		ItemRecord itemRecord = ItemInventoryTestUtils.buildMockItemRecord();

		ItemDTO itemDTO = InventoryEventMapper.mapItemRecordToItemDTO(itemRecord);

		assertNotNull(itemRecord);
		assertEquals(itemRecord.itemId(), itemDTO.getItemId());
		assertEquals(itemRecord.name(), itemDTO.getName());
		assertEquals(itemRecord.price(), itemDTO.getPrice());
		assertEquals(itemRecord.quantity(), itemDTO.getQuantity());

		inventoryEventMapperMock.verify(() -> InventoryEventMapper.mapItemRecordToItemDTO(any(ItemRecord.class)));
	}

	@Test
	void buildResponseDTO_test() {
		log.debug("buildResponseDTO_test");

		inventoryEventMapperMock.when(() -> InventoryEventMapper.buildResponseDTO(anyList())).thenCallRealMethod();

		InventoryEventDTO inventoryEventDTO = ItemInventoryTestUtils.buildMockInventoryEventDTO();

		InventoryEventDTOResponse inventoryEventDTOResponse = (InventoryEventDTOResponse) InventoryEventMapper
				.buildResponseDTO(Collections.singletonList(inventoryEventDTO));

		assertNotNull(inventoryEventDTOResponse);
		assertEquals(1, inventoryEventDTOResponse.getResultSetSize());

		InventoryEventDTO match = inventoryEventDTOResponse.getItemInventoryDTOList().getFirst();

		assertNotNull(match);
		assertEquals(inventoryEventDTO.getEventId(), match.getEventId());
		assertNotNull(inventoryEventDTO.getEventType());

		assertNotNull(inventoryEventDTO.getItemDTO());
		assertEquals(inventoryEventDTO.getItemDTO().getItemId(), match.getItemDTO().getItemId());
		assertEquals(inventoryEventDTO.getItemDTO().getName(), match.getItemDTO().getName());
		assertEquals(inventoryEventDTO.getItemDTO().getPrice(), match.getItemDTO().getPrice());
		assertEquals(inventoryEventDTO.getItemDTO().getQuantity(), match.getItemDTO().getQuantity());

		inventoryEventMapperMock.verify(() -> InventoryEventMapper.buildResponseDTO(anyList()));
	}

	@Test
	void buildBadPathVariableResponse_test() {
		log.debug("buildBadPathVariableResponse_test");

		inventoryEventMapperMock.when(() -> InventoryEventMapper.buildBadPathVariableResponse(anyString())).thenCallRealMethod();

		ResponseEntity<ResponseDTO> responseEntity = InventoryEventMapper.buildBadPathVariableResponse("badPathVariableTest");
	
		assertNotNull(responseEntity);
		assertNotNull(responseEntity.getBody());
		
		ErrorDTOResponse responseDTO = (ErrorDTOResponse) responseEntity.getBody();
		assertEquals(400, responseDTO.getErrorCode());
		assertEquals("BAD_REQUEST", responseDTO.getStatus());
		assertTrue(responseDTO.getErrorMessage().contains("Is Not Numeric"));
		
		inventoryEventMapperMock.verify(() -> InventoryEventMapper.buildBadPathVariableResponse(anyString()));
	}
}
