package com.eon.ants;

//import jdk.incubator.concurrent.StructuredTaskScope;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@SpringBootApplication
public class AntsApplication {
	private double[][] distanceMatrix={ { 0, 8, 7, 4, 6, 4 },
			{ 8, 0, 5, 7, 11, 5 },
			{ 7, 5, 0, 9, 6, 7 },
			{ 4, 7, 9, 0, 5, 6 },
			{ 6, 11, 6, 5, 0, 3 },
			{ 4, 5, 7, 6, 3, 0 }
	};

//	protected class AntPathScope extends StructuredTaskScope<List<String>>{
//		private List<String> shortestPath;
//		//Here! get fastest ANT!
//		protected List<String> getShortestPath(){
//			return null;
//		}
//
//		@Override
//		protected void handleComplete(Future<List<String>> future) {
//			super.handleComplete(future);
//			switch(future.state()){
//				case RUNNING -> throw new RuntimeException("oops");
//				case SUCCESS -> {
//					try {
//						updateShortestPath(future.get());
//					} catch (InterruptedException e) {
//						throw new RuntimeException(e);
//					} catch (ExecutionException e) {
//						throw new RuntimeException(e);
//					}
//				}
//			}
//
//		}
//
//		private void updateShortestPath(List<String> path) {
//			if(isShorter(path)){
//				this.shortestPath = path;
//			}
//		};
//
//		private boolean isShorter(List<String> path) {
//			return false;
//
//
//
//		}
//	}
	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(AntsApplication.class, args);
//		new AntsApplication().findShortestPath();

	}

//	private void findShortestPath() throws InterruptedException {
//		try (var scope = new AntPathScope()) {
//			for(int i=0; i<100; i++) {
//				scope.fork(() -> getShortestPath());
//			}
//
//
//			scope.join(); //blocking
//
//			//			return scope.result();
//		}
//
//	}

	private List<String> getShortestPath() {

		ProblemSpace ps = new ProblemSpace();
		Ant ant = new Ant(ps);
		ant.run();
		return ant.getPathTaken();
	}

}
