package com.online.store;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlaygroundUnitTest {

	@Test
	public void testStringFormatter() {
		String tmp = String.format("Some %s, %g, %s", "apples", 11.5, "oranges");
		log.debug(tmp);
	}
}
