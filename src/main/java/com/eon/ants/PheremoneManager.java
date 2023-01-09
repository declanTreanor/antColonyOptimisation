package com.eon.ants;

import com.eon.ants.concurrrency.ACOLockObject;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Data
@Component
public class PheremoneManager {
	@Autowired
	@Qualifier("pheremones")
	private double [][] pheremoneTrails;
	@Autowired
	private ACOLockObject lockObject;
	@Autowired
	private String[] nodeNames;


	protected double getPheremoneLevel(String from, String to){
		double pheremoneLevel;
		try {
			lockObject.readLock().lock();
			int indexFrom = Arrays.asList(nodeNames).indexOf(from);
			int indexTo = Arrays.asList(nodeNames).indexOf(to);
			pheremoneLevel = pheremoneTrails[indexFrom][indexTo];
		}finally {
			lockObject.readLock().unlock();
		}

		return pheremoneLevel;
	}

	public synchronized void dropPheremone(int from, int to) {
		try {
			lockObject.writeLock().lock();
			pheremoneTrails[from][to] += Ant.PHEREMONES;
		}finally {
			lockObject.writeLock().unlock();
		}


	}
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
