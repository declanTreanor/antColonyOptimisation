package com.eon.ants;

import com.eon.ants.concurrrency.ACOLockObjectPheremones;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.*;

@Data
@Slf4j
public class PheremoneManager {

	private StringBuilder allChanges = new StringBuilder("an audit trail of the paths, looks like this: \n");
	private Map<String, List<String>> all6RoutesFromDifferentStarts = new HashMap<>();
	@Autowired
	@Qualifier("pheremones")
	private double[][] pheremoneTrails;
	@Autowired
	private ACOLockObjectPheremones pheremoneLockObject;
	@Autowired
	@Qualifier("med")
	private String[] nodeNames;
	private boolean isReady;

	private record ShortestPath(double distance, List<String> path) {

	}

	private ShortestPath shortestPath;

	private record RouteDeets(double distance, List<String> path) {}

	;
	private List<RouteDeets> allRoutes;

	public PheremoneManager(double[][] pheremoneTrails) {
		this.pheremoneTrails = pheremoneTrails;
	}

	protected double getPheremoneLevel(String from, String to) {
		double pheremoneLevel;
		try {
			pheremoneLockObject.readLock().lock();

			int indexFrom = Arrays.asList(nodeNames).indexOf(from);
			int indexTo = Arrays.asList(nodeNames).indexOf(to);
			log.info("travelling from {} to {}", from, to);
			pheremoneLevel = pheremoneTrails[indexFrom][indexTo];
		} finally {
			pheremoneLockObject.readLock().unlock();
		}

		return pheremoneLevel;
	}

	public void consistencyCheck(int fromm, int too) {
		if (isFullyPopulated(fromm, too)) {
			checkForDuplicates();
		}
	}

	private void checkForDuplicates() {
		double[][] copy = pheremoneTrails;
		List<String> projectedPath = new ArrayList<>();

		double largest = 0.0;
		int winningIndex = 0;
		for (int i = 0; i < copy.length; i++) {
			for (int j = 0; j < copy.length; j++) {
				if (copy[i][j] > largest) {
					largest = j;
					winningIndex = j;
				}

			}
			String nodeName = nodeNames[winningIndex];
			projectedPath.add(nodeName);
		}

	}

	private boolean isFullyPopulated(int fromm, int too) {
		return fromm == pheremoneTrails.length - 1 && too == pheremoneTrails.length - 2;
	}

	public synchronized void dropPheremone(Ant ant, int from, int to) {

		try {
			pheremoneLockObject.writeLock().lock();
			try {
				pheremoneTrails[from][to] += Ant.PHEREMONES;
			} catch (IndexOutOfBoundsException ioobe) {
				System.out.println();
			}
		} finally {
			pheremoneLockObject.writeLock().unlock();
			if (isFullyPopulated(from, to))
				consistencyCheck(from, to);
		}
	}

	public void evaporate(double evaporationConstant) {
		int square = pheremoneTrails.length;
		for (int i = 0; i < square; i++) {
			for (int j = 0; j < square; j++) {
				pheremoneTrails[i][j] = pheremoneTrails[i][j] * evaporationConstant;
			}
		}

	}

	public void saveRoute(Ant ant, int i) {
		if (allRoutes == null)
			allRoutes = new ArrayList<>();
		RouteDeets rd = new RouteDeets(ant.getPathDistance(), ant.getPathTaken());
		allRoutes.add(rd);

		if (shortestPath == null || ant.getPathDistance() < shortestPath.distance) {
			log.info("shortest path {} at iteration {}", shortestPath, i + 1);
			shortestPath = new ShortestPath(ant.getPathDistance(), ant.getPathTaken());
			all6RoutesFromDifferentStarts.put(ant.getStartingNode(), ant.getPathTaken());
			allChanges.append("\npath taken: " + ant.getPathTaken() + " has distance " + ant.getPathDistance() + " at iteration " + (i + 1));
		}
	}

}
