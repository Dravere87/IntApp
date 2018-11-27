package intapp.algorythm;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import intapp.model.Section;
import intapp.sort.PresentationOperator;
import intapp.sort.PresentationProblem;

public class NewGenericAlgorythm {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	LinkedList<NewParent> parentList;
	LinkedList<NewParent> newParentList;
	LinkedList<Parent> originalParentList = new LinkedList<>();
	LinkedList<NewParent> childrenList;
	Map<String, Map<String, LinkedList<Integer>>> tmpMap;
	PresentationProblem p;
	HashMap<String, LinkedList<PresentationOperator>> actualDayOperatorList;
	boolean first = true;
	int target = 0;
	int min;
	PresentationProblem seged;
	Map<String, LinkedList<Integer>> actualMap = new HashMap<>();

	public NewGenericAlgorythm(PresentationProblem p) {
		this.p = p;
	}

	public boolean run() {
		target++;
		tmpMap = new HashMap<String, Map<String, LinkedList<Integer>>>();
		// szétszedni napokra az összes
		actualDayOperatorList = sortedDays();
		for (Map.Entry<String, LinkedList<PresentationOperator>> operators : actualDayOperatorList.entrySet()) {
			childrenList = new LinkedList<>();
			initalizeStartParents(operators.getValue(), p.getSection());
			getCostMap(parentList, operators.getValue(), p.getSection());
			calcFitnessSec(parentList);
			Collections.sort(parentList, (a, b) -> a.getFitness().compareTo(b.getFitness()));
			long startTime = System.currentTimeMillis();
			System.out.println("Az eloadasok beosztasa elkezdodott!");
			boolean done = true;
			while(done){
//			while (target <= 25000) {
				target++;
				Random r = new Random();
				double randomValue = 0 + (100 - 0) * r.nextDouble();
				Double sum = 0.0;
				Map<String, LinkedList<Integer>> child1 = new HashMap<>();
				Map<String, LinkedList<Integer>> child2 = new HashMap<>();
				Collections.sort(parentList, (a, b) -> b.getFitness().compareTo(a.getFitness()));
				while (child1.isEmpty()) {
					for (NewParent parent : parentList) {
						if (parent.getFitness() >= randomValue) {
							child1 = parent.getStateMap();
							break;
						}
					}
					randomValue = 0 + (100 - 0) * r.nextDouble();
				}
				randomValue = 0 + (100 - 0) * r.nextDouble();
				sum = 0.0;
				while (child2.isEmpty()) {
					for (NewParent parent : parentList) {
						if (parent.getFitness() > randomValue) {
							child2 = parent.getStateMap();
							break;
						}
					}
					randomValue = 0 + (100 - 0) * r.nextDouble();
				}
				keresztezes(child1, child2, operators.getValue());
				if (childrenList.size() == 10) {
					target++;
					for (NewParent newParent : childrenList) {
						parentList.add(new NewParent(newParent.getStateMap(), null));
					}
					getCostMap(parentList, operators.getValue(), p.getSection());
					Collections.sort(parentList, (a, b) -> a.getCost().compareTo(b.getCost()));
					for (int i = 0; i < 10; i++) {
						parentList.removeLast();
					}
					calcFitnessSec(parentList);
					childrenList = new LinkedList<>();
				}
				 long endTime = System.currentTimeMillis();
				 long totalTime = endTime - startTime;
				 if(totalTime >= 3000){
					 done = false;
				 }
			}
			tmpMap.put(operators.getKey(), parentList.getFirst().getStateMap());
		}
		// calcStartTime();
		return true;
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

	private void keresztezes(Map<String, LinkedList<Integer>> child1, Map<String, LinkedList<Integer>> child2,
			Collection<PresentationOperator> operators) {
		int i = 0;
		Map<String, LinkedList<Integer>> temp1Map = new HashMap<>();
		Map<String, LinkedList<Integer>> temp2Map = new HashMap<>();
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(operators.size()) + 1;
		for (PresentationOperator presentationOperator : operators) {
			if (i <= operators.size() / randomInt) {
				for (Map.Entry<String, LinkedList<Integer>> entry : child2.entrySet()) {
					LinkedList<Integer> lista = entry.getValue();
					if (lista != null && lista.contains(presentationOperator.getId())) {
						if (temp1Map.get(entry.getKey()) != null) {
							LinkedList<Integer> tmpList = temp1Map.get(entry.getKey());
							tmpList.add(presentationOperator.getId());
							temp1Map.put(new String(entry.getKey()), new LinkedList<>(tmpList));
							break;
						} else {
							LinkedList<Integer> tmpList = new LinkedList<>();
							tmpList.add(presentationOperator.getId());
							temp1Map.put(new String(entry.getKey()), new LinkedList<>(tmpList));
							break;
						}
					}
				}
				for (Map.Entry<String, LinkedList<Integer>> entry : child1.entrySet()) {
					LinkedList<Integer> lista = entry.getValue();
					if (lista != null && lista.contains(presentationOperator.getId())) {
						if (temp2Map.get(entry.getKey()) != null) {
							LinkedList<Integer> tmpList = temp2Map.get(entry.getKey());
							tmpList.add(presentationOperator.getId());
							temp2Map.put(new String(entry.getKey()), new LinkedList<>(tmpList));
							break;
						} else {
							LinkedList<Integer> tmpList = new LinkedList<>();
							tmpList.add(presentationOperator.getId());
							temp2Map.put(new String(entry.getKey()), new LinkedList<>(tmpList));
							break;
						}
					}
				}
			}
			if (i > operators.size() / randomInt) {
				for (Map.Entry<String, LinkedList<Integer>> entry : child2.entrySet()) {
					LinkedList<Integer> lista = entry.getValue();
					if (lista != null && lista.contains(presentationOperator.getId())) {
						if (temp2Map.get(entry.getKey()) != null) {
							LinkedList<Integer> tmpList = temp2Map.get(entry.getKey());
							tmpList.add(presentationOperator.getId());
							temp2Map.put(new String(entry.getKey()), new LinkedList<>(tmpList));
							break;
						} else {
							LinkedList<Integer> tmpList = new LinkedList<>();
							tmpList.add(presentationOperator.getId());
							temp2Map.put(new String(entry.getKey()), new LinkedList<>(tmpList));
							break;
						}
					}
				}
				for (Map.Entry<String, LinkedList<Integer>> entry : child1.entrySet()) {
					LinkedList<Integer> lista = entry.getValue();
					if (lista != null && lista.contains(presentationOperator.getId())) {
						if (temp1Map.get(entry.getKey()) != null) {
							LinkedList<Integer> tmpList = temp1Map.get(entry.getKey());
							tmpList.add(presentationOperator.getId());
							temp1Map.put(new String(entry.getKey()), new LinkedList<>(tmpList));
							break;
						} else {
							LinkedList<Integer> tmpList = new LinkedList<>();
							tmpList.add(presentationOperator.getId());
							temp1Map.put(new String(entry.getKey()), new LinkedList<>(tmpList));
							break;
						}
					}
				}
			}
			i++;
		}
//		Random rand = new Random();
//		int randomNumber = rand.nextInt(100) + 1;
//		if (randomNumber <= 10) {
//			temp1Map = mutacio(temp1Map);
//		}
//		randomNumber = rand.nextInt(100) + 1;
//		if (randomNumber <= 10) {
//			temp2Map = mutacio(temp2Map);
//		}
		if (checkState(temp1Map, p.getOperators())) {
			childrenList.add(new NewParent(new HashMap<>(temp1Map), 0.00));
		}
		if (checkState(temp2Map, p.getOperators())) {
			childrenList.add(new NewParent(new HashMap<>(temp2Map), 0.00));
		}
	}

	private void calcFitness(LinkedList<NewParent> parentList) {
		Double actualCost = 0.0;
		Double sumFitness = 0.0;
		for (int i = 0; i < parentList.size(); i++) {
			sumFitness += 1.00 / Double.parseDouble(parentList.get(i).getCost().toString());
		}
		for (int i = 0; i < parentList.size(); i++) {
			actualCost = 1.00 / Double.parseDouble(parentList.get(i).getCost().toString());
			parentList.get(i).setFitness(((actualCost / sumFitness) * 100.0));
		}
	}

	private void calcFitnessSec(LinkedList<NewParent> parentList) {
		Double actualCost = 0.0;
		Double sumFitness = 0.0;
		for (int i = 0; i < parentList.size(); i++) {
			sumFitness += Double.parseDouble(parentList.get(i).getCost().toString());
		}
		for (int i = 0; i < parentList.size(); i++) {
			actualCost = sumFitness - Double.parseDouble(parentList.get(i).getCost().toString());
			parentList.get(i).setFitness(((actualCost / sumFitness) * 100.0));
		}
	}

	public void getCostMap(LinkedList<NewParent> parent, LinkedList<PresentationOperator> operators, Section section) {
		List<String> typeList = new ArrayList<>();
		Map<String, Integer> typeCostMap = new HashMap<>();
		for (PresentationOperator operator : operators) {
			typeCostMap.put(operator.getTopic(), -1);
		}
		typeList = new ArrayList<>();
		double sumDb = 0.0;
		int db = 0;
		int cost = 0;
		for (int i = 0; i < parent.size(); i++) {
			cost = 0;
			Map<String, LinkedList<Integer>> temp = parent.get(i).getStateMap();
			PresentationOperator presentation = null;
			sumDb = 0.0;
			for (Map.Entry<String, LinkedList<Integer>> entry : temp.entrySet()) {
				int error = 0;
				typeList = new ArrayList<>();
				LinkedList<Integer> lista = entry.getValue();
				if (lista != null) {
					error = 0;
					String last = "";
					for (int j = 0; j < lista.size(); j++) {
						for (PresentationOperator o : operators) {
							if (lista.get(j) == o.getId()) {
								presentation = o;
								break;
							}
						}
						if (presentation != null && !typeList.contains(presentation.getTopic())) {
							typeList.add(presentation.getTopic());
						}
						if (j > 0 && presentation != null && last != presentation.getTopic()) {
							error++;
						}
						if (presentation != null) {
							last = presentation.getTopic();
						}
					}
				}
				if (typeList != null) {
					db += typeList.size() - 1;
				}
				if (db < 0) {
					db = 0;
				}
				cost += db;
				cost += error;
				error = 0;
				db = 0;
			}
			sumDb += cost;
			cost = 0;
			parent.get(i).setCost((sumDb));
		}
	}

	public void initalizeStartParents(LinkedList<PresentationOperator> operators, Section section) {
		Random randomGenerator = new Random();
		parentList = new LinkedList<>();
		while (parentList.size() < 10) {
			int count = 0;
			Map<String, LinkedList<Integer>> tmpMap = new HashMap<>();
			for (int i = 0; i < section.getSections().size(); i++) {
				tmpMap.put(section.getSections().get(i) + "DE", null);
				tmpMap.put(section.getSections().get(i) + "DU", null);
			}
			for (PresentationOperator presentationOperator : operators) {
				int randomInt = randomGenerator.nextInt(tmpMap.size());
				count = 0;
				for (Map.Entry<String, LinkedList<Integer>> entry : tmpMap.entrySet()) {
					if (count == randomInt) {
						if (tmpMap.get(entry.getKey()) != null) {
							LinkedList<Integer> tmpList = new LinkedList<>();
							tmpList = tmpMap.get(entry.getKey());
							tmpList.add(presentationOperator.getId());
							tmpMap.put(entry.getKey(), tmpList);
						} else {
							LinkedList<Integer> tmpList = new LinkedList<>();
							tmpList.add(presentationOperator.getId());
							tmpMap.put(entry.getKey(), tmpList);
						}
						break;
					} else {
						count++;
					}
				}
			}
			if (checkState(tmpMap, operators)) {
				parentList.add(new NewParent(tmpMap, 0.00));
			}
		}
	}

	private boolean checkState(Map<String, LinkedList<Integer>> tmpMap, LinkedList<PresentationOperator> operators) {
		LocalTime localtime = null;
		LocalTime localtimeEnd = null;
		LocalTime localtimeDel = null;
		LocalTime localtime13 = null;
		for (Map.Entry<String, LinkedList<Integer>> entry : tmpMap.entrySet()) {
			LinkedList<Integer> tmpList = entry.getValue();
			if (tmpList != null) {
				localtime = LocalTime.parse(p.getSection().getFrom(), formatter);
				localtimeEnd = LocalTime.parse(p.getSection().getTo(), formatter);
				localtimeDel = LocalTime.parse("12:00", formatter);
				localtime13 = LocalTime.parse("13:00", formatter);
				for (Integer integer : tmpList) {
					for (PresentationOperator presentationOperator : operators) {
						if (integer == presentationOperator.getId()) {
							if (entry.getKey().contains("DE")) {
								if ("DU".equals(presentationOperator.getFrom())) {
									return false;
								}
								String tmpInter = presentationOperator.getInter();
								String[] interArray = tmpInter.split("\\.");
								localtime = localtime.plusMinutes(Integer.parseInt(interArray[0]));
								if (localtime.isAfter(localtimeDel)) {
									return false;
								}
							} else if (entry.getKey().contains("DU")) {
								if ("DE".equals(presentationOperator.getFrom())) {
									return false;
								}
								String tmpInter = presentationOperator.getInter();
								String[] interArray = tmpInter.split("\\.");
								localtime13 = localtime13.plusMinutes(Integer.parseInt(interArray[0]));
								if (localtime13.isAfter(localtimeEnd)) {
									return false;
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

	private Map<String, LinkedList<Integer>> mutacio(Map<String, LinkedList<Integer>> childrenMap) {
		Map<String, LinkedList<Integer>> actualMap = new HashMap<>();
		Random rand = new Random();
		int from = p.getOperators().size();
		int randomNumber = rand.nextInt(from);
		PresentationOperator op1 = p.getOperators().get(randomNumber);
		randomNumber = rand.nextInt(from);
		PresentationOperator op2 = p.getOperators().get(randomNumber);
		for (Map.Entry<String, LinkedList<Integer>> entry : childrenMap.entrySet()) {
			LinkedList<Integer> tmpList = entry.getValue();
			LinkedList<Integer> newList = new LinkedList<>();
			if (tmpList != null) {
				for (Integer id : tmpList) {
					if (id == op1.getId()) {
						newList.add(op2.getId());
					} else if (id == op2.getId()) {
						newList.add(op1.getId());
					} else {
						newList.add(id);
					}
				}
			}
			actualMap.put(entry.getKey(), newList);
		}
		return actualMap;
	}

	public Map<String, LinkedList<Integer>> GetMapFullTabla() {
		return parentList.getFirst().getStateMap();
	}

	public Map<String, Map<String, LinkedList<Integer>>> GetFullTabla() {
		return tmpMap;
	}
}
