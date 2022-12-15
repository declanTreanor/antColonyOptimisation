package com.eon.ants;

import com.eon.ants.concurrrency.LockObject;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
public class Arc {

	@Autowired
	private LockObject lockObject;
	private Node from;
	private Node to;
	private double pheremoneLevel;
	protected void addPheremones(double pheremones){
		lockObject.lock();
		this.pheremoneLevel += pheremones;
		lockObject.unlock();
	}
}
