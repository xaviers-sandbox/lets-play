package com.inventory.consumer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.ActiveProfiles;

import com.inventory.consumer.entity.InventoryEvent;
import com.inventory.consumer.mapper.InventoryEventMapper;
import com.inventory.consumer.repo.InventoryEventRepo;
import com.inventory.consumer.service.impl.InventoryEventServiceImpl;
import com.inventory.producer.enums.InventoryEventType;

@ActiveProfiles("test")
public class InventoryEventServiceTest {
	@Spy
	@InjectMocks
	private InventoryEventServiceImpl inventoryEventServiceImplMock;

	@Spy
	private InventoryEventRepo inventoryEventRepoMock;

	@Mock
	private ConsumerRecord<String, String> consumerRecordMock;

	private MockedStatic<InventoryEventMapper> inventoryEventMapperMock;

	@Spy
	private InventoryEvent inventoryEventMock;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);

		inventoryEventMapperMock = Mockito.mockStatic(InventoryEventMapper.class);
	}

	@AfterEach
	public void tearDown() {
		inventoryEventMapperMock.close();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void processConsumerRecord_new_test() {
		inventoryEventMock.setEventType(InventoryEventType.NEW);

		when(consumerRecordMock.value()).thenReturn("consumerRecordString");

		inventoryEventMapperMock
				.when(() -> InventoryEventMapper.buildInventoryEvent((ConsumerRecord<String, String>) any()))
				.thenReturn(inventoryEventMock);

		doReturn(inventoryEventMock).when(inventoryEventServiceImplMock).saveInventoryEvent(any(InventoryEvent.class));

		inventoryEventServiceImplMock.processConsumerRecord(consumerRecordMock);

		verify(inventoryEventMock, times(3)).getEventType();
		verify(consumerRecordMock).value();

		inventoryEventMapperMock
				.verify(() -> InventoryEventMapper.buildInventoryEvent((ConsumerRecord<String, String>) any()));

		verify(inventoryEventServiceImplMock).saveInventoryEvent(any(InventoryEvent.class));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void processConsumerRecord_update_test() {
		inventoryEventMock.setEventType(InventoryEventType.UPDATE);

		when(consumerRecordMock.value()).thenReturn("consumerRecordString");

		inventoryEventMapperMock
				.when(() -> InventoryEventMapper.buildInventoryEvent((ConsumerRecord<String, String>) any()))
				.thenReturn(inventoryEventMock);

		doReturn(inventoryEventMock).when(inventoryEventServiceImplMock)
				.processUpdateConsumerRecord(any(InventoryEvent.class));

		inventoryEventServiceImplMock.processConsumerRecord(consumerRecordMock);

		verify(inventoryEventMock, times(4)).getEventType();
		verify(consumerRecordMock).value();

		inventoryEventMapperMock
				.verify(() -> InventoryEventMapper.buildInventoryEvent((ConsumerRecord<String, String>) any()));

	}

	@Test
	@SuppressWarnings("unchecked")
	public void processConsumerRecord_invalid_test() {
		inventoryEventMock.setEventType(InventoryEventType.UPDATED);

		when(consumerRecordMock.value()).thenReturn("consumerRecordString");

		inventoryEventMapperMock
				.when(() -> InventoryEventMapper.buildInventoryEvent((ConsumerRecord<String, String>) any()))
				.thenReturn(inventoryEventMock);

		inventoryEventServiceImplMock.processConsumerRecord(consumerRecordMock);

		verify(inventoryEventMock, times(4)).getEventType();
		verify(consumerRecordMock).value();

		inventoryEventMapperMock
				.verify(() -> InventoryEventMapper.buildInventoryEvent((ConsumerRecord<String, String>) any()));

	}

	@Test
	void processUpdateConsumerRecord() {

		when(inventoryEventMock.getEventId()).thenReturn("testEventIdString");
		when(inventoryEventRepoMock.findById(any(String.class))).thenReturn(Optional.of(inventoryEventMock));

		inventoryEventMapperMock.when(() -> InventoryEventMapper
				.updateOrigItemEventWithUpdatedItemEvent(any(InventoryEvent.class), any(InventoryEvent.class)))
				.thenReturn(inventoryEventMock);

		doReturn(inventoryEventMock).when(inventoryEventServiceImplMock).saveInventoryEvent(any(InventoryEvent.class));

		inventoryEventServiceImplMock.processUpdateConsumerRecord(inventoryEventMock);

		verify(inventoryEventMock).getEventId();
		verify(inventoryEventRepoMock).findById(any(String.class));

		inventoryEventMapperMock.verify(() -> InventoryEventMapper
				.updateOrigItemEventWithUpdatedItemEvent(any(InventoryEvent.class), any(InventoryEvent.class)));

		verify(inventoryEventServiceImplMock).saveInventoryEvent(any(InventoryEvent.class));
	}

	@Test
	public void saveInventoryEvent() {

		inventoryEventMock.setEventId("testEventIdString");

		doReturn(inventoryEventMock).when(inventoryEventRepoMock).save(any(InventoryEvent.class));

		InventoryEvent inventoryEvent = inventoryEventServiceImplMock.saveInventoryEvent(inventoryEventMock);

		assertNotNull(inventoryEvent);
		assertEquals("testEventIdString", inventoryEvent.getEventId());

		verify(inventoryEventRepoMock).save(any(InventoryEvent.class));

	}

	@Test
	public void deleteInventoryEventById() {

		doNothing().when(inventoryEventRepoMock).deleteById(any(String.class));

		inventoryEventServiceImplMock.deleteInventoryEventById("someTestString");

		verify(inventoryEventRepoMock).deleteById(any(String.class));
	}
}
