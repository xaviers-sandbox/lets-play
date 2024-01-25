package com.item.inventory.service.management.impll;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.item.inventory.entity.ItemInventoryEntity;
import com.item.inventory.mapper.ItemInventoryMapper;
import com.item.inventory.model.response.ResponseDTO;
import com.item.inventory.repository.ItemInventoryRepository;
import com.item.inventory.service.ProcessItemInventoryEntityService;
import com.item.inventory.service.management.DatabaseManagementService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class DatabaseManagementServiceImpl extends ProcessItemInventoryEntityService implements DatabaseManagementService {

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
}
