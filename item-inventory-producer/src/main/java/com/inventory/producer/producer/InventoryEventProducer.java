package com.inventory.producer.producer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.inventory.producer.model.record.InventoryEventRecord;
import com.sandbox.util.SandboxUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InventoryEventProducer {

	private KafkaTemplate<String, String> kafkaTemplate;

	public String topicName;

	public InventoryEventProducer(KafkaTemplate<String, String> kafkaTemplate, @Value("${spring.kafka.topic-name}") String topicName) {
		this.kafkaTemplate = kafkaTemplate;
		this.topicName = topicName;
	}

	public CompletableFuture<SendResult<String, String>> sendEventToTopicAsyncWithProducerRecord(
			ProducerRecord<String, String> producerRecord) {

		CompletableFuture<SendResult<String, String>> kafkaResponse = kafkaTemplate
				.send(producerRecord);

		return kafkaResponse.whenComplete((sendResponse, throwable) -> {
			if (ObjectUtils.isNotEmpty(throwable)) {
				logKafkaFailure(producerRecord.key(), producerRecord.value(), throwable);
			} else {
				logKafkaSuccess(producerRecord.key(), producerRecord.value(), sendResponse);
			}
		});
	}

	public SendResult<String, String> sendEventToTopicBlocking(InventoryEventRecord inventoryEventRecord)
			throws InterruptedException, ExecutionException {

		String eventId = inventoryEventRecord.eventId();
		String inventoryEventStr = SandboxUtils.convertObjectToString(inventoryEventRecord);

		SendResult<String, String> sendResponse = kafkaTemplate.send(topicName, eventId, inventoryEventStr).get();

		logKafkaSuccess(eventId, inventoryEventStr, sendResponse);

		return sendResponse;
	}

	public void logKafkaFailure(String eventId, String inventoryEventStr, Throwable throwable) {
		
		log.error("logKafkaFailure - eventId={} errorMessage={} inventoryEventStr={}",
				eventId,
				throwable.getLocalizedMessage(),
				inventoryEventStr);
	}

	public void logKafkaSuccess(String eventId, String inventoryEventStr, SendResult<String, String> sendResponse) {
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
