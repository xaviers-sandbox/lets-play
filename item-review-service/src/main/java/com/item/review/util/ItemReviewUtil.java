package com.item.review.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.item.review.entity.ItemReviewEntity;

import net.datafaker.Faker;

public class ItemReviewUtil {
	private static Faker f = new Faker();

	public static List<ItemReviewEntity> generateItemReviewEntityList(int resulSetSize) {
		return IntStream.range(0, resulSetSize)
				.mapToObj(i -> ItemReviewUtil.buildMockItemReviewEntity())
				.collect(Collectors.toList());

	}

	public static ItemReviewEntity buildMockItemReviewEntity() {
		// String feedback = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

		String feedback = f.shakespeare().asYouLikeItQuote();

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
