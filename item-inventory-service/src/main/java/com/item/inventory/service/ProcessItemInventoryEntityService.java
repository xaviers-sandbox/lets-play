package com.item.inventory.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.item.inventory.entity.ItemInventoryEntity;
import com.item.inventory.mapper.ItemInventoryMapper;
import com.item.inventory.model.ItemInventoryDTO;
import com.item.inventory.model.response.ResponseDTO;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public abstract class ProcessItemInventoryEntityService {
	public Mono<ResponseEntity<ResponseDTO>> processItemInventoryEntityMono(
			Mono<ItemInventoryEntity> itemInventoryEntityMono, HttpStatus httpStatus) {

		log.debug("processItemInventoryEntityMono");

		return itemInventoryEntityMono.flatMap(returnedItemInventoryEntity -> {
			ResponseEntity<ResponseDTO> responseEntity = Stream
					.of(ItemInventoryMapper.mapItemInventoryEntityToItemInventoryDTO(returnedItemInventoryEntity))
					.map(itemInventoryDTO -> {
						List<ItemInventoryDTO> itemInventoryDTOList = new ArrayList<>(Arrays.asList(itemInventoryDTO));
						return ItemInventoryMapper.buildItemInventoryDTOResponse(itemInventoryDTOList);
					})
					.map(itemInventoryDTOResponse -> ItemInventoryMapper
							.buildResponseEntityWithDTOResponse(itemInventoryDTOResponse, httpStatus))
					.findAny()
					.get();

			return Mono.just(responseEntity);
		});
	}

	public Mono<ResponseEntity<ResponseDTO>> processItemInventoryEntityFlux(
			Flux<ItemInventoryEntity> itemInventoryEntityFlux) {

		log.debug("processItemInventoryEntityFlux");

		return itemInventoryEntityFlux.collectList().flatMap(itemInventoryEntityList -> {

			ResponseEntity<ResponseDTO> responseEntity = Stream
					.of(ItemInventoryMapper.mapItemInventoryEntityListToItemInventoryDTOList(itemInventoryEntityList))
					.map(ItemInventoryMapper::buildItemInventoryDTOResponse)
					.map(itemInventoryDTOResponse -> ItemInventoryMapper
							.buildResponseEntityWithDTOResponse(itemInventoryDTOResponse, HttpStatus.OK))
					.findAny()
					.get();

			return Mono.just(responseEntity);
		});
	}
}
