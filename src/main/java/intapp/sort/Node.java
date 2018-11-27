package intapp.sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Node {
    PresentationState state;
    Node parent;
    int cost;
    int typecost;
    LinkedList<PresentationOperator> operators;

    public Node(PresentationState state, PresentationOperator op, Node parent, LinkedList<PresentationOperator> operators ) {
        this.cost = parent == null ? 0 : getCostMap(state, operators);
        this.parent = parent;
        this.state = state;
        this.typecost = parent == null ? 0 : getCostMap(state, operators);
    }
    
    public Node(PresentationState state, PresentationOperator op, Node parent, LinkedList<PresentationOperator> operators, LinkedList<PresentationOperator> availableOperators) {
        this.cost = parent == null ? 0 : getCostMap(state, operators);
        this.parent = parent;
        this.state = state;
        this.typecost = parent == null ? 0 : getCostMap(state, operators);
        this.operators = availableOperators;
    }

	private int getWeightPay(PresentationOperator op) {
    	PresentationOperator operator = (PresentationOperator)op;
		return 100 - operator.getWeight();
	}

	@Override
    public String toString() {
        return state.toString();
    }
	
	public LinkedList<PresentationOperator> getOperators() {
		return operators;
	}

	public void setOperators(LinkedList<PresentationOperator> operators) {
		this.operators = operators;
	}

	static int getCostMap(PresentationState s, LinkedList<PresentationOperator> operators) {
		Map<Integer, PresentationOperator> presentationMap = new HashMap<Integer, PresentationOperator>();
		for (PresentationOperator operator : operators) {
			presentationMap.put(operator.getId(), operator);
		}
		List<String> typeList = new ArrayList<>();
		int db = 0;
		int cost = 0;
		Map<String, LinkedList<Integer>> temp = s.getMapTabel();
		PresentationOperator presentation = null;
		for (Map.Entry<String, LinkedList<Integer>> entry : temp.entrySet()) {
			db=0;
			typeList = new ArrayList<>();
			LinkedList<Integer> lista = entry.getValue();
			for (int j = 0; j < lista.size(); j++) {
				for (PresentationOperator o : operators) {
					if (lista.get(j) == o.getId()) {
						presentation = o;
						break;
					}
				}
				if (!typeList.contains(presentation.getTopic())) {
					typeList.add(presentation.getTopic());
				}
			}
			if (typeList != null) {
				db += typeList.size() - 1;
			}
			if (db < 0) {
				db = 0;
			}
			cost += db;
		}
		return cost;
	}
	
	static int getCost(PresentationState s, LinkedList<PresentationOperator> operators){
		Map<Integer, PresentationOperator> presentationMap = new HashMap<Integer, PresentationOperator>();
		for (PresentationOperator operator : operators) {
			presentationMap.put(operator.getId(), operator);
		}
    	List<String> typeList = new ArrayList<>();
    	List<String> actorList = new ArrayList<>();
		int db = 0;
		int cost = 0;
		int tabla[][] = s.getTable();
		for (int i = 0; i < tabla.length; i++) {
			db=0;
			typeList = new ArrayList<>();
			actorList = new ArrayList<>();
			for (int j = 0; j < tabla[i].length; j++) {
				if (tabla[i][j] != 0){
					PresentationOperator presentation = null;
					for (PresentationOperator o : operators) {
						if(tabla[i][j] == o.getId()){
							presentation = o;
						}
					}
					if(!typeList.contains(presentation.getTopic())) {
						typeList.add(presentation.getTopic());
					}
					if(!actorList.contains(presentation.getActor())) {
						actorList.add(presentation.getTopic());
					}
					if(actorList.contains(presentation.getActor())) {
						cost += 5;
					}
				}
			}
			if(typeList!= null){
				db += typeList.size()-1;
			}
			if(db<0){
				db = 0;
			}
			cost += db;
		}
		return cost;
    }

	public PresentationState getState() {
		return state;
	}

	public void setState(PresentationState state) {
		this.state = state;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getTypecost() {
		return typecost;
	}

	public void setTypecost(int typecost) {
		this.typecost = typecost;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cost;
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		Node other = (Node) obj;
		if (cost != other.cost)
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}
}
