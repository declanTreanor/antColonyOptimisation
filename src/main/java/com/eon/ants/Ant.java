package com.eon.ants;

import com.eon.ants.concurrrency.ACOLockObject;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;


@Data
public class Ant implements Runnable{

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
	public void moveToNext(){


		String nextNode = problemSpace.chooseNextNode(this);
		if(!Arrays.asList(this.pathTaken).contains(nextNode))
			System.out.println();
		this.pathTaken.add(nextNode);
		try {
			this.pheremoneManager.dropPheremone(this.getCurrentNode(), nextNode);
		}catch (IndexOutOfBoundsException ioobe){
			this.startOnRandomNode();
		}
		this.setCurrentNode(nextNode);

	}

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

	protected void startOn(String startingNode){
		this.startingNode = startingNode;
		this.currentNode = startingNode;
		pathTaken.add(startingNode);
	}
	protected void startOnRandomNode(){
 		this.pathTaken=new ArrayList<>();
		int indexOfStartNode = random.nextInt(nodeNames.length-1);
		this.startingNode = nodeNames[indexOfStartNode];
		this.currentNode = this.startingNode;
		this.pathTaken.add(this.startingNode);

	}
	protected void moveToNextNode(){


		String[] attractionsLeft = getAttractionsLeft();

		int indexOfNextAttraction=random.nextInt(attractionsLeft.length-1);
		String nextName = attractionsLeft[indexOfNextAttraction];
		int realIndexOfNext = Arrays.asList(nodeNames).indexOf(nextName);

		this.pathTaken.add(nextName);
		pheremoneManager.dropPheremone(Arrays.asList(nodeNames).indexOf(currentNode), Arrays.asList(nodeNames).indexOf(nextName));
		this.currentNode = nextName;
		;

	}

	protected String[] getAttractionsLeft(){
		List<String> list = new ArrayList<>(Arrays.asList(nodeNames));
		list.removeAll(this.pathTaken);
		return list.toArray(new String[0]);
	}
}
