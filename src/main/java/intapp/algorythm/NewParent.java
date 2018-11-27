package intapp.algorythm;

import java.util.LinkedList;
import java.util.Map;

public class NewParent {
	Map<String, LinkedList<Integer>> stateMap;
	Double cost;
	Double fitness;

	public NewParent(Map<String, LinkedList<Integer>> stateMap, Double cost) {
		super();
		this.stateMap = stateMap;
		this.cost = cost;
	}

	public Map<String, LinkedList<Integer>> getStateMap() {
		return stateMap;
	}

	public void setStateMap(Map<String, LinkedList<Integer>> stateMap) {
		this.stateMap = stateMap;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Double getFitness() {
		return fitness;
	}

	public void setFitness(Double fitness) {
		this.fitness = fitness;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cost == null) ? 0 : cost.hashCode());
		result = prime * result + ((fitness == null) ? 0 : fitness.hashCode());
		result = prime * result + ((stateMap == null) ? 0 : stateMap.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewParent other = (NewParent) obj;
		if (cost == null) {
			if (other.cost != null)
				return false;
		} else if (!cost.equals(other.cost))
			return false;
		if (fitness == null) {
			if (other.fitness != null)
				return false;
		} else if (!fitness.equals(other.fitness))
			return false;
		if (stateMap == null) {
			if (other.stateMap != null)
				return false;
		} else if (!stateMap.equals(other.stateMap))
			return false;
		return true;
	}
}
