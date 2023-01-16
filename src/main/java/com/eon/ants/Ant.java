package com.eon.ants;

import com.eon.ants.concurrrency.ACOLockObject;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Data
public class Ant implements Runnable {

	public static final double PHEREMONES = 2.3;
	@Autowired
	private PheremoneManager pheremoneManager;
	@Autowired
	private ProblemSpace problemSpace;
	@Autowired
	private String[] nodeNames;
	private final Random random = new Random();
	private String startingNode;
	private String currentNode;
	private double pathDistance = Double.MIN_NORMAL;
	private List<String> pathTaken = new ArrayList<>();

	public Ant(ProblemSpace attractionMap) {
		this.problemSpace = attractionMap;
	}

	public List<String> getPathTaken() {
		return pathTaken;
	}

	public double getPathDistance() {
		return pathDistance;
	}

	@Autowired
	private ACOLockObject lockObject;

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

//	public void moveToNext() {
//
//		String nextNode = problemSpace.chooseNextNode(this);
//		if (!Arrays.asList(this.pathTaken).contains(nextNode))
//			System.out.println();
//		this.pathTaken.add(nextNode);
//		try {
//			this.pheremoneManager.dropPheremone(this.getCurrentNode(), nextNode);
//		} catch (IndexOutOfBoundsException ioobe) {
//			this.startOnRandomNode();
//		}
//		this.setCurrentNode(nextNode);
//
//	}

	@Override
	public String toString() {
		return "Ant{" +
				"nodeNames=" + Arrays.toString(nodeNames) +
				", random=" + random +
				", problemSpace=" + problemSpace +
				", startingNode='" + startingNode + '\'' +
				", currentNode='" + currentNode + '\'' +
				", pathDistance=" + pathDistance +
				", pathTaken=" + pathTaken +
				", pheremoneManager=" + pheremoneManager +
				", lockObject=" + lockObject +
				'}';
	}

	protected void startOn(String startingNode) {
		this.pathDistance=0.0;
		this.startingNode = startingNode;
		this.currentNode = startingNode;
		pathTaken.add(startingNode);
	}

	protected void startOnRandomNode() {
		this.pathDistance=0.0;
		this.pathTaken = new ArrayList<>();
		int indexOfStartNode = random.nextInt(nodeNames.length - 1);

		this.startingNode = nodeNames[indexOfStartNode];
		if(this.startingNode.equals(""))
			throw new IllegalStateException("starting node is blank!!");
		this.currentNode = this.startingNode;
		this.pathTaken.add(this.startingNode);

	}

	protected void moveToNextNode() {


		String nextName = problemSpace.chooseNextNode(this);
		this.pathTaken.add(nextName);
		int indexCurrent = Arrays.asList(nodeNames).indexOf(currentNode);
		int indexNext= Arrays.asList(nodeNames).indexOf(nextName);

		pheremoneManager.consistencyCheck(Arrays.asList(nodeNames).indexOf(currentNode),Arrays.asList(nodeNames).indexOf(nextName));
		this.pathDistance+=problemSpace.getAdjacencyMatrix()[indexCurrent][indexNext];

		pheremoneManager.dropPheremone(this,indexCurrent,indexNext);
		this.currentNode = nextName;

	}

	protected String[] getAttractionsLeft() {
		List<String> list = new ArrayList<>(Arrays.asList(nodeNames));
		list.removeAll(this.pathTaken);
		return list.toArray(new String[0]);
	}
}
