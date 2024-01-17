package com.item.review.util;

import java.util.ArrayList;
import java.util.List;

import com.item.review.entity.ItemReviewEntity;
import com.item.review.model.ItemReviewDTO;
import com.item.review.model.request.ItemReviewDTORequest;
import com.item.review.model.response.ItemReviewDTOResponse;

import net.datafaker.Faker;
import reactor.core.publisher.Flux;

public class ItemReviewTestUtil {
	private static Faker f = new Faker();

	public static List<ItemReviewEntity> generateItemReviewEntityList(int resulSetSize) {
		return Flux.range(1, resulSetSize)
				.map(i -> ItemReviewTestUtil.buildMockItemReviewEntity())
				.collectList()
				.block();
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

	public static ItemReviewDTORequest buildMockItemReviewDTORequest() {
		String feedback = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

		double rating = Double.valueOf(f.commerce().price(1, 5));

		Long itemInventoryId = f.number().randomNumber(7, false);

		return ItemReviewDTORequest.builder()
				.id(null)
				.itemInventoryId(itemInventoryId.toString())
				.feedback(feedback)
				.rating(rating)
				.build();
	}

	public static ItemReviewDTO buildMockItemReviewDTO() {
		String feedback = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

		double rating = Double.valueOf(f.commerce().price(1, 5));

		Long itemInventoryId = f.number().randomNumber(7, false);

		return ItemReviewDTO.builder()
				.id(null)
				.itemInventoryId(itemInventoryId.toString())
				.feedback(feedback)
				.rating(rating)
				.build();
	}

	public static ItemReviewDTOResponse buildMockItemReviewDTOResponse(int resulSetSize) {
		if (resulSetSize <= 0) {
			return ItemReviewDTOResponse.builder()
					.resultSetSize(0)
					.itemReviewDTOList(new ArrayList<ItemReviewDTO>())
					.build();
		} else {
			List<ItemReviewDTO> itemReviewDTOList = generateItemReviewDTOList(resulSetSize);
			return ItemReviewDTOResponse.builder()
					.resultSetSize(resulSetSize)
					.itemReviewDTOList(itemReviewDTOList)
					.build();
		}
	}

	public static List<ItemReviewDTO> generateItemReviewDTOList(int resulSetSize) {
		List<ItemReviewDTO> itemInventoryDTOList = Flux.range(1, resulSetSize)
				.map(i -> ItemReviewTestUtil.buildMockItemReviewDTO())
				.collectList()
				.block();

		return itemInventoryDTOList;
	}

//	public static boolean isBetweenTwoNums(int quantity, int numGT, int numLT) {
//
//		Range<Integer> range = Range.of(numGT, numLT);
//
//		return range.contains(quantity);
//	}
//
//	public static boolean isBetweenTwoNums(Double price, double numGT, double numLT) {
//		Range<Double> range = Range.of(numGT, numLT);
//
//		return range.contains(price);
//	}
}
