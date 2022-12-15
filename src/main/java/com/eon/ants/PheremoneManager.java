package com.eon.ants;

import com.eon.ants.concurrrency.LockObject;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.eon.ants.ProblemSpace.nodeNames;

@Data
@Component
public class PheremoneManager {
	private double [][] pheremoneTrails;
	@Autowired
	private LockObject lockObject;


	protected double getPheremoneLevel(String from, String to){
		int indexFrom = Arrays.asList(nodeNames).indexOf(from);
		int indexTo = Arrays.asList(nodeNames).indexOf(to);

		return pheremoneTrails[indexFrom][indexTo];
	}

	public synchronized void dropPheremone(int from, int to) {
		lockObject.lock();
		pheremoneTrails[from][to] += Ant.PHEREMONES;
		lockObject.unlock();

	}
}
