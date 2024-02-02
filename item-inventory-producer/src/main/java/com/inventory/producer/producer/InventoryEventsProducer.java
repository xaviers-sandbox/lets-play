package com.inventory.producer.producer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.inventory.producer.record.InventoryEvent;
import com.sandbox.util.SandboxUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InventoryEventsProducer {

	private KafkaTemplate<Integer, String> kafkaTemplate;

	@Value("${spring.kafka.topic}")
	public String topicName;

	public InventoryEventsProducer(KafkaTemplate<Integer, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public CompletableFuture<SendResult<Integer, String>> sendEventToTopicAsync(InventoryEvent inventoryEvent) {

		Integer key = inventoryEvent.eventId();
		String value = SandboxUtils.convertObjectToString(inventoryEvent);

		CompletableFuture<SendResult<Integer, String>> kafkaResponse = kafkaTemplate.send(topicName, key, value);

		return kafkaResponse.whenComplete((sendResponse, throwable) -> {
			if (ObjectUtils.isNotEmpty(throwable)) {
				 logKafkaFailure(key, value, throwable);
			} else {
				logKafkaSuccess(key, value, sendResponse);
			}
		});
	}

	public CompletableFuture<SendResult<Integer, String>> sendEventToTopicAsyncWithProducerRecord(
			ProducerRecord<Integer, String> producerRecord) {

		CompletableFuture<SendResult<Integer, String>> kafkaResponse = kafkaTemplate
				.send(producerRecord);

		return kafkaResponse.whenComplete((sendResponse, throwable) -> {
			if (ObjectUtils.isNotEmpty(throwable)) {
				logKafkaFailure(producerRecord.key(), producerRecord.value(), throwable);
			} else {
				logKafkaSuccess(producerRecord.key(), producerRecord.value(), sendResponse);
			}
		});
	}

	public SendResult<Integer, String> sendEventToTopicBlocking(InventoryEvent inventoryEvent)
			throws InterruptedException, ExecutionException {

		Integer eventId = inventoryEvent.eventId();
		String inventoryEventStr = SandboxUtils.convertObjectToString(inventoryEvent);

		SendResult<Integer, String> sendResponse = kafkaTemplate.send(topicName, eventId, inventoryEventStr).get();

		logKafkaSuccess(eventId, inventoryEventStr, sendResponse);

		return sendResponse;
	}

	public void logKafkaFailure(Integer eventId, String inventoryEventStr, Throwable throwable) {
		
		log.error("logKafkaFailure - eventId={} errorMessage={} inventoryEventStr={}",
				eventId,
				throwable.getLocalizedMessage(),
				inventoryEventStr);
	}

	public void logKafkaSuccess(Integer eventId, String inventoryEventStr, SendResult<Integer, String> sendResponse) {
		int partition = sendResponse.getRecordMetadata().partition();
		long offset = sendResponse.getRecordMetadata().offset();
		String topicName = sendResponse.getRecordMetadata().topic();

		log.debug("logKafkaSuccess - eventId={} topicName={} partition={} offset={} inventoryEventStr={}",
				eventId,
				topicName,
				partition,
				offset,
				inventoryEventStr);
	}
}
