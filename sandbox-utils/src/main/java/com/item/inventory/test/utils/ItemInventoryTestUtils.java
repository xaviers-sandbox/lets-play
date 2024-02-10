package com.item.inventory.test.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.ObjectUtils;

import com.inventory.producer.enums.InventoryEventType;
import com.inventory.producer.model.InventoryEventDTO;
import com.inventory.producer.model.ItemDTO;
import com.inventory.producer.model.record.InventoryEventRecord;
import com.inventory.producer.model.record.ItemRecord;
import com.inventory.producer.model.request.InventoryEventDTORequest;
import com.inventory.producer.model.request.ItemDTORequest;

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

		return IntStream.range(0, testDataSize)
				.mapToObj(i -> buildMockInventoryEventRecord())
				.collect(Collectors.toList());
	}

	public static List<ItemRecord> generateItemRecordList(int testDataSize) {

		return IntStream.range(0, testDataSize).mapToObj(i -> buildMockItemRecord()).collect(Collectors.toList());
	}

	public static InventoryEventRecord buildMockInventoryEventRecord() {

		return InventoryEventRecord.builder()
				.eventId(buildUniqueId())
				.eventType(getRandomInventoryEventType())
				.item(buildMockItemRecord())
				.build();
	}

	public static ItemRecord buildMockItemRecord() {

		return ItemRecord.builder()
				.itemId(buildUniqueId())
				.price(getRandomPrice())
				.name(getRandomName())
				.quantity(getRandomQuantity())
				.build();
	}

	public static InventoryEventDTO buildMockInventoryEventDTO() {

		return InventoryEventDTO.builder()
				.eventId(buildUniqueId())
				.eventType(getRandomInventoryEventType())
				.itemDTO(buildMockItemDTO())
				.build();
	}

	public static ItemDTO buildMockItemDTO() {

		return ItemDTO.builder()
				.itemId(buildUniqueId())
				.price(getRandomPrice())
				.name(getRandomName())
				.quantity(getRandomQuantity())
				.build();
	}

	public static InventoryEventDTORequest buildMockInventoryEventDTORequest() {

		return InventoryEventDTORequest.builder()
				.eventId(buildUniqueId())
				.eventType(getRandomInventoryEventType())
				.item(buildMockItemDTORequest())
				.build();
	}

	public static ItemDTORequest buildMockItemDTORequest() {

		return ItemDTORequest.builder()
				.itemId(buildUniqueId())
				.name(getRandomName())
				.price(getRandomPrice())
				.quantity(getRandomQuantity())
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

	public static int getRandomQuantity() {
		return getFaker().number().numberBetween(0, 10);
	}

	public static String getRandomName() {
		return getFaker().commerce().productName();
	}

	public static double getRandomPrice() {
		return Double.valueOf(getFaker().commerce().price(1, 100));
	}

	public static InventoryEventType getRandomInventoryEventType() {
		int randomNum = getFaker().number().numberBetween(100000, 10000000);

		return (randomNum % 2 == 0) ? InventoryEventType.NEW : InventoryEventType.UPDATE;
	}
}
