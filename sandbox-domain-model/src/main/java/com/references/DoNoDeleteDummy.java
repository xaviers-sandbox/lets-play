package com.references;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
//@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class DoNoDeleteDummy {
	@NotBlank(message = "ID Cannot Be Empty or Null")
	private String id;

	@NotNull(message = "Price Cannot Be Empty or Null")
	@Positive(message = "Price Cannot Be Negative")
	private Double price;

	@NotBlank(message = "Name Cannot Be Empty or Null")
	private String name;

	@NotNull(message = "Quantity Cannot Be Empty or Null")
	@Positive(message = "Quantity Cannot Be Negative")
	private Integer quantity;

	@NotNull(message = "randomStringList Cannot Be Empty or Null")
	private List<@NotNull(message = "randomStringList Cannot Be Empty or Null") @NotBlank(message = "randomStringList Cannot Be Empty or Null") String> randomStringList;
}
