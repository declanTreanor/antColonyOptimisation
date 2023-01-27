package com.eon.ants.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProblemSpaceTest {

	@Autowired
	private ProblemSpace problemSpace;
	@Test
	void determineShortestPath() {
		List<String> path = problemSpace.determineShortestPath();
		assertEquals(6,path.size());
		assertEquals(22.0D,problemSpace.findLength(path));

	}

}