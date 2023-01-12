package com.eon.ants;

import org.apache.commons.lang3.Range;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProblemSpaceTest {
	@Autowired
	private String[] nodeNames;
	@Autowired
	@Qualifier(value = "problemSpaceForTest")
	private ProblemSpace problemSpace;
	@Autowired
	private Ant ant;
	@Autowired
	private ApplicationContext appContext;

	@Test
	void probabilityChoosingPath_circusToFerrisWheel() {
		System.out.println(ant);
		ant.startOn("circus");

		double probability = problemSpace.probabilityChoosingPath(ant, "ferrisWheel");
		double probabilityRounded = Math.round(probability * 1000.0) / 1000.0;
		assertEquals(0.331,probabilityRounded);

	}

	@Test
	void chooseNextNode() {

		this.ant.startOn("circus");
		String nextStep = problemSpace.chooseNextNode(this.ant);
		System.out.println("chosen: "+nextStep);
		assertTrue(new ArrayList<>(Arrays.asList(nodeNames)).contains(nextStep));
	}

	@Test
	void findLength() {
		System.out.println(ant);
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