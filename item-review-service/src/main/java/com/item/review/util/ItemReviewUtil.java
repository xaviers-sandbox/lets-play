package com.item.review.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.item.review.entity.ItemReviewEntity;

import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;

@Slf4j
public class ItemReviewUtil {
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

	public static List<ItemReviewEntity> generateItemReviewEntityList(int resulSetSize) {
		return IntStream.range(0, resulSetSize)
				.mapToObj(i -> ItemReviewUtil.buildMockItemReviewEntity())
				.collect(Collectors.toList());

	}

	public static ItemReviewEntity buildMockItemReviewEntity() {
		String feedback = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

		double rating = Double.valueOf(f.commerce().price(1, 5));

		Long itemInventoryId = f.number().randomNumber(7, false);

		return ItemReviewEntity.builder()
				.id(null)
				.itemInventoryId(itemInventoryId.toString())
				.feedback(feedback)
				.rating(rating)
				.build();
	}

}
