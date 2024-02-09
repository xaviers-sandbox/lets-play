package com.inventory.consumer.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.ActiveProfiles;

import com.inventory.consumer.entity.InventoryEvent;
import com.inventory.consumer.entity.Item;
import com.inventory.consumer.entity.KafkaDetails;
import com.inventory.producer.enums.InventoryEventType;
import com.sandbox.util.SandboxUtils;

@ActiveProfiles("test")
public class InventoryEventMapperTest {
	private MockedStatic<InventoryEventMapper> inventoryEventMapperMock;

	private MockedStatic<SandboxUtils> sandboxUtilsMock;

	@Mock
	private ConsumerRecord<String, String> consumerRecordMock;

	@Spy
	private InventoryEvent origInventoryEventMock;

	@Spy
	private InventoryEvent updatedInventoryEventMock;

	@Spy
	private Item origItemMock;

	@Spy
	private Item updatedItemMock;

	@Spy
	private KafkaDetails updatedKafkaDetailsMock;

	@Spy
	private KafkaDetails origKafkaDetailsMock;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);

		inventoryEventMapperMock = Mockito.mockStatic(InventoryEventMapper.class);

		sandboxUtilsMock = Mockito.mockStatic(SandboxUtils.class);
	}

	@AfterEach
	public void tearDown() {
		inventoryEventMapperMock.close();

		sandboxUtilsMock.close();
	}

	@SuppressWarnings("unchecked")
	@Test
	void buildInventoryEvent_test() {

		origInventoryEventMock.setEventId("testingMapper1234");

		when(consumerRecordMock.value()).thenReturn("consumerRecordString");

		sandboxUtilsMock.when(() -> SandboxUtils.mapStringToObject(any(String.class), any(Class.class)))
				.thenReturn(origInventoryEventMock);

		inventoryEventMapperMock.when(() -> InventoryEventMapper
				.mapKafkaDetailsToInventoryEvent(any(InventoryEvent.class), (ConsumerRecord<String, String>)any()))
				.thenReturn(origInventoryEventMock);

		inventoryEventMapperMock.when(() -> InventoryEventMapper.buildInventoryEvent((ConsumerRecord<String, String>)any()))
				.thenCallRealMethod();

		InventoryEvent inventoryEvent = InventoryEventMapper.buildInventoryEvent(consumerRecordMock);

		assertNotNull(inventoryEvent);
		assertEquals("testingMapper1234", inventoryEvent.getEventId());

		verify(consumerRecordMock).value();
		sandboxUtilsMock.verify(() -> SandboxUtils.mapStringToObject(any(String.class), any(Class.class)));
		
		inventoryEventMapperMock.verify(() -> InventoryEventMapper
				.mapKafkaDetailsToInventoryEvent(any(InventoryEvent.class), (ConsumerRecord<String, String>)any()));
		
		inventoryEventMapperMock.verify(() -> InventoryEventMapper.buildInventoryEvent((ConsumerRecord<String, String>)any()));
	}

	@SuppressWarnings("unchecked")
	@Test
	void mapKafkaDetailsToInventoryEvent() {
		origItemMock.setName("testItemName");
		origInventoryEventMock.setEventId("testingMapper1234");

		when(origInventoryEventMock.getItem()).thenReturn(origItemMock);

		when(consumerRecordMock.topic()).thenReturn("consumerRecordTopic");
		when(consumerRecordMock.partition()).thenReturn(1234);
		when(consumerRecordMock.offset()).thenReturn(4321L);

		inventoryEventMapperMock.when(() -> InventoryEventMapper
				.mapKafkaDetailsToInventoryEvent(any(InventoryEvent.class), (ConsumerRecord<String, String>)any()))
				.thenCallRealMethod();

		InventoryEvent inventoryEvent = InventoryEventMapper.mapKafkaDetailsToInventoryEvent(origInventoryEventMock,
				consumerRecordMock);

		assertNotNull(inventoryEvent);
		assertEquals("testingMapper1234", inventoryEvent.getEventId());
		
		assertNotNull(inventoryEvent.getItem());
		assertEquals("testItemName", inventoryEvent.getItem().getName());

		assertNotNull(inventoryEvent.getKafkaDetails());
		assertEquals("consumerRecordTopic", inventoryEvent.getKafkaDetails().getTopicName());

		verify(consumerRecordMock).topic();
		verify(consumerRecordMock).partition();
		verify(consumerRecordMock).offset();

		inventoryEventMapperMock.verify(() -> InventoryEventMapper
				.mapKafkaDetailsToInventoryEvent(any(InventoryEvent.class), (ConsumerRecord<String, String>)any()));
	}

	@Test
	void updateOrigItemEventWithUpdatedItemEvent() {
		
		inventoryEventMapperMock.when(() -> InventoryEventMapper
				.updateOrigItemEventWithUpdatedItemEvent(any(InventoryEvent.class), any(InventoryEvent.class)))
				.thenCallRealMethod();

		updatedItemMock.setName("updatedTestItem");
		updatedItemMock.setPrice(9876.00);
		updatedItemMock.setQuantity(5432);

		
		updatedKafkaDetailsMock.setTopicName("updatedKafkaTopicName");
		updatedKafkaDetailsMock.setPartition(5555);
		updatedKafkaDetailsMock.setOffset(4444L);
		
		updatedInventoryEventMock.setKafkaDetails(updatedKafkaDetailsMock);
		updatedInventoryEventMock.setItem(updatedItemMock);

		origKafkaDetailsMock.setTopicName("origKafkaTopicName");
		origKafkaDetailsMock.setPartition(2468);
		origKafkaDetailsMock.setOffset(7531L);
		
		origInventoryEventMock.setEventId("origInventoryEventId");
		origInventoryEventMock.setEventType(InventoryEventType.NEW);
		origInventoryEventMock.setKafkaDetails(origKafkaDetailsMock);
		origInventoryEventMock.setItem(origItemMock);

		InventoryEvent inventoryEvent = InventoryEventMapper.updateOrigItemEventWithUpdatedItemEvent(updatedInventoryEventMock,
				origInventoryEventMock);
		
		assertNotNull(inventoryEvent);
		assertEquals("origInventoryEventId", inventoryEvent.getEventId());
		assertEquals(InventoryEventType.UPDATED, inventoryEvent.getEventType());
		
		assertNotNull(inventoryEvent.getItem());
		assertEquals("updatedTestItem", inventoryEvent.getItem().getName());
		assertEquals(5432, inventoryEvent.getItem().getQuantity());

		assertNotNull(inventoryEvent.getKafkaDetails());
		assertEquals("updatedKafkaTopicName", inventoryEvent.getKafkaDetails().getTopicName());
		assertEquals("origKafkaTopicName", inventoryEvent.getKafkaDetails().getPreviousTopicName());

		verify(updatedItemMock).getName();
		verify(updatedItemMock).getPrice();
		verify(updatedItemMock).getQuantity();
		
		verify(updatedKafkaDetailsMock).getTopicName();
		verify(updatedKafkaDetailsMock).getPartition();
		verify(updatedKafkaDetailsMock).getOffset();
		
		verify(origKafkaDetailsMock, times(2)).getTopicName();
		verify(origKafkaDetailsMock).getPartition();
		verify(origKafkaDetailsMock).getOffset();
		
		inventoryEventMapperMock.verify(() -> InventoryEventMapper
				.updateOrigItemEventWithUpdatedItemEvent(any(InventoryEvent.class), any(InventoryEvent.class)));
	}
}
