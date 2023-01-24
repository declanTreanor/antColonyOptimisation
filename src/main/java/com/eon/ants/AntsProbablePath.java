package com.eon.ants;

import lombok.Getter;
import org.apache.commons.lang3.Range;

@Getter
public class AntsProbablePath{


	private String nodeName;
	private double probability;
	private Range<Double> rangeOfProbabilities;

	public AntsProbablePath(String nodeName, double probability){
		this.nodeName=nodeName;
		this.probability=probability;
	}
	public void setRangeOfProbabilities(Range<Double> rangeOfProbabilities){
		this.rangeOfProbabilities=rangeOfProbabilities;
	}

}
