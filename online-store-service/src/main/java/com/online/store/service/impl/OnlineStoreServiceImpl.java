package com.online.store.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.item.inventory.model.ItemInventoryDTO;
import com.item.inventory.model.response.ItemInventoryDTOResponse;
import com.item.review.model.response.ItemReviewDTOResponse;
import com.online.store.client.ItemInventoyWebClient;
import com.online.store.client.ItemReviewWebClient;
import com.online.store.mapper.OnlineStoreMapper;
import com.online.store.service.OnlineStoreService;
import com.online.store.service.model.OnlineStoreDTOResponse;
import com.sandbox.util.SandboxUtils;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OnlineStoreServiceImpl implements OnlineStoreService {
	private ItemInventoyWebClient itemInventoyWebClient;
	private ItemReviewWebClient itemReviewWebClient;

	public OnlineStoreServiceImpl(ItemInventoyWebClient itemInventoyWebClient,
			ItemReviewWebClient itemReviewWebClient) {

		this.itemInventoyWebClient = itemInventoyWebClient;
		this.itemReviewWebClient = itemReviewWebClient;
	}

	@Override
	public Mono<ServerResponse> getAllItemReviews() {
		log.debug("getAllItemReviews");
		Mono<ItemReviewDTOResponse> itemReviewDTOResponseMono = itemReviewWebClient.getAllItemReviews();

		return processItemReviewDTOResponseMono(itemReviewDTOResponseMono, HttpStatus.OK);
	}

	@Override
	public Mono<ServerResponse> getItemReviewById(String id) {
		log.debug("getItemReviewById id={}", id);
		Mono<ItemReviewDTOResponse> itemReviewDTOResponseMono = itemReviewWebClient.getItemReviewById(id);

		return processItemReviewDTOResponseMono(itemReviewDTOResponseMono, HttpStatus.OK);
	}

	@Override
	public Mono<ServerResponse> getItemReviewsByItemInventoryId(String itemInventoryId) {
		log.debug("getItemReviewsByItemInventoryId - itemInventoryId={}", itemInventoryId);

		Mono<ItemReviewDTOResponse> itemReviewDTOResponseMono = itemReviewWebClient
				.getItemReviewByItemInventoryId(itemInventoryId);

		return processItemReviewDTOResponseMono(itemReviewDTOResponseMono, HttpStatus.OK);
	}

	@Override
	public Mono<ServerResponse> getAllItemInventories() {
		log.debug("getAllItemInventories");

		Mono<ItemInventoryDTOResponse> itemInventoryDTOResponseMono = itemInventoyWebClient.getAllItemInventories();

		return processItemInventoryDTOResponseMono(itemInventoryDTOResponseMono, HttpStatus.OK);
	}

	@Override
	public Mono<ServerResponse> getItemInventoryById(String id) {
		log.debug("getItemInventoryById id={}", id);

		Mono<ItemInventoryDTOResponse> itemInventoryDTOResponseMono = itemInventoyWebClient
				.getItemInventoryByItemInventoryId(id);

		return processItemInventoryDTOResponseMono(itemInventoryDTOResponseMono, HttpStatus.OK);
	}

	@Override
	public Mono<ServerResponse> getItemDetailsByItemInventoryId(String itemInventoryId) {
		log.debug("getItemDetailsByItemInventoryId itemInventoryId={}", itemInventoryId);

		Mono<ItemInventoryDTOResponse> itemInventoryDTOResponseMono = itemInventoyWebClient
				.getItemInventoryByItemInventoryId(itemInventoryId);
		log.debug("100000");
		Mono<ServerResponse> tmp = itemInventoryDTOResponseMono.flatMap(itemInventoryDTOResponse -> {
			log.debug("200000");
			ItemInventoryDTO itemInventoryDTO = itemInventoryDTOResponse.getItemInventoryDTOList()
					.stream()
					.findFirst()
					.get();

			log.debug("300000 itemInventoryDTO=" + itemInventoryDTO);
			if (!ObjectUtils.isEmpty(itemInventoryDTO)) {
				log.debug("400000");
				Mono<ItemReviewDTOResponse> itemReviewDTOResponseMono = itemReviewWebClient
						.getItemReviewByItemInventoryId(itemInventoryDTO.getId());

				log.debug("500000 itemReviewDTOResponseMono=");

				return itemReviewDTOResponseMono.flatMap(itemReviewDTOResponse -> {
					log.debug("600000");
					OnlineStoreDTOResponse onlineStoreDTOResponse = OnlineStoreMapper
							.buildOnlineStoreDTOResponsWithItemInventoryDTOResponseAndItemReviewDTOResponse(
									itemInventoryDTOResponse,
									itemReviewDTOResponse);
					log.debug("700000");
					return OnlineStoreMapper.buildServerResponseMonoWithDTOResponse(onlineStoreDTOResponse,
							HttpStatus.OK);
				}).switchIfEmpty(processItemInventoryDTOResponseMono(itemInventoryDTOResponseMono, HttpStatus.OK));
				// return processItemInventoryDTOResponseMono(itemInventoryDTOResponseMono,
				// HttpStatus.OK);

			}
			log.debug("800000");
			return processItemInventoryDTOResponseMono(itemInventoryDTOResponseMono, HttpStatus.OK);
		}).switchIfEmpty(OnlineStoreMapper.buildDefaultEmptyServerResponseMono());

		return tmp;
	}

	@Override
	public Mono<ServerResponse> processItemReviewDTOResponseMono(Mono<ItemReviewDTOResponse> itemReviewDTOResponseMono,
			HttpStatus httpStatus) {

		return itemReviewDTOResponseMono.flatMap(itemReviewDTOResponse -> {
			log.debug("processItemReviewDTOResponseMono itemReviewDTOResponseMono={}",
					SandboxUtils.getPrettyPrintJsonFromObject(itemReviewDTOResponse));

			OnlineStoreDTOResponse onlineStoreDTOResponse = OnlineStoreMapper
					.buildOnlineStoreDTOResponseWithItemReviewDTOResponse(itemReviewDTOResponse);

			return OnlineStoreMapper.buildServerResponseMonoWithDTOResponse(onlineStoreDTOResponse, httpStatus);
		});
	}

	@Override
	public Mono<ServerResponse> processItemInventoryDTOResponseMono(
			Mono<ItemInventoryDTOResponse> itemInventoryDTOResponseMono, HttpStatus httpStatus) {

		return itemInventoryDTOResponseMono.flatMap(itemInventoryDTOResponse -> {
			log.debug("processItemInventoryDTOResponseMono itemInventoryDTOResponse={}",
					SandboxUtils.getPrettyPrintJsonFromObject(itemInventoryDTOResponse));

			OnlineStoreDTOResponse onlineStoreDTOResponse = OnlineStoreMapper
					.buildOnlineStoreDTOResponsWithItemInventoryDTOResponse(itemInventoryDTOResponse);

			return OnlineStoreMapper.buildServerResponseMonoWithDTOResponse(onlineStoreDTOResponse, httpStatus);
		});
	}
}
