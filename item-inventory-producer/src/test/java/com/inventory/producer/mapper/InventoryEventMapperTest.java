package com.inventory.producer.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

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
import org.springframework.test.context.ActiveProfiles;

import com.inventory.producer.model.record.InventoryEventRecord;
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
	private Iterable<Header> headerIterableMock;

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

		headerIterableMock = List.of(new RecordHeader("test-header", "producerHeader".getBytes()));

		inventoryEventMapperMock.when(
				() -> InventoryEventMapper.buildProducerRecord(any(InventoryEventRecord.class), any(String.class)))
				.thenCallRealMethod();

		sandboxUtilsMock.when(() -> SandboxUtils.convertObjectToString(any(InventoryEventRecord.class)))
				.thenReturn(inventoryEventRecordStr);

		inventoryEventMapperMock.when(() -> InventoryEventMapper.buildKafkaHeaders()).thenReturn(headerIterableMock);

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
				() -> InventoryEventMapper.buildProducerRecord(any(InventoryEventRecord.class), any(String.class)));

		sandboxUtilsMock.verify(() -> SandboxUtils.convertObjectToString(any(InventoryEventRecord.class)));

		inventoryEventMapperMock.verify(() -> InventoryEventMapper.buildKafkaHeaders());
	}
	
	@Test
	void buildKafkaHeaders_test() {
		log.debug("buildKafkaHeaders_test");

		inventoryEventMapperMock.when(() -> InventoryEventMapper.buildKafkaHeaders()).thenCallRealMethod();

		List<Header> headersList = InventoryEventMapper
				.buildKafkaHeaders();

		assertNotNull(headersList);
		assertEquals(2, headersList.size());
		
		List<String> headerKeysList = headersList.stream().map(Header::key).toList();
		assertTrue(headerKeysList.contains("generic-header"));
		assertTrue(headerKeysList.contains("dummy-header"));

		inventoryEventMapperMock.verify(() -> InventoryEventMapper.buildKafkaHeaders());
	}
}
