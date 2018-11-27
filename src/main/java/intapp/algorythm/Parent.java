package intapp.algorythm;

import intapp.sort.PresentationState;

public class Parent {
	PresentationState parentState;
	Integer cost;

	public Parent(PresentationState parentState) {
		super();
		this.parentState = parentState;
	}

	public Parent(PresentationState parentState, Integer cost) {
		super();
		this.parentState = parentState;
		this.cost = cost;
	}

	public PresentationState getParentState() {
		return parentState;
	}

	public void setParentState(PresentationState parentState) {
		this.parentState = parentState;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}
}
