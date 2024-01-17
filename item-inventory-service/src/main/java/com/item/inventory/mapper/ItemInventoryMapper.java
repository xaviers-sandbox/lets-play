package com.item.inventory.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.item.inventory.entity.ItemInventoryEntity;
import com.item.inventory.model.ItemInventoryDTO;
import com.item.inventory.model.request.ItemInventoryDTORequest;
import com.item.inventory.model.response.ErrorDTOResponse;
import com.item.inventory.model.response.ItemInventoryDTOResponse;
import com.item.inventory.model.response.ResponseDTO;

public class ItemInventoryMapper {

	public static ItemInventoryEntity mapItemInventoryDTOrequestToItemInventoryEntity(
			ItemInventoryDTORequest itemInventoryDTORequest) {

		return ItemInventoryEntity.builder()
				.id(itemInventoryDTORequest.getId())
				.name(itemInventoryDTORequest.getName())
				.price(itemInventoryDTORequest.getPrice())
				.quantity(itemInventoryDTORequest.getQuantity())
				.build();
	}

	public static ItemInventoryDTO mapItemInventoryEntityToItemInventoryDTO(ItemInventoryEntity itemInventoryEntity) {

		return ItemInventoryDTO.builder()
				.id(itemInventoryEntity.getId())
				.name(itemInventoryEntity.getName())
				.price(itemInventoryEntity.getPrice())
				.quantity(itemInventoryEntity.getQuantity())
				.build();
	}

	public static List<ItemInventoryDTO> mapItemInventoryEntityListToItemInventoryDTOList(
			List<ItemInventoryEntity> itemInventoryEntityList) {

		List<ItemInventoryDTO> itemInventoryDTOList = itemInventoryEntityList.parallelStream().map(i -> {
			return mapItemInventoryEntityToItemInventoryDTO(i);
		}).collect(Collectors.toList());

		return itemInventoryDTOList;
	}

	public static ItemInventoryEntity buildUpdatedItemInventoryEntityWithOrigEntityAndNewDTO(
			ItemInventoryEntity origItemInventory, ItemInventoryDTORequest updatedItemInventoryDTOrequest) {

		return ItemInventoryEntity.builder()
				.id(origItemInventory.getId())
				.name(updatedItemInventoryDTOrequest.getName())
				.price(updatedItemInventoryDTOrequest.getPrice())
				.quantity(updatedItemInventoryDTOrequest.getQuantity())
				.build();
	}

	public static ItemInventoryDTOResponse buildItemInventoryDTOResponse(List<ItemInventoryDTO> itemInventoryDTOList) {

		return ItemInventoryDTOResponse.builder()
				.itemInventoryDTOList(itemInventoryDTOList)
				.resultSetSize(itemInventoryDTOList.size())
				.build();
	}

	public static ResponseEntity<ResponseDTO> buildResponseEntityWithDTOResponse(ResponseDTO responseDTO,
			HttpStatus httpStatus) {

		return ResponseEntity.status(httpStatus).body(responseDTO);
	}

	public static ResponseEntity<ResponseDTO> generateItemNotFoundResponse() {

		return buildResponseEntityWithDTOResponse(buildItemInventoryDTOResponse(new ArrayList<>()),
				HttpStatus.NOT_FOUND);
	}

	public static ErrorDTOResponse buildErrorDTOResponse(String errorMessage, HttpStatus httpStatus) {
		return ErrorDTOResponse.builder().errorMessage(errorMessage).status(httpStatus.getReasonPhrase()).build();
	}
}
