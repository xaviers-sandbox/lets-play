package com.item.review.model.request;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemReviewDTORequest {
	// @NotBlank(message = "ID Cannot Be Empty or Null")
	private String id;

	@NotBlank(message = "ItemInventoryId Cannot Be Empty or Null")
	private String itemInventoryId;

	@NotBlank(message = "Feedback Cannot Be Empty or Null")
	private String feedback;

	@NotNull(message = "Rating Cannot Be Empty or Null")
	@Range(min = 1, max = 5, message = "Rate between 1 and 5")
	// @Positive(message = "Rating Cannot Be Negative")
	// @Max(value = 5, message = "Max Rating is 5.0")
	// @Min(value = 1, message = "Min Rating is 1.0")
	private Double rating;
}
