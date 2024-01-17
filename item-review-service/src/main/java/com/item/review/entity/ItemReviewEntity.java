package com.item.review.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("ItemReview")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemReviewEntity {
	@Id
	private String id;
	private String itemInventoryId;
	private String feedback;
	private Double rating;
}
