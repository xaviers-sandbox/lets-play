package com.inventory.producer.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.ObjectUtils;

import com.inventory.producer.enums.InventoryEventType;
import com.inventory.producer.record.InventoryEvent;
import com.inventory.producer.record.Item;

import net.datafaker.Faker;

public class InventoryEventUtils {
	private static Faker faker;

	public static Faker getFaker() {
		if (ObjectUtils.isEmpty(faker)) {
			faker = new Faker();
		}

		return faker;
	}

	public static List<InventoryEvent> generateInventoryEventList(int testDataSize) {

		return IntStream.range(0, testDataSize).mapToObj(i -> buildMockInventoryEvent()).collect(Collectors.toList());
	}

	public static InventoryEvent buildMockInventoryEvent() {

		int randomNum = getFaker().number().numberBetween(100000, 10000000);

		InventoryEventType inventoryEventType = (randomNum % 2 == 0) ? InventoryEventType.NEW
				: InventoryEventType.UPDATE;

		return InventoryEvent.builder()
				.eventId(buildUniqueId())
				.eventType(inventoryEventType)
				.item(buildMockItem())
				.build();
	}

	public static List<Item> generateItemList(int testDataSize) {

		return IntStream.range(0, testDataSize).mapToObj(i -> buildMockItem()).collect(Collectors.toList());
	}

	public static Item buildMockItem() {
		double price = Double.valueOf(getFaker().commerce().price(1, 100));

		return Item.builder()
				.itemId(buildUniqueId())
				.price(price)
				.name(getFaker().commerce().productName())
				.quantity(getFaker().number().numberBetween(0, 10))
				.build();
	}

	public static String buildUniqueId() {
		String lorem1 = getFaker().lorem().characters(5);
		int num1 = getFaker().number().numberBetween(1000, 9999);
		String lorem2 = getFaker().lorem().characters(5);
		long num2 = getFaker().number().numberBetween(1000, 9999);

		return new StringBuilder().append(lorem1)
				.append("-")
				.append(num1)
				.append("-")
				.append(lorem2)
				.append("-")
				.append(num2)
				.toString();
	}
}
