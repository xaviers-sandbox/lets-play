package com.inventory.producer.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.inventory.producer.enums.InventoryEventType;
import com.inventory.producer.record.InventoryEvent;
import com.inventory.producer.record.Item;

import net.datafaker.Faker;

public class InventoryEventUtils {
	private static Faker f = new Faker();

	public static List<InventoryEvent> generateInventoryEventList(int testDataSize) {
		
		return IntStream.range(0, testDataSize).mapToObj(i -> buildMockInventoryEvent()).collect(Collectors.toList());
	}

	public static InventoryEvent buildMockInventoryEvent() {
		double price = Double.valueOf(f.commerce().price(1, 100));

		Item item = Item.builder()
				.id(f.number().numberBetween(100000, 10000000))
				.price(price)
				.name(f.commerce().productName())
				.quantity(f.number().numberBetween(0, 10))
				.build();

		InventoryEventType inventoryEventType = (item.id() % 2 == 0) ? InventoryEventType.NEW
				: InventoryEventType.UPDATE;

		return InventoryEvent.builder()
				.eventId(f.number().numberBetween(100000, 10000000))
				.eventType(inventoryEventType)
				.item(item)
				.build();
	}
}
