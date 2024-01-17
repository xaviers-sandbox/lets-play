package com.item.inventory.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.item.inventory.entity.ItemInventoryEntity;

import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;

@Slf4j
public class ItemInventoryUtil {
	private static Faker f = new Faker();

	public static void prettyPrintObjectToJson(Object anyObject) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			mapper.writeValueAsString(anyObject);

			log.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(anyObject));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public static List<ItemInventoryEntity> generateItemInventoryEntityList(int testDataSize) {
		return IntStream.range(0, testDataSize)
				.mapToObj(i -> ItemInventoryUtil.buildMockItemInventoryEntity())
				.collect(Collectors.toList());

	}

	public static ItemInventoryEntity buildMockItemInventoryEntity() {
		double price = Double.valueOf(f.commerce().price(1, 100));

		return ItemInventoryEntity.builder()
				.id(null)
				.price(price)
				.name(f.commerce().productName())
				.quantity(f.number().numberBetween(0, 10))
				.build();
	}

}
