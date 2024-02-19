package com.inventory.producer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import com.inventory.producer.mapper.InventoryEventMapper;
import com.inventory.producer.model.InventoryEventDTO;
import com.inventory.producer.model.record.InventoryEventRecord;
import com.inventory.producer.model.request.InventoryEventDTORequest;
import com.inventory.producer.model.response.InventoryEventDTOResponse;
import com.inventory.producer.model.response.ResponseDTO;
import com.inventory.producer.producer.InventoryEventProducer;
import com.inventory.producer.service.impl.InventoryEventServiceImpl;
import com.item.inventory.test.utils.ItemInventoryTestUtils;

@ActiveProfiles("test")
public class InventoryEventServiceTest {
	private static final int TEST_LIST_SIZE = 2;

	@Spy
	@InjectMocks
	private InventoryEventServiceImpl inventoryEventServiceImpl;

	@Mock
	private InventoryEventProducer inventoryEventsProducerMock;

	@Spy
	private CompletableFuture<SendResult<String, String>> kafkaResponseMock;

	MockedStatic<ItemInventoryTestUtils> itemInventoryTestUtilsMock;

	MockedStatic<InventoryEventMapper> inventoryEventMapperMock;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		ReflectionTestUtils.setField(inventoryEventServiceImpl, "topicName", "inventory-events");

		itemInventoryTestUtilsMock = Mockito.mockStatic(ItemInventoryTestUtils.class);
		inventoryEventMapperMock = Mockito.mockStatic(InventoryEventMapper.class);
	}

	@AfterEach
	public void tearDown() {
		itemInventoryTestUtilsMock.close();
		inventoryEventMapperMock.close();
	}

	@Test
	void addNewMockItems_test() {
		itemInventoryTestUtilsMock.when(() -> ItemInventoryTestUtils.generateInventoryEventRecordList(anyInt()))
				.thenCallRealMethod();

		inventoryEventMapperMock.when(() -> InventoryEventMapper.buildResponseDTO(anyList())).thenCallRealMethod();

		ProducerRecord<String, String> producerRecordMock = any();

		when(inventoryEventsProducerMock.sendEventToTopicAsyncWithProducerRecord(producerRecordMock))
				.thenReturn(kafkaResponseMock);

		ResponseEntity<ResponseDTO> responseEntity = inventoryEventServiceImpl.addNewMockItems(TEST_LIST_SIZE);

		InventoryEventDTOResponse responseDTO = (InventoryEventDTOResponse) responseEntity.getBody();

		List<InventoryEventDTO> responseList = responseDTO.getItemInventoryDTOList();

		assertNotNull(responseList);
		assertEquals(TEST_LIST_SIZE, responseList.size());

		verify(inventoryEventsProducerMock, times(2)).sendEventToTopicAsyncWithProducerRecord(producerRecordMock);

		itemInventoryTestUtilsMock.verify(() -> ItemInventoryTestUtils.generateInventoryEventRecordList(anyInt()));

		inventoryEventMapperMock.verify(() -> InventoryEventMapper.buildResponseDTO(anyList()));

		System.out.println("Successful addNewMockItems_test");
	}

	@Test
	void addNewItemInventory_test() {
		itemInventoryTestUtilsMock.when(() -> ItemInventoryTestUtils.buildMockInventoryEventDTORequest())
				.thenCallRealMethod();

		itemInventoryTestUtilsMock.when(() -> ItemInventoryTestUtils.buildMockInventoryEventRecord())
				.thenCallRealMethod();

		itemInventoryTestUtilsMock.when(() -> ItemInventoryTestUtils.buildMockInventoryEventDTO()).thenCallRealMethod();

		itemInventoryTestUtilsMock.when(() -> ItemInventoryTestUtils.buildMockInventoryEventDTOResponse())
				.thenCallRealMethod();

		inventoryEventMapperMock
				.when(() -> InventoryEventMapper
						.mapInventoryEventDTORequestToInventoryEventRecord(any(InventoryEventDTORequest.class)))
				.thenCallRealMethod();

		InventoryEventDTORequest inventoryEventDTORequest = ItemInventoryTestUtils.buildMockInventoryEventDTORequest();

		InventoryEventRecord inventoryEventRecord = ItemInventoryTestUtils.buildMockInventoryEventRecord();

		inventoryEventMapperMock
				.when(() -> InventoryEventMapper
						.mapInventoryEventDTORequestToInventoryEventRecord(any(InventoryEventDTORequest.class)))
				.thenReturn(inventoryEventRecord);

		ProducerRecord<String, String> producerRecordMock = any();

		when(inventoryEventsProducerMock.sendEventToTopicAsyncWithProducerRecord(producerRecordMock))
				.thenReturn(kafkaResponseMock);

		InventoryEventDTO inventoryEventDTO = ItemInventoryTestUtils.buildMockInventoryEventDTO();

		inventoryEventMapperMock.when(
				() -> InventoryEventMapper.mapInventoryEventRecordToInventoryEventDTO(any(InventoryEventRecord.class)))
				.thenReturn(inventoryEventDTO);

		InventoryEventDTOResponse inventoryEventDTOResponse = ItemInventoryTestUtils
				.buildMockInventoryEventDTOResponse();

		inventoryEventMapperMock.when(() -> InventoryEventMapper.buildResponseDTO(anyList()))
				.thenReturn(inventoryEventDTOResponse);

		ResponseEntity<ResponseDTO> responseEntity = inventoryEventServiceImpl
				.addNewItemInventory(inventoryEventDTORequest);

		InventoryEventDTOResponse responseDTO = (InventoryEventDTOResponse) responseEntity.getBody();

		List<InventoryEventDTO> responseList = responseDTO.getItemInventoryDTOList();

		assertNotNull(responseList);

		assertEquals(1, responseList.size());

		itemInventoryTestUtilsMock.verify(() -> ItemInventoryTestUtils.buildMockInventoryEventDTORequest());

		itemInventoryTestUtilsMock.verify(() -> ItemInventoryTestUtils.buildMockInventoryEventRecord());

		itemInventoryTestUtilsMock.verify(() -> ItemInventoryTestUtils.buildMockInventoryEventDTO(), times(2));

		itemInventoryTestUtilsMock.verify(() -> ItemInventoryTestUtils.buildMockInventoryEventDTOResponse());

		inventoryEventMapperMock.verify(() -> InventoryEventMapper
				.mapInventoryEventDTORequestToInventoryEventRecord(any(InventoryEventDTORequest.class)));

		verify(inventoryEventsProducerMock).sendEventToTopicAsyncWithProducerRecord(any());

		inventoryEventMapperMock.verify(
				() -> InventoryEventMapper.mapInventoryEventRecordToInventoryEventDTO(any(InventoryEventRecord.class)));

		inventoryEventMapperMock.verify(() -> InventoryEventMapper.buildResponseDTO(anyList()));

		System.out.println("Successful addNewItemInventory_test");
	}
}
