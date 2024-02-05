package com.inventory.consumer.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	@Column(name = "kafka_details_id")
	private Integer kafkaDetailsId;

	@Column(name = "orig_topic_name")
	private String origTopicName;

	@Column(name = "orig_partition")
	private Integer origPartition;

	@Column(name = "orig_offset")
	private Long origOffset;

	@CreationTimestamp
	private Instant createdOn;

	@UpdateTimestamp
	private Instant lastUpdatedOn;

	@JoinColumn(name = "event_id")
	@OneToOne
	@JsonBackReference
	private InventoryEvent inventoryEventKafka;

	@Override
	public String toString() {
		return "KafkaDetails [kafkaDetailsId=" + kafkaDetailsId + ", origTopicName=" + origTopicName + ", origPartition=" + origPartition
				+ ", origOffset=" + origOffset + ", createdOn=" + createdOn + ", lastUpdatedOn=" + lastUpdatedOn + "]";
	}
}
