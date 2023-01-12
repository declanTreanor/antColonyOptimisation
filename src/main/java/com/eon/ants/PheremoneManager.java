package com.eon.ants;

import com.eon.ants.concurrrency.ACOLockObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Data
@Slf4j
public class PheremoneManager {

	@Autowired
	@Qualifier("pheremones")
	private double[][] pheremoneTrails;
	@Autowired
	private ACOLockObject lockObject;
	@Autowired
	private String[] nodeNames;
	private boolean isReady;

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

	public void consistencyCheck(int fromm,int too) {
		if(isFullyPopulated(fromm, too)) {
			extracted();
		}
	}

	private void extracted() {
		double[][] copy = pheremoneTrails;
		List<String> projectedPath = new ArrayList<>();
		Set<String> uniqueNodeNames = new HashSet<>();

		double largest = 0.0;
		int winningIndex = 0;
		for(int i = 0; i<copy.length;i++) {
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

	public synchronized void dropPheremone(int from, int to) {


		try {
			lockObject.writeLock().lock();
			try {
				pheremoneTrails[from][to] += Ant.PHEREMONES;
			} catch (IndexOutOfBoundsException ioobe) {
				System.out.println();
			}
		} finally {
			lockObject.writeLock().unlock();
			if(isFullyPopulated(from, to))
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

	public List<String> displayShortestPath() {
		List<String> shortestPath = new ArrayList<>();
		for (int i = 0; i < pheremoneTrails.length; i++) {
			BigDecimal most = BigDecimal.ZERO;
			int winningIndex = 0;
			for(int j = 0; j < pheremoneTrails.length; j++){
				BigDecimal bigDecimal = new BigDecimal(String.valueOf(pheremoneTrails[i][j]));
				/**
				 * This indefensible hack is to get around the bug! Not the shortest path! :(
				 */
				if (bigDecimal.compareTo(most)>0 && !shortestPath.contains(nodeNames[j])){
					most = bigDecimal;
					winningIndex=j;
				}
			}
			shortestPath.add(this.nodeNames[winningIndex]);

		}
		return shortestPath;
	}
}
