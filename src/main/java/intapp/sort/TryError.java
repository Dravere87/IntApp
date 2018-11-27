/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intapp.sort;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TryError {

	PresentationProblem p;
	PresentationProblem seged;

	public TryError(PresentationProblem p) {
		this.p = p;
	}

	private LinkedList<Node> openNodes;
	private LinkedList<Node> closedNodes;
	private Node node;

	public boolean run() {
		closedNodes = new LinkedList<>();
		openNodes = new LinkedList<>();
		openNodes.add(new Node(p.startState(), null, null, null));
		while (true) {
			if (openNodes.isEmpty()) {
				return false;
			}

			Random randomGenerator = new Random();
			int randomInt = randomGenerator.nextInt(openNodes.size());
			node = openNodes.remove(randomInt);
			if (node.state.isGoal(p)) {
				return true;
			}

			closedNodes.add(node);
			randomInt = randomGenerator.nextInt(p.operators().size());
			PresentationOperator op = PresentationProblem.operators.get(randomInt);
				if (op.isApplicable(node.state, op, PresentationProblem.operators)) {
					PresentationState newState = op.apply(node.state, op);
					if (newState != null) {
						if (search(closedNodes, newState)) {
							continue;
						}
						Node newNode = null;
						newNode = new Node(newState, op, node, PresentationProblem.operators);
						openNodes.addLast(newNode);
					}
				}
		}
	}

	private boolean search(List<Node> nodeList, PresentationState state) {
		int[][] actualState = state.getTable();
		ArrayList<Integer> nodeArrayList = new ArrayList<>();
		ArrayList<Integer> stateArrayList = new ArrayList<>();
		for (Node node : nodeList) {
			int[][]nodeState = node.getState().getTable();
			for (int i = 0; i < nodeState.length; i++) {
				nodeArrayList = new ArrayList<>();
				stateArrayList = new ArrayList<>();
				for (int j = 0; j < nodeState[i].length; j++) {
					if(nodeState[i][j] != 0){
						nodeArrayList.add(nodeState[i][j]);
					}
					if(actualState[i][j] != 0){
						stateArrayList.add(actualState[i][j]);
					}
				}
				if(nodeArrayList.contains(stateArrayList)){
					return true;
				}
			}
		}
		return false;
	}

//	private Node search(List<Node> nodeList, PresentationState state) {
//		for (Node node : nodeList) {
//			if (state.equals(node.state)) {
//				return node;
//			}
//		}
//		return null;
//	}
	
	public int GetLastCost(){
		return node.cost;
	}

	public String getGoal() {
		return (String) node.state.getGoal();
	}

	public int[][] GetTabla() {
		return node.state.getTable();
	}
}
