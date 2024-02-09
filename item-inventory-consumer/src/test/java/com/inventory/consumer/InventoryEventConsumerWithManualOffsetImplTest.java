package com.inventory.consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.test.context.ActiveProfiles;

import com.inventory.consumer.consumer.InventoryEventConsumerWithManualOffsetImpl;
import com.inventory.consumer.service.InventoryEventService;


@ActiveProfiles("test")
public class InventoryEventConsumerWithManualOffsetImplTest {

	@InjectMocks
	private InventoryEventConsumerWithManualOffsetImpl inventoryEventConsumerWithManualOffsetImplMock;
	
	@Mock
	private ConsumerRecord<String, String> consumerRecordMock;
	
	@Mock
	private Acknowledgment acknowledgmentMock;

	@Mock
	private InventoryEventService inventoryEventServiceMock;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}
	
	@SuppressWarnings({ "unchecked"})
	@Test
	void onMessage_test() {
		doNothing().when(acknowledgmentMock).acknowledge();
		doNothing().when(inventoryEventServiceMock).processConsumerRecord((ConsumerRecord<String, String>)any());
		
		inventoryEventConsumerWithManualOffsetImplMock.onMessage(consumerRecordMock, acknowledgmentMock);
		
		verify(acknowledgmentMock).acknowledge();
		verify(inventoryEventServiceMock).processConsumerRecord((ConsumerRecord<String, String>)any());
	}
}