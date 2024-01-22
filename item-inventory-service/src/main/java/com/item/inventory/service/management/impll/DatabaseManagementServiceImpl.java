package com.item.inventory.service.management.impll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.item.inventory.entity.ItemInventoryEntity;
import com.item.inventory.mapper.ItemInventoryMapper;
import com.item.inventory.model.ItemInventoryDTO;
import com.item.inventory.model.response.ItemInventoryDTOResponse;
import com.item.inventory.model.response.ResponseDTO;
import com.item.inventory.repository.ItemInventoryRepository;
import com.item.inventory.service.management.DatabaseManagementService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class DatabaseManagementServiceImpl implements DatabaseManagementService {

	private ItemInventoryRepository itemInventoryRepository;

	public DatabaseManagementServiceImpl(ItemInventoryRepository itemInventoryRepository) {
		this.itemInventoryRepository = itemInventoryRepository;
	}

	@Override
	public Mono<ResponseEntity<ResponseDTO>> getAllItemInventoriesFromDBManagement() {
		log.debug("getAllItemInventoriesFromDBManagement");

		Flux<ItemInventoryEntity> itemInventoryEntityFlux = itemInventoryRepository.findAllByOrderByNameAsc();

		return processItemInventoryEntityFlux(itemInventoryEntityFlux);
	}

	@Override
	public Mono<ResponseEntity<ResponseDTO>> getItemInventoryByIdFromDBManagement(String id) {
		log.debug("getItemInventoryByIdFromDBManagement - id={}", id);

		Mono<ItemInventoryEntity> itemInventoryEntityMono = itemInventoryRepository.findById(id);

		return processItemInventoryEntityMono(itemInventoryEntityMono, HttpStatus.OK)
				.switchIfEmpty(Mono.just(ItemInventoryMapper.generateItemNotFoundResponse()));
	}

	// this uses streams. the others to streams
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

			List<ItemInventoryDTO> itemInventoryDTOList = ItemInventoryMapper
					.mapItemInventoryEntityListToItemInventoryDTOList(itemInventoryEntityList);

			ItemInventoryDTOResponse itemInventoryDTOResponse = ItemInventoryMapper
					.buildItemInventoryDTOResponse(itemInventoryDTOList);

			ResponseEntity<ResponseDTO> responseEntity = ItemInventoryMapper
					.buildResponseEntityWithDTOResponse(itemInventoryDTOResponse, HttpStatus.OK);

			return Mono.just(responseEntity);
		});
	}

}
