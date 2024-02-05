package hacker.rank.com;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import com.inventory.producer.model.InventoryEventDTO;
import com.inventory.producer.model.InventoryEventDTOResponse;
import com.inventory.producer.model.ResponseDTO;
import com.inventory.producer.producer.InventoryEventProducer;
import com.inventory.producer.record.InventoryEvent;
import com.inventory.producer.service.impl.InventoryEventServiceImpl;
import com.inventory.producer.util.InventoryEventUtils;

@ActiveProfiles("test")
public class InventoryEventServiceTest {
	private static final int TEST_LIST_SIZE = 2;

	@InjectMocks
	private InventoryEventServiceImpl inventoryEventServiceImpl;

	@Mock
	private InventoryEventProducer inventoryEventsProducerMock;

	@MockBean
	private CompletableFuture<SendResult<Integer, String>> kafkaResponseMock;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		ReflectionTestUtils.setField(inventoryEventServiceImpl, "topicName", "inventory-events");
	}

	@Test
	void postItemEventsTest() {
		System.out.println("\n\npostItemEventsTest");
		
		List<InventoryEvent> inventoryEventListMock = InventoryEventUtils
				.generateInventoryEventList(TEST_LIST_SIZE);

		MockedStatic<InventoryEventUtils> inventoryProducerUtilsMock = Mockito.mockStatic(InventoryEventUtils.class);

		inventoryProducerUtilsMock.when(() -> InventoryEventUtils.generateInventoryEventList(any(Integer.class)))
				.thenReturn(inventoryEventListMock);

		ProducerRecord<Integer, String> producerRecordMock = any();

		when(inventoryEventsProducerMock.sendEventToTopicAsyncWithProducerRecord(producerRecordMock))
				.thenReturn(kafkaResponseMock);

		ResponseEntity<ResponseDTO> responseEntity = inventoryEventServiceImpl.addNewMockItems(TEST_LIST_SIZE);

		InventoryEventDTOResponse responseDTO = (InventoryEventDTOResponse)responseEntity.getBody();
		
		List<InventoryEventDTO> responseList = responseDTO.getItemInventoryDTOList();
		
		assertNotNull(responseList);
		assertEquals(TEST_LIST_SIZE, responseList.size());
		
		for(int i = 0; i < responseList.size(); i++) {
			assertEquals(responseList.get(i).getEventId(), inventoryEventListMock.get(i).eventId());
			assertEquals(responseList.get(i).getEventType(), inventoryEventListMock.get(i).eventType());
			assertNotNull(responseList.get(i).getItemDTO());
		}
		
		System.out.println("Successful postItemEventsTest");
	}
}
