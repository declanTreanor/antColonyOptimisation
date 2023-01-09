package com.eon.ants.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

	@Bean("pheremones")//"circus", "balloon", "bumpers", "carousel", "swings", "ferrisWheel"
	public double[][] initialPheremones(){
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
