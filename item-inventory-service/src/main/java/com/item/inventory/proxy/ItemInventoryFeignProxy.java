package com.item.inventory.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.item.review.model.response.ItemReviewDTOResponse;

//@FeignClient(name = "item-review", url = "localhost:2222")
@FeignClient(name = "item-review-service")
public interface ItemInventoryFeignProxy {

	@GetMapping("/v1/item-reviews/app?itemInventoryId={itemInventoryId}")
	public ItemReviewDTOResponse getItemReviews(@PathVariable String itemInventoryId);

}
