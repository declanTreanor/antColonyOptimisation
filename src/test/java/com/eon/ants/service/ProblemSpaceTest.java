package com.eon.ants.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProblemSpaceTest {

	@Autowired
	private ProblemSpace problemSpace;
	@Test
	void determineShortestPath() {
		System.out.println(problemSpace.determineShortestPath());
	}
}