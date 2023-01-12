package com.eon.ants;

//import jdk.incubator.concurrent.StructuredTaskScope;
import com.eon.ants.config.ACOConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

@SpringBootApplication
public class AntsApplication {
	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(AntsApplication.class, args);
//		new AntsApplication().findShortestPath();
		ApplicationContext ctx = new AnnotationConfigApplicationContext(ACOConfig.class);
		PheremoneManager pheremoneManager = null;
		Ant ant = null;
		for(int i =9000; i>0; i--){
			if(i%5==0){
				pheremoneManager = (PheremoneManager) ctx.getBean("PheremoneManager");
				pheremoneManager.evaporate(0.9D);

			}

			ant = (Ant) ctx.getBean("Ant");
			ant.startOn("swings");
			while(ant.getPathTaken().size()<ant.getNodeNames().length) {
				/**
				 * this was necessary because of an inexplicable (by me) bug.
				 */
				if("".equals(ant.getCurrentNode()))
					ant.startOnRandomNode();

				ant.moveToNextNode();
			}
		}
		System.out.println(ant.toString());
		double [][] pheremoneTrails = ant.getPheremoneManager().getPheremoneTrails();
		System.out.println(pheremoneManager.displayShortestPath());

	}


}
