package com.inventory.consumer.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.inventory.producer.enums.InventoryEventType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "kafka_details")
@Table(name = "kafka_details")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "topic_name")
	private String topicName;

	@Column(name = "partition_value")
	private Integer partition;

	@Column(name = "offset_value")
	private Long offset;

	@Column(name = "previous_topic_name")
	private String previousTopicName;

	@Column(name = "previous_partition_value")
	private Integer previousPartition;

	@Column(name = "previous_offset_value")
	private Long previousOffset;

	@Enumerated(EnumType.STRING)
	@Column(name = "previous_event_type")
	private InventoryEventType previousEventType;

	@CreationTimestamp
	private Instant createdOn;

	@UpdateTimestamp
	private Instant lastUpdatedOn;
	
	@JoinColumn(name = "event_id", referencedColumnName = "event_id")
	@OneToOne(cascade = CascadeType.ALL)
	@JsonBackReference
	private InventoryEvent inventoryEventKafka;

	@Override
	public String toString() {
		return "KafkaDetails [id=" + id + ", topicName=" + topicName + ", partition=" + partition + ", offset=" + offset
				+ ", previousTopicName=" + previousTopicName + ", previousPartition=" + previousPartition
				+ ", previousOffset=" + previousOffset + ", previousEventType=" + previousEventType + ", createdOn="
				+ createdOn + ", lastUpdatedOn=" + lastUpdatedOn + "]";
	}
}
