package com.eon.ants;

import com.eon.ants.concurrrency.LockObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.eon.ants.ProblemSpace.nodeNames;

@Component
public class Ant implements Runnable{

	public static final double PHEREMONES = 2.3;
	private final Random random = new Random();
	private ProblemSpace problemSpace;
	private String startingNode;
	private String currentNode;
	private double pathDistance = Double.MIN_NORMAL;
	private List<String> pathTaken = new ArrayList<>();

	public Ant(ProblemSpace route) {
		this.problemSpace = route;
	}

	@Autowired
	private PheremoneManager mgr;
	@Autowired
	private LockObject lockObject;
	@Override
	public void run() {
		startOnRandomNode();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		moveToNextNode();
	}
	protected void startOnRandomNode(){
		int indexOfStartNode = random.nextInt(nodeNames.length-1);
		this.startingNode = nodeNames[indexOfStartNode];
		pathTaken.add(this.startingNode);

	}
	protected void moveToNextNode(){


		String[] attractionsLeft = getAttractionsLeft();
		int indexOfNextAttraction=random.nextInt(attractionsLeft.length-1);
		String nextName = attractionsLeft[indexOfNextAttraction];
		int realIndexOfNext = Arrays.asList(nodeNames).indexOf(nextName);

		this.pathTaken.add(nextName);
		mgr.dropPheremone(Arrays.asList(nodeNames).indexOf(currentNode), Arrays.asList(nodeNames).indexOf(nextName));
		this.currentNode = nextName;
		;

	}

	protected String[] getAttractionsLeft(){
		List<String> list = Arrays.asList(nodeNames);
		list.removeAll(this.pathTaken);
		return list.toArray(new String[0]);
	}
}
