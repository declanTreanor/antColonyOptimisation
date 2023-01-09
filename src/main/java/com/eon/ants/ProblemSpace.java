package com.eon.ants;

import com.eon.ants.concurrrency.ACOLockObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@Data
public class ProblemSpace {
	@Autowired
	@Qualifier("adjacency")
	private double[][] adjacencyMatrix;
	@Autowired
	@Qualifier("pheremones")
	private double[][] pheremones;
	@Autowired
	private String[] nodeNames;
	@Autowired
	private PheremoneManager pheremoneManager;

	@Autowired
	private ACOLockObject lockObject;
	protected static final short ALPHA = 1;
	protected static final short BETA = 2;

	/**
	 * Given two distinct, full paths, this returns
	 * the shortest path, in terms of distance.
	 * @param pathA
	 * @param pathB
	 * @return if <tt>findLength(pathA) >= findLength(pathB)</tt>, returns pathB,
	 * returns pathA in all other cases.
	 */
	public List<String> findShortestPath(List<String> pathA, List<String> pathB){
		if(findLength(pathA) >= findLength(pathB))
			return pathB;
		else
			return pathA;
	}

	/**
	 *
	 * @param start
	 * @param destination
	 * @return
	 */
	protected double probabilityChoosingPath(Ant ant, String start, String destination){
		double denominator=0;
		for(String possibleDestinationN:possibleDestinations(start,ant)){
			denominator+= getNumerator(start,possibleDestinationN);

		}
		return getNumerator(start, destination)/denominator;
	}

	private double getNumerator(String start, String destination) {
		double pheremoneLevelFromTo = pheremoneManager.getPheremoneLevel(start, destination);
		double topA = Math.pow(pheremoneLevelFromTo, ALPHA);
		double topB = Math.pow(1/getDistance(start, destination),BETA);
		return topA * topB;
	}

	private String[] possibleDestinations(String startingNode, Ant ant){
		if(!ant.getPathTaken().contains(startingNode))
			throw new IllegalStateException("Ant's path, "+ant.getPathTaken()+", must contain starting node, "+startingNode);
		String[] possibleDestinations = new String[nodeNames.length-1];
		int counter = 0;
		List<String>availableLocations =  new ArrayList<>(Arrays.stream(nodeNames).toList());
		availableLocations.removeAll(ant.getPathTaken());
		availableLocations.remove(startingNode);
		for(String node : availableLocations) {
			if (!node.equals(startingNode))
				possibleDestinations[counter++] = node;
		}
		return availableLocations.toArray(new String[0]);
	}

	/**
	 * finds the total length of the path given, by compounding
	 * the distances between each node.
	 * @param pathA
	 * @return
	 */
	protected double findLength(List<String> pathA) {
		String startingNode = pathA.get(0);
		double totalDistance = 0.0;
		for(String attraction: pathA){
			totalDistance += getDistance(startingNode,attraction);
			startingNode=attraction;
		}
		return totalDistance;
	}

	/**
	 * simply reads the static distance from the adjacency matrix.
	 * @param from a <tt>String</tt> representing the named node, from which the ant is travelling.
	 * @param to a <tt>String</tt> representing the named node, to which the ant is travelling.
	 * @return
	 */
	protected double getDistance(String from, String to){
		if(from.equals(to))
			return 0.0;
		int indexFrom, indexTo;
		double distance;
		try{
			lockObject.readLock().lock();
			indexFrom = Arrays.asList(nodeNames).indexOf(from);
			indexTo = Arrays.asList(nodeNames).indexOf(to);
			log.info("x,y, nodenames {},{}, {}",indexFrom,indexTo,nodeNames);
			distance = adjacencyMatrix[indexFrom][indexTo];
		}finally {
			lockObject.readLock().unlock();
		}
		return distance;
	}

	/**
	 *
	 */
	private double totalPheremones;

}
