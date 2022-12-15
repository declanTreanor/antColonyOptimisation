package com.eon.ants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Setter
@Getter
@Component
public class Node {
	private String name;


	public Set<Node> getAllConnectedNodes() {
		return allConnections().keySet();
	}
	public Map<Node, Double> allConnections(){
	return null;
	}

	public Arc getFromArc(Node to) {
		return null;
	}
}
