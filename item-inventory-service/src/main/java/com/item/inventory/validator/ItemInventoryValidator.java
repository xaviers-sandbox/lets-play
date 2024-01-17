package com.item.inventory.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ItemInventoryValidator {

	public int checkInitTestDataSize(String testDataSize) {
		return StringUtils.isNumeric(testDataSize) ? Integer.valueOf(testDataSize) : 0;
	}
}
