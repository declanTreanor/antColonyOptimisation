package com.eon.ants;

//import jdk.incubator.concurrent.StructuredTaskScope;

import com.eon.ants.config.ACOConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class AntsApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(AntsApplication.class, args);
		//		new AntsApplication().findShortestPath();
		ApplicationContext ctx = new AnnotationConfigApplicationContext(ACOConfig.class);
		PheremoneManager pheremoneManager = null;
		Ant ant;
		int amountOfAnts = getNumberOfAnts(ctx.getBean(ACOConfig.class).nodeNames().length);
		for (int i = 0; i < amountOfAnts; i++) {

			if (i % 5 == 0) {
				if (pheremoneManager == null)
					pheremoneManager = (PheremoneManager) ctx.getBean("PheremoneManager");
				else {
					pheremoneManager.evaporate(0.9D);
				}

			}
			ant = (Ant) ctx.getBean("Ant");
			ant.startOnRandomNode();

			while (ant.getPathTaken().size() < ant.getNodeNames().length) {

				ant.moveToNextNode();

			}
			pheremoneManager.saveRoute(ant,i);

		}


		System.out.println(pheremoneManager.getAllChanges().toString());
		System.out.println("\nthe following comprises the best attempts found, though not looked-for (so, therefore not definitive), starting at various attractions:\n");
		System.out.println(pheremoneManager.getAll6RoutesFromDifferentStarts());
		System.out.println("\n but (one of) the absolute shortest path(s) is: "+pheremoneManager.getShortestPath());

	}

	private static int getNumberOfAnts(int amountOfNodes) {
		int amount = 1;
		for(int i=1; i<amountOfNodes; i++)
			amount *= i;

		return amount;
	}

}
