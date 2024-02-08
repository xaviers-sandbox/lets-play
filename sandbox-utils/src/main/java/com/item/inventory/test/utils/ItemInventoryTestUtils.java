package com.item.inventory.test.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.ObjectUtils;

import com.inventory.producer.enums.InventoryEventType;
import com.inventory.producer.model.record.InventoryEventRecord;
import com.inventory.producer.model.record.ItemRecord;

import net.datafaker.Faker;

public class ItemInventoryTestUtils {
	private static Faker faker;

	public static Faker getFaker() {
		if (ObjectUtils.isEmpty(faker)) {
			faker = new Faker();
		}

		return faker;
	}
	
	public static List<InventoryEventRecord> generateInventoryEventRecordList(int testDataSize) {

		return IntStream.range(0, testDataSize).mapToObj(i -> buildMockInventoryEventRecord()).collect(Collectors.toList());
	}
	
	public static List<ItemRecord> generateItemRecordList(int testDataSize) {

		return IntStream.range(0, testDataSize).mapToObj(i -> buildMockItemRecord()).collect(Collectors.toList());
	}
	
	public static InventoryEventRecord buildMockInventoryEventRecord() {

		int randomNum = getFaker().number().numberBetween(100000, 10000000);

		InventoryEventType inventoryEventType = (randomNum % 2 == 0) ? InventoryEventType.NEW
				: InventoryEventType.UPDATE;

		return InventoryEventRecord.builder()
				.eventId(buildUniqueId())
				.eventType(inventoryEventType)
				.item(buildMockItemRecord())
				.build();
	}

	public static InventoryEventRecord buildMockInventoryEventRecord(InventoryEventType inventoryEventType) {

		return InventoryEventRecord.builder()
				.eventId(ItemInventoryTestUtils.buildUniqueId())
				.eventType(inventoryEventType)
				.item(buildMockItemRecord())
				.build();
	}

	public static ItemRecord buildMockItemRecord() {
		double price = Double.valueOf(getFaker().commerce().price(1, 100));

		return ItemRecord.builder()
				.itemId(ItemInventoryTestUtils.buildUniqueId())
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
