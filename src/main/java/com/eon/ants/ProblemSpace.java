package com.eon.ants;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Data
public class ProblemSpace {
	private double[][] adjacencyMatrix;
	protected static String [] nodeNames = new String[]{"circus", "balloon", "bumpers", "carousel", "swings", "ferrisWheel"};

	/**
	 *
	 */
	private double totalPheremones;

}
