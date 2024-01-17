package com.item.review.validation;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.item.review.model.request.ItemReviewDTORequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ItemReviewValidator {
	private Validator validator;

	public ItemReviewValidator(Validator validator) {
		this.validator = validator;
	}

	public String checkItemReviewDTORequestValidations(ItemReviewDTORequest itemReviewDTORequest) {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		log.debug("classMethod={} - Validating ItemReviewDTORequest itemReviewDTORequest={}",
				methodName,
				itemReviewDTORequest);

		Set<ConstraintViolation<ItemReviewDTORequest>> constraintViolationsSet = validator
				.validate(itemReviewDTORequest);

		if (!CollectionUtils.isEmpty(constraintViolationsSet)) {
			String errorMessages = constraintViolationsSet.stream()
					.map(ConstraintViolation::getMessage)
					.sorted()
					.collect(Collectors.joining(", "));

			log.error("classMethod={} - errors={}", methodName, errorMessages);
			return errorMessages;
		}

		return "";
	}

	public int checkInitTestDataSize(String size) {
		return StringUtils.isNumeric(size) ? Integer.valueOf(size) : 0;
	}
}
