package com.item.inventory.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.item.inventory.model.request.ItemInventoryDTORequest;
import com.item.inventory.model.response.ResponseDTO;
import com.item.inventory.proxy.ItemInventoryFeignProxy;
import com.item.inventory.service.ItemInventoryService;
import com.item.review.model.response.ErrorDTOResponse;

import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("v1/item-inventories")
@Slf4j
public class ItemInventoryController {

	private ItemInventoryService itemInventoryService;
	private ItemInventoryFeignProxy itemInventoryFeignProxy;

	public ItemInventoryController(ItemInventoryService itemInventoryService,
			ItemInventoryFeignProxy itemInventoryFeignProxy) {
		this.itemInventoryService = itemInventoryService;
		this.itemInventoryFeignProxy = itemInventoryFeignProxy;
	}
	
	@GetMapping("unsecure-world")
	public String helloWorldNoSecurity() {
		return "Life without security";
	}

	@PostMapping("/app")
	public Mono<ResponseEntity<ResponseDTO>> addNewItemInventory(
			@RequestBody @Valid ItemInventoryDTORequest newItemInventoryDTOrequest) {
		return itemInventoryService.addNewItemInventory(newItemInventoryDTOrequest);
	}

	@GetMapping("/app/{id}")
	public Mono<ResponseEntity<ResponseDTO>> getItemInventoryById(@PathVariable String id) {
		return itemInventoryService.getItemInventoryById(id);
	}

	@GetMapping("/app")
	public Mono<ResponseEntity<ResponseDTO>> getAllItemInventories() {
		return itemInventoryService.getAllItemInventories();
	}

	@PutMapping("/app/{id}")
	public Mono<ResponseEntity<ResponseDTO>> updateItemInventoryById(@PathVariable String id,
			@RequestBody @Valid ItemInventoryDTORequest updatedItemInventoryDTOrequest) {
		return itemInventoryService.updateItemInventoryById(id, updatedItemInventoryDTOrequest);
	}

	@DeleteMapping("/app/delete/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteItemInventoryById(@PathVariable String id) {
		return itemInventoryService.deleteItemInventoryById(id);
	}

	@DeleteMapping("/app/delete/all")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteAllItemInventories() {
		return itemInventoryService.deleteAllItemInventories();
	}

	@PostMapping("/app/init-test-data/{size}")
	public Mono<ResponseEntity<ResponseDTO>> initTestDataBySize(@PathVariable String size) {
		return itemInventoryService.initTestDataBySize(size);
	}

	@GetMapping("/app/item-reviews/{itemInventoryId}")
	@Retry(name = "testing-circuit-breaker", fallbackMethod = "returnDefaultResponse")
	// fires off 10 request per sec command: watch -n 0.1 curl
	// http://localhost:1111/circuit-breaker
	// @CircuitBreaker(name = "default", fallbackMethod = "returnDefaultResponse")
	// @Bulkhead(name = "default")
	// @RateLimiter(name = "default")
	public com.item.review.model.response.ResponseDTO getItemReviewsByItemInventoryIdFeign(
			@PathVariable String itemInventoryId) {
		log.debug("getItemReviewsByItemInventoryIdFeign - itemInventoryId={}", itemInventoryId);

		return itemInventoryFeignProxy.getItemReviews(itemInventoryId);
	}

	public ErrorDTOResponse returnDefaultResponse(Throwable ex) {
		log.debug("returnDefaultResponse - Error occured during junk-breaker call. ex=" + ex.getLocalizedMessage());

		return ErrorDTOResponse.builder()
				.errorMessage("Error occured during junk-breaker call. ex=" + ex.getLocalizedMessage())
				.build();
	}
}
