package com.eon.ants;

import com.eon.ants.concurrrency.ACOLockObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Slf4j
@Component
public class PheremoneManager {

	@Autowired
	@Qualifier("pheremones")
	private double[][] pheremoneTrails;
	@Autowired
	private ACOLockObject lockObject;
	@Autowired
	private String[] nodeNames;

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

	public synchronized void dropPheremone(String from, String to) {
		log.info("from {} to {}", from, to);
		List nodeNamesLst = Arrays.asList(nodeNames);
		if (!nodeNamesLst.contains(from))
			System.out.println();

		dropPheremone(nodeNamesLst.indexOf(from), nodeNamesLst.indexOf(to));
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
				if (bigDecimal.compareTo(most)>0 && !shortestPath.contains(nodeNames[j])){
					most = bigDecimal;
					winningIndex=j;
				}
			}

			shortestPath.add(this.nodeNames[winningIndex]);

		}
		return shortestPath;
	}

//		for(int i=0; i<this.nodeNames.length; i++) {
//			double[] row = this.pheremoneTrails[i];
//			BigDecimal most = BigDecimal.ZERO;
//			int indexOfLargest =0;
//			for(int j=0; j<this.nodeNames.length; j++){
//				BigDecimal attractionPheremones = BigDecimal.valueOf(row[j]);
//				if(attractionPheremones.compareTo(most) >0) {
//					most = BigDecimal.valueOf(row[j]);
//					indexOfLargest =j;
//				}
//
//			}
//			String nodename=this.nodeNames[indexOfLargest];
//			if(shortestPath.contains(nodename))
//				throw new IllegalStateException(nodename+" is already in "+shortestPath);
//			shortestPath.add(nodename);
//
//			System.out.println(nodename);
//
//		}
//		return shortestPath;

	//	try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
	//		for(int i=0; i<100; i++) {
	//			scope.fork(() -> getHello());
	//		}
	//
	//		scope.join(); //blocking
	//
	//		return scope.result();
	//	}
}
