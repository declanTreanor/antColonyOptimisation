package com.eon.ants;

//import jdk.incubator.concurrent.StructuredTaskScope;

import com.eon.ants.config.ACOConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class AntsApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(AntsApplication.class, args);
		//		new AntsApplication().findShortestPath();
		ApplicationContext ctx = new AnnotationConfigApplicationContext(ACOConfig.class);
		PheremoneManager pheremoneManager = null;
		Ant ant = null;
		for (int i = 0; i < 100; i++) {
			if (i % 5 == 0) {
				if (pheremoneManager == null)
					pheremoneManager = (PheremoneManager) ctx.getBean("PheremoneManager");
				else
					pheremoneManager.evaporate(0.9D);

			}
			if (pheremoneManager != null && i % 7 == 0 && pheremoneManager.getAllRoutes() != null) {
				pheremoneManager.rewardBest();
				pheremoneManager.shitTax();
			}

			ant = (Ant) ctx.getBean("Ant");
			ant.startOnRandomNode();

			while (ant.getPathTaken().size() < ant.getNodeNames().length) {
				//				/**
				//				 * this was necessary because of an inexplicable (by me) bug.
				//				 */
				//				if("".equals(ant.getCurrentNode()))
				//					ant.startOnRandomNode();

				ant.moveToNextNode();

			}
			pheremoneManager.saveRoute(ant
					,i);

		}


		System.out.println(pheremoneManager.getAllChanges().toString());

	}

}
