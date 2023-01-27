package com.eon.ants.service;

import com.eon.ants.config.ACOConfig;
import com.eon.ants.domain.Ant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Data
@Component
public class ProblemSpace {

	@Autowired
	@Qualifier("adjacency")
	private double[][] adjacencyMatrix;
	@Autowired
	@Qualifier("pheremones")
	private double[][] pheremones;
	@Autowired
	@Qualifier("med")
	private String[] nodeNames;
	@Autowired
	private PheremoneManager pheremoneManager;
	@Autowired
	private ApplicationContext ctx;


	protected static final short ALPHA = 1;
	protected static final short BETA = 2;

	public ProblemSpace(double[][] pheremones) {
		this.pheremones = pheremones;
	}

	/**
	 * Given two distinct, full paths, this returns the shortest path, in terms of distance.
	 * @param pathA
	 * @param pathB
	 * @return if <tt>findLength(pathA) >= findLength(pathB)</tt>, returns pathB, returns pathA in all other cases.
	 */
	public List<String> findShortestPath(List<String> pathA, List<String> pathB) {
		if (findLength(pathA) >= findLength(pathB))
			return pathB;
		else
			return pathA;
	}

	/**
	 * @param destination
	 * @return
	 */
	public double probabilityChoosingPath(Ant ant, String destination) {
		double denominator = 0;
		for (String possibleDestinationN : possibleDestinations(ant)) {
			if (!Arrays.asList(nodeNames).contains(ant.getCurrentNode()))
				System.out.println();
			denominator += getNumerator(ant.getCurrentNode(), possibleDestinationN);

		}

		return getNumerator(ant.getCurrentNode(), destination) / denominator;
	}

	private double getNumerator(String start, String destination) {
		if (!Arrays.asList(nodeNames).contains(start))
			System.out.println();
		double pheremoneLevelFromTo = pheremoneManager.getPheremoneLevel(start, destination);
		double topA = Math.pow(pheremoneLevelFromTo, ALPHA);
		double topB = Math.pow(1 / getDistance(start, destination), BETA);
		return topA * topB;
	}

	public List<String> determineShortestPath(){
		int amountOfAnts = getNumberOfAnts(ctx.getBean(ACOConfig.class).nodeNames().length);
		for (int i = 0; i < amountOfAnts; i++) {
			CompletableFuture<Void> cf = CompletableFuture.runAsync(new Ant(this));

			if (i % 5 == 0) {
				if (pheremoneManager == null)
					pheremoneManager = (PheremoneManager) ctx.getBean("PheremoneManager");
				else {
					pheremoneManager.evaporate(0.9D);
				}

			}


			doRunAround(pheremoneManager, i);

		}

		System.out.println(pheremoneManager.getAllChanges().toString());
		System.out.println("\nthe following comprises the best attempts found, though not looked-for (so, therefore not definitive), starting at various attractions:\n");
		System.out.println(pheremoneManager.getAll6RoutesFromDifferentStarts());
		System.out.println("\n but (one of) the absolute shortest path(s) is: "+pheremoneManager.getShortestPath());

		return pheremoneManager.getShortestPath().path();
	}
	private int getNumberOfAnts(int amountOfNodes) {
		int amount = 1;
		for(int i=1; i<amountOfNodes; i++)
			amount *= i;

		return amount;
	}
	private void doRunAround(PheremoneManager pheremoneManager, int i) {
		Ant ant = (Ant) ctx.getBean("Ant");
		ant.startOnRandomNode();

		while (ant.getPathTaken().size() < ant.getNodeNames().length) {

			ant.moveToNextNode();

		}
		pheremoneManager.saveRoute(ant, i);
	}


	private String[] possibleDestinations(Ant ant) {
		return ant.getAttractionsLeft();
	}

	List<AntsProbablePath> antsProbablePaths = new ArrayList<>();

	public String chooseNextNode(Ant ant) {

		List<AntsProbablePath> antsProbablePaths = new ArrayList<>();
		List<String> attractionsLeft = new ArrayList<>(Arrays.asList(ant.getAttractionsLeft()));
		for (String attraction : attractionsLeft)
			antsProbablePaths.add(new AntsProbablePath(attraction, probabilityChoosingPath(ant, attraction)));

		antsProbablePaths.sort(Comparator.comparing(a -> a.getProbability()));

		List<Range<Double>> ranges = new ArrayList<>();
		double lowerBound = 0, overallLargestProbability = 0;
		for (AntsProbablePath ppath : antsProbablePaths) {

			Range<Double> probabilityRange = Range.between(lowerBound, ppath.getProbability());
			lowerBound = ppath.getProbability();
			ranges.add(probabilityRange);
			ppath.setRangeOfProbabilities(probabilityRange);
			overallLargestProbability = ppath.getProbability();
		}
		String nodeName = getNodeName(antsProbablePaths, overallLargestProbability);
		if (ant.getPathTaken().contains(nodeName)) {
			while (ant.getPathTaken().contains(nodeName)) {
				nodeName = nodeNames[new Random().nextInt(nodeNames.length - 1)];
			}

		}
		return nodeName;
	}

	public String getNodeName(List<AntsProbablePath> antsProbablePaths, double upperBound) {
		double randomDouble = 0;
		try {
			randomDouble = new Random().nextDouble(upperBound);
		} catch (IllegalArgumentException iae) {
			log.error("upperBound is not valid [{}]", upperBound);
			return this.nodeNames[new Random().nextInt(this.nodeNames.length)];

		}
		/**
		 * here lies the problem:
		 */
		String returnVal = "";
		outerFor:
		for (Range<Double> rangeOfDoubles : antsProbablePaths.stream().map(pp -> pp.getRangeOfProbabilities()).collect(Collectors.toList())) {
			if (rangeOfDoubles.contains(randomDouble)) {
				AntsProbablePath attraction = antsProbablePaths.stream().filter(att -> att.getRangeOfProbabilities().equals(rangeOfDoubles))
						.findFirst().get();
				returnVal = attraction.getNodeName();
				break outerFor;
			}
		}
		if (returnVal.equals(""))
			returnVal = nodeNames[new Random().nextInt(nodeNames.length - 1)];
		return returnVal;
	}

	/**
	 * finds the total length of the path given, by compounding the distances between each node.
	 * @param pathA
	 * @return
	 */
	public double findLength(List<String> pathA) {
		String startingNode = pathA.get(0);
		double totalDistance = 0.0;
		for (String attraction : pathA) {
			totalDistance += getDistance(startingNode, attraction);
			startingNode = attraction;
		}
		return totalDistance;
	}

	/**
	 * simply reads the static distance from the adjacency matrix.
	 * @param from a <tt>String</tt> representing the named node, from which the ant is travelling.
	 * @param to   a <tt>String</tt> representing the named node, to which the ant is travelling.
	 * @return
	 */
	protected double getDistance(String from, String to) {
		if (from.equals(to))
			return 0.0;
		int indexFrom, indexTo;
		double distance;
		indexFrom = Arrays.asList(nodeNames).indexOf(from);
		indexTo = Arrays.asList(nodeNames).indexOf(to);
		log.info("x,y, nodenames {},{}, {}", indexFrom, indexTo, nodeNames);
		distance = adjacencyMatrix[indexFrom][indexTo];

		return distance;
	}

}
