package com.inventory.consumer.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.inventory.producer.enums.InventoryEventType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "inventory_event")
@Table(name = "inventory_event")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryEvent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "event_id")
	private Integer eventId;

	@Enumerated(EnumType.STRING)
	@Column(name = "event_type")
	private InventoryEventType eventType;

	
    @OneToOne(mappedBy = "inventoryEventItem", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Item item;
 
    @OneToOne(mappedBy = "inventoryEventKafka", cascade = CascadeType.ALL)
    @JsonManagedReference
    private KafkaDetails kafkaDetails;

	@Override
	public String toString() {
		return "InventoryEvent [eventId=" + eventId + ", eventType=" + eventType + ", item=" + item + ", kafkaDetails="
				+ kafkaDetails + "]";
	}
}
