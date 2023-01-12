package com.eon.ants.config;

import com.eon.ants.Ant;
import com.eon.ants.PheremoneManager;
import com.eon.ants.ProblemSpace;
import com.eon.ants.concurrrency.ACOLockObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ACOConfig {

	@Bean("adjacency")
	public double[][] adjacencymatrix(){
		double[][] distanceMatrix={ { 0, 8, 7, 4, 6, 4 },
				{ 8, 0, 5, 7, 11, 5 },
				{ 7, 5, 0, 9, 6, 7 },
				{ 4, 7, 9, 0, 5, 6 },
				{ 6, 11, 6, 5, 0, 3 },
				{ 4, 5, 7, 6, 3, 0 }
		};
		return distanceMatrix;

	}

	@Bean(name = {"PheremoneManager"})
	public PheremoneManager getPheremoneMgr() {
		return new PheremoneManager(initialPheremones());
	}
//	@Bean(name = {"PheremoneManagerTst"})
//	public PheremoneManager getPheremoneMgrTst() {
//		return new PheremoneManager(initialPheremonesBook());
//	}

	@Bean(name = {"ACOLockObject"})
	public ACOLockObject getACOLockObject() {
		return new ACOLockObject();
	}


	@Bean(name = {"ant", "Ant"})
	@Scope(value = "prototype")
	public Ant getAnt() {
		return new Ant(problemSpaceReal());
	}

	@Bean(name = "problemSpace")
	public ProblemSpace problemSpaceReal(){
		return new ProblemSpace(initialPheremones());
	}

	@Bean(name = "problemSpaceForTest")
	public ProblemSpace problemSpaceTst(){
		return new ProblemSpace(initialPheremonesBook());
	}


	@Bean("pheremones")//"circus", "balloon", "bumpers", "carousel", "swings", "ferrisWheel"
	public double[][] initialPheremones(){
		double[][] distanceMatrix={ { 0, 1, 1, 1, 1, 1 },
				{ 1, 0, 1, 1, 1, 1 },
				{ 1, 1, 0, 1, 1, 1 },
				{ 1, 1, 1, 0, 1, 1 },
				{ 1, 1, 1, 1, 0, 1 },
				{ 1, 1, 1, 1, 1, 0 }
		};
		return distanceMatrix;
	}

	@Bean("initialPheremonesBook")
	public double[][] initialPheremonesBook(){
		double[][] distanceMatrix={ { 0, 5, 7, 10, 8, 11 },
				{ 1, 0, 1, 1, 1, 1 },
				{ 1, 1, 0, 1, 1, 1 },
				{ 1, 1, 1, 0, 1, 1 },
				{ 1, 1, 1, 1, 0, 1 },
				{ 1, 1, 1, 1, 1, 0 }
		};
		return distanceMatrix;
	}

	@Bean
	public String[] nodeNames(){
		return new String[]{"circus", "balloon", "bumpers", "carousel", "swings", "ferrisWheel"};
	}

}
