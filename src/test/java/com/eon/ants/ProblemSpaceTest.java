package com.eon.ants;

import com.eon.ants.domain.Ant;
import com.eon.ants.service.AntsProbablePath;
import com.eon.ants.service.ProblemSpace;
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
	@Qualifier(value = "med")
	private String[] nodeNames;
	@Autowired
	private ProblemSpace problemSpace;
	@Autowired
	private Ant ant;


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

	@Test
	void getNodeNames() {
		/**
		 * swings 0.06648930531397114
		 * circus 0.1257063428592267
		 * carousel 0.16418787638756138
		 * bumpers 0.3218082377196204
		 * ferrisWheel 0.3218082377196204
		 */
		List<AntsProbablePath> listOfAntsProbablePaths = new ArrayList<>();

		listOfAntsProbablePaths.add(new AntsProbablePath("swings", 0.06648930531397114));
		listOfAntsProbablePaths.add(new AntsProbablePath("circus", 0.1257063428592267));//List<AntsProbablePath> antsProbablePaths, double upperBound
		listOfAntsProbablePaths.add(new AntsProbablePath("carousel", 0.16418787638756138));//List<AntsProbablePath> antsProbablePaths, double upperBound
		listOfAntsProbablePaths.add(new AntsProbablePath("bumpers", 0.3218082377196204));//List<AntsProbablePath> antsProbablePaths, double upperBound
		listOfAntsProbablePaths.add(new AntsProbablePath("ferrisWheel", 0.3218082377196204));
		double lowerBound= 0,upperBound=0;
		List<Range<Double>> ranges = new ArrayList<>();
		for(AntsProbablePath ppath: listOfAntsProbablePaths){

			Range<Double> probabilityRange = Range.between(lowerBound, ppath.getProbability());
			lowerBound =ppath.getProbability();
			ranges.add(probabilityRange);
			ppath.setRangeOfProbabilities(probabilityRange);
			upperBound = ppath.getProbability();
		}//List<AntsProbablePath> antsProbablePaths, double upperBound
		String dec = this.problemSpace.getNodeName(listOfAntsProbablePaths,upperBound);
		System.out.println(dec);

	}
}