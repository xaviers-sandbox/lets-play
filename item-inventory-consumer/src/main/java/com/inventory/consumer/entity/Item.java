package com.inventory.consumer.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
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

@Entity(name = "item")
@Table(name = "item")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "item_id", unique = true)
	private String itemId;

	@Column(name = "name")
	private String name;

	@Column(name = "price")
	private Double price;

	@Column(name = "quantity")
	private Integer quantity;

	@JoinColumn(name = "event_id", referencedColumnName = "event_id")
	@OneToOne(cascade = CascadeType.ALL)
	@JsonBackReference
	private InventoryEvent inventoryEventItem;

	@Override
	public String toString() {
		return "Item [id=" + id + ", itemId=" + itemId + ", name=" + name + ", price=" + price + ", quantity="
				+ quantity + "]";
	}
}
