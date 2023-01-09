package com.eon.ants;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProblemSpaceTest {
	@Autowired
	private String[] nodeNames;
	@Autowired
	private ProblemSpace problemSpace;
	@Autowired
	private Ant ant;

	@Test
	void probabilityChoosingPath_circusToFerrisWheel() {
		ant.startOn("circus");


		double probability = problemSpace.probabilityChoosingPath(ant, ant.getPathTaken().get(0),"ferrisWheel");
		double probabilityRounded = Math.round(probability * 1000.0) / 1000.0;
		assertEquals(0.392,probabilityRounded);

	}

	@Test
	void findLength() {
		double distance = problemSpace.findLength(getTestPath());
		assertEquals(35.0,distance);

	}

	private List<String> getTestPath() {
		//[circus, balloon, bumpers, carousel, swings, ferrisWheel]
		List exhibits=new ArrayList<>();
		exhibits.add("circus");
		exhibits.add("ferrisWheel");
		exhibits.add("balloon");
		exhibits.add("swings");
		exhibits.add("bumpers");
		exhibits.add("carousel");
		return exhibits;
	}
}