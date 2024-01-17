package com.item.inventory.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Range;

import com.item.inventory.entity.ItemInventoryEntity;
import com.item.inventory.model.ItemInventoryDTO;
import com.item.inventory.model.request.ItemInventoryDTORequest;
import com.item.inventory.model.response.ItemInventoryDTOResponse;

import net.datafaker.Faker;
import reactor.core.publisher.Flux;

public class ItemInventoryTestUtil {
	private static Faker f = new Faker();

	public static ItemInventoryEntity buildMockItemInventoryEntity() {

		double price = Double.valueOf(f.commerce().price(1, 100));

		return ItemInventoryEntity.builder()
				.id(null)
				.price(price)
				.name(f.commerce().productName())
				.quantity(f.number().numberBetween(0, 10))
				.build();
	}

	public static ItemInventoryDTO buildMockItemInventoryDTO() {

		double price = Double.valueOf(f.commerce().price(1, 100));

		return ItemInventoryDTO.builder()
				.id(null)
				.price(price)
				.name(f.commerce().productName())
				.quantity(f.number().numberBetween(0, 10))
				.build();
	}

	public static ItemInventoryDTORequest buildMockItemInventoryDTORequest() {

		double price = Double.valueOf(f.commerce().price(1, 100));

		return ItemInventoryDTORequest.builder()
				.id(null)
				.price(price)
				.name(f.commerce().productName())
				.quantity(f.number().numberBetween(1, 10))
				.build();
	}

	public static ItemInventoryDTOResponse buildMockItemInventoryDTOResponse(int resulSetSize) {
		if (resulSetSize <= 0) {
			return ItemInventoryDTOResponse.builder()
					.resultSetSize(0)
					.itemInventoryDTOList(new ArrayList<ItemInventoryDTO>())
					.build();
		} else {
			List<ItemInventoryDTO> itemInventoryDTOList = generateItemInventoryDTOList(resulSetSize);
			return ItemInventoryDTOResponse.builder()
					.resultSetSize(resulSetSize)
					.itemInventoryDTOList(itemInventoryDTOList)
					.build();
		}
	}

	public static List<ItemInventoryDTO> generateItemInventoryDTOList(int resulSetSize) {
		List<ItemInventoryDTO> itemInventoryDTOList = Flux.range(1, resulSetSize)
				.map(i -> ItemInventoryTestUtil.buildMockItemInventoryDTO())
				.collectList()
				.block();

		return itemInventoryDTOList;
	}

	public static List<ItemInventoryEntity> generateItemInventoryEntityList(int resulSetSize) {
		List<ItemInventoryEntity> itemInventoryEntityList = Flux.range(1, resulSetSize)
				.map(i -> ItemInventoryTestUtil.buildMockItemInventoryEntity())
				.collectList()
				.block();

		return itemInventoryEntityList;
	}

	public static boolean isBetweenTwoNums(int quantity, int numGT, int numLT) {

		Range<Integer> range = Range.of(numGT, numLT);

		return range.contains(quantity);
	}

	public static boolean isBetweenTwoNums(Double price, double numGT, double numLT) {
		Range<Double> range = Range.of(numGT, numLT);

		return range.contains(price);
	}
}
