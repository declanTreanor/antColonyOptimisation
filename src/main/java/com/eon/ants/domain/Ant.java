package com.eon.ants.domain;

import com.eon.ants.service.PheremoneManager;
import com.eon.ants.service.ProblemSpace;
import com.eon.ants.config.ACOConfig;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

@Data
@Component("Ant")
public class Ant implements Runnable {

	public static final double PHEREMONES = 1.0;
	@Autowired
	private PheremoneManager pheremoneManager;
	@Autowired
	private ProblemSpace problemSpace;
	@Autowired
	@Qualifier(ACOConfig.MEDIUM)
	private String[] nodeNames;
	private final Random random = new Random();
	private String startingNode;
	private String currentNode;
	private double pathDistance = 0.0D;
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



	@Override
	public void run() {
		startOnRandomNode();
		while(pathIncomplete()){
			moveToNextNode();
		}

	}

	private boolean pathIncomplete() {
		return this.pathTaken.size()<nodeNames.length;
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
				'}';
	}

	public void startOn(String startingNode) {
		this.pathDistance=0.0;
		this.startingNode = startingNode;
		this.currentNode = startingNode;
		pathTaken.add(startingNode);
	}

	public void startOnRandomNode() {
		this.pathDistance=0.0;
		this.pathTaken = new ArrayList<>();
		int indexOfStartNode = random.nextInt(nodeNames.length - 1);

		this.startingNode = nodeNames[indexOfStartNode];
		if(this.startingNode.equals(""))
			throw new IllegalStateException("starting node is blank!!");
		this.currentNode = this.startingNode;
		this.pathTaken.add(this.startingNode);

	}

	public void moveToNextNode() {


		String nextName = problemSpace.chooseNextNode(this);
		this.pathTaken.add(nextName);
		int indexCurrent = Arrays.asList(nodeNames).indexOf(currentNode);
		int indexNext= Arrays.asList(nodeNames).indexOf(nextName);

		pheremoneManager.consistencyCheck(Arrays.asList(nodeNames).indexOf(currentNode),Arrays.asList(nodeNames).indexOf(nextName));
		double[][] peek=problemSpace.getAdjacencyMatrix();
		this.pathDistance+=peek[indexCurrent][indexNext];

		pheremoneManager.dropPheremone(this,indexCurrent,indexNext);
		this.currentNode = nextName;

	}

	public String[] getAttractionsLeft() {
		List<String> list = new ArrayList<>(Arrays.asList(nodeNames));
		list.removeAll(this.pathTaken);
		return list.toArray(new String[0]);
	}
}
