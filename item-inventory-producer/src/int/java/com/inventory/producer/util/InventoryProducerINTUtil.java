package com.inventory.producer.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.util.ObjectUtils;

import com.inventory.producer.enums.InventoryEventType;
import com.inventory.producer.record.InventoryEventRecord;
import com.inventory.producer.record.ItemRecord;

import net.datafaker.Faker;

public class InventoryProducerINTUtil {
	private static Faker faker;

	public static Faker getFaker() {
		if (ObjectUtils.isEmpty(faker)) {
			faker = new Faker();
		}

		return faker;
	}

	public static List<InventoryEventRecord> generateInventoryEventRecordList(int testDataSize) {
		return IntStream.range(0, testDataSize).mapToObj(i -> {
			if (i % 2 == 0)
				return buildMockInventoryEventRecord(InventoryEventType.NEW);
			
			return buildMockInventoryEventRecord(InventoryEventType.UPDATE);

		}).collect(Collectors.toList());

	}

	public static InventoryEventRecord buildMockInventoryEventRecord(InventoryEventType inventoryEventType) {
		double price = Double.valueOf(getFaker().commerce().price(1, 100));

		ItemRecord itemRecord = ItemRecord.builder()
				.itemId(buildUniqueId())
				.price(price)
				.name(getFaker().commerce().productName())
				.quantity(getFaker().number().numberBetween(0, 10))
				.build();

		return InventoryEventRecord.builder()
				.eventId(buildUniqueId())
				.eventType(inventoryEventType)
				.item(itemRecord)
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
