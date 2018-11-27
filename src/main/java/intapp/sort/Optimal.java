/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intapp.sort;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Optimal {

	Map<String, Map<String, LinkedList<Integer>>> tmpMap;
	PresentationProblem p;
	int min;
	PresentationProblem seged;

	public Optimal(PresentationProblem p) {
		this.p = p;
	}

	public Optimal(PresentationProblem p, int min) {
		this.p = p;
		this.min = min;
	}

	private LinkedList<Node> openNodes;
	private LinkedList<Node> closedNodes;
	private Node node;

	public boolean run() {
		tmpMap = new HashMap<String, Map<String, LinkedList<Integer>>>();
		HashMap<String, LinkedList<PresentationOperator>> actualDayOperatorList;
		// szétszedni napokra az összes
		actualDayOperatorList = sortedDays();
		for (Map.Entry<String, LinkedList<PresentationOperator>> operators : actualDayOperatorList.entrySet()) {
			closedNodes = new LinkedList<>();
			openNodes = new LinkedList<>();
			openNodes.add(new Node(p.startMapState(operators.getValue()), null, null, null, operators.getValue()));
			int target = 0;
			while (true) {
				if (openNodes.isEmpty()) {
					break;
				}

				node = openNodes.remove(getLowCostNode(openNodes));
				//Teljes
//				if (node.state.isGoalMap(p)) {
//					break;
//				}
				//Naponkenti 
				if (node.state.isGoalMap(node.getOperators())) {
					break;
				}

				closedNodes.add(node);
				for (PresentationOperator op : node.getOperators()) {
					if (op.isApplicableMap(node.state, op, node.getOperators(), p.getSection())) {
						PresentationState newState = op.applyMap(node.state, op, p.getSection(), node.getOperators());
						if (newState != null) {
							if (searchMap(closedNodes, newState)) {
								continue;
							}
							Node newNode = null;
							LinkedList<PresentationOperator> avaibleOperators = getOperatorList(newState.getMapTabel(),operators.getValue()); 
							newNode = new Node(newState, op, node, operators.getValue(), avaibleOperators);
							// if (newNode.typecost > 3) {
							// continue;
							// }
							openNodes.add(newNode);
						}
					}
				}
				target++;
			}
			tmpMap.put(operators.getKey(), node.getState().getMapTabel());
		}
		return true;
	}

	private LinkedList<PresentationOperator> getOperatorList(Map<String, LinkedList<Integer>> mapTabel,
			LinkedList<PresentationOperator> allOperators) {
		LinkedList<Integer> usedOperatorList = new LinkedList<>();
		LinkedList<PresentationOperator> avaibleOperators = new LinkedList<>();
		for(Entry<String, LinkedList<Integer>> operators : mapTabel.entrySet()){
			usedOperatorList.addAll(operators.getValue());
		}
		for(PresentationOperator operator : allOperators){
			if(!usedOperatorList.contains(operator.getId())){
				avaibleOperators.add(operator);
			}
		}
		return avaibleOperators;
	}

	private LinkedList<Integer> getPiorityList(PresentationState state) {
		int temp[][] = state.getTable();
		LinkedList<Integer> tempList = new LinkedList<>();
		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < temp[i].length; j++) {
				if (temp[i][j] != 0) {
					tempList.add(temp[i][j]);
				}
			}
		}
		return tempList;
	}

	private int getLowCostNode(LinkedList<Node> openNodes) {
		int index = 0, i = 0;
		int cost = Integer.MAX_VALUE;
		int stateCost = 0;
		for (Node node : openNodes) {
			stateCost = node.getCost();
			if (cost > stateCost) {
				cost = stateCost;
				index = i;
			}
			i++;
		}
		return index;
	}

	private boolean searchMap(List<Node> nodeList, PresentationState state) {
		Map<String, LinkedList<Integer>> actualMap = state.getMapTabel();
		for (Node node : nodeList) {
			Map<String, LinkedList<Integer>> nodeStateMap = node.getState().getMapTabel();
			if (nodeStateMap.equals(actualMap)) {
				return true;
			}
		}
		return false;
	}

	public String getGoal() {
		return (String) node.state.getGoal();
	}

	public int[][] GetTabla() {
		return node.state.getTable();
	}

	public Map<String, LinkedList<Integer>> GetMapTabla() {
		return node.state.getMapTabel();
	}

	private HashMap<String, LinkedList<PresentationOperator>> sortedDays() {
		HashMap<String, LinkedList<PresentationOperator>> actualDayTempOperatorList = new HashMap<>();
		for (PresentationOperator operator : p.getOperators()) {
			if (actualDayTempOperatorList.containsKey(operator.getTo())) {
				LinkedList<PresentationOperator> tmpList = actualDayTempOperatorList.get(operator.getTo());
				tmpList.add(operator);
				actualDayTempOperatorList.put(operator.getTo(), new LinkedList<>(tmpList));
			} else {
				LinkedList<PresentationOperator> tmpList = new LinkedList<>();
				tmpList.add(operator);
				actualDayTempOperatorList.put(operator.getTo(), new LinkedList<>(tmpList));
			}
		}
		return actualDayTempOperatorList;
	}

	public Map<String, Map<String, LinkedList<Integer>>> GetFullTabla() {
		Map<String, Map<String, LinkedList<Integer>>> actualTmpMap = new HashMap<>();
		for (Map.Entry<String, Map<String, LinkedList<Integer>>> days : tmpMap.entrySet()) {
			Map<String, LinkedList<Integer>> tempMap = new HashMap<>();
			tempMap = days.getValue();
			Map<String, LinkedList<Integer>> temp = new HashMap<>();
			String section = "";
			LinkedList<Integer> deDays = new LinkedList<>();
			LinkedList<Integer> duDays = new LinkedList<>();
			for (Map.Entry<String, LinkedList<Integer>> entry : tempMap.entrySet()) {
				deDays = new LinkedList<>();
				duDays = new LinkedList<>();
				for (Integer id : entry.getValue()) {
					for(PresentationOperator o : p.getOperators()){
						if(o.getId() == id && o.getTimeTo().contains("DE")){
							deDays.add(id);
							break;
						}else if(o.getId() == id && (o.getTimeTo().contains("DU") || o.getId() == id && o.getTimeTo().contains("BAR"))){
							duDays.add(id);
							break;
						}
					}
				}
				temp.put(entry.getKey()+"DE", deDays);
				temp.put(entry.getKey()+"DU", duDays);
			}
			actualTmpMap.put(days.getKey(), temp);
		}
		return actualTmpMap;
	}
}
