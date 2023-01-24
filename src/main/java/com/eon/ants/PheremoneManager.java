package com.eon.ants;

import com.eon.ants.concurrrency.ACOLockObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Slf4j
public class PheremoneManager {

	private StringBuilder allChanges = new StringBuilder("an audit trail of the paths, looks like this: \n");
	@Autowired
	@Qualifier("pheremones")
	private double[][] pheremoneTrails;
	@Autowired
	private ACOLockObject lockObject;
	@Autowired
	@Qualifier("med")
	private String[] nodeNames;
	private boolean isReady;

	private record ShortestPath(double distance, List<String> path) {

	}

	private ShortestPath shortestPath;

	@AllArgsConstructor
	@Data
	public class RouteDeets implements Comparable {

		private double distance;
		private List<String> path;

		@Override
		public int compareTo(Object o) {
			return BigDecimal.valueOf(((RouteDeets) o).distance).
					compareTo(BigDecimal.valueOf(this.distance));
		}
	}

	;
	private List<RouteDeets> allRoutes;

	public PheremoneManager(double[][] pheremoneTrails) {
		this.pheremoneTrails = pheremoneTrails;
	}

	protected double getPheremoneLevel(String from, String to) {
		double pheremoneLevel;
		try {
			lockObject.readLock().lock();

			int indexFrom = Arrays.asList(nodeNames).indexOf(from);
			int indexTo = Arrays.asList(nodeNames).indexOf(to);
			log.info("travelling from {} to {}", from, to);
			pheremoneLevel = pheremoneTrails[indexFrom][indexTo];
		} finally {
			lockObject.readLock().unlock();
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
//		Set<String> uniqueNodeNames = new HashSet<>();

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
			//			if(!uniqueNodeNames.add(nodeName))
			//				throw new IllegalArgumentException(nodeName+" not unique, in "+projectedPath);
		}

	}

	private boolean isFullyPopulated(int fromm, int too) {
		return fromm == pheremoneTrails.length - 1 && too == pheremoneTrails.length - 2;
	}

	public synchronized void dropPheremone(Ant ant, int from, int to) {

		try {
			lockObject.writeLock().lock();
			try {
				pheremoneTrails[from][to] += Ant.PHEREMONES;
			} catch (IndexOutOfBoundsException ioobe) {
				System.out.println();
			}
		} finally {
			lockObject.writeLock().unlock();
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
			shortestPath = new ShortestPath(ant.getPathDistance(), ant.getPathTaken());
			allChanges.append("\npath taken: "+ant.getPathTaken()+" has distance "+ant.getPathDistance()+" at iteration "+(i+1));
		}
		System.out.println("rote66 " + allRoutes);
	}

	private void increaseNthNRouteBy(int n, double addition) {
		List<String> nthPath = getSortedRoutes().get(n).getPath();
		addEach(nthPath, addition);

	}
	private void multNtRhouteBy(int n, double addition) {
		List<String> nthPath = getSortedRoutes().get(n).getPath();
		multiplyEach(nthPath, addition);

	}

	private void multiplyEach(List<String> nthPath, double multiplier) {
		String from = "";
		for (String node : nthPath) {
			if (from.equals(""))
				from = node;
			else {
				int indexFrom = Arrays.asList(nodeNames).indexOf(from);
				int indexTo = Arrays.asList(nodeNames).indexOf(node);
				from = node;
				pheremoneTrails[indexFrom][indexTo] = pheremoneTrails[indexFrom][indexTo] * multiplier;
			}
		}
	}

	public List<PheremoneManager.RouteDeets> getSortedRoutes() {
		List<PheremoneManager.RouteDeets> sorted = allRoutes.stream().sorted().collect(Collectors.toList());
		return sorted;
	}

	public void shitTax() {

		multNtRhouteBy(0, 0.5);

	}

	private void addEach(List<String> path, double multiplier) {
			String from = "";
		for (String node : path) {
			if (from.equals(""))
				from = node;
			else {
				try {
					int indexFrom = Arrays.asList(nodeNames).indexOf(from);
					int indexTo = Arrays.asList(nodeNames).indexOf(node);
					from = node;
					pheremoneTrails[indexFrom][indexTo] = pheremoneTrails[indexFrom][indexTo] + multiplier;
				} catch (ArrayIndexOutOfBoundsException aioob) {
					System.out.println();
				}
			}
		}
	}

	public void rewardBest() {

		increaseNthNRouteBy(0, 5.0);

	}
}
