package intapp.algorythm;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import intapp.model.DateOperator;
import intapp.model.Section;
import intapp.sort.PresentationOperator;
import intapp.sort.PresentationProblem;
import intapp.sort.PresentationState;


public class GeneticAlgorithm {
	DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
	LinkedList<Parent> parentList;
	LinkedList<Parent> originalParentList = new LinkedList<>();
	LinkedList<Parent> childrenList;
	PresentationProblem p;
	boolean first = true;
	int min;
	PresentationProblem seged;
	Map<String, LinkedList<Integer>> actualMap = new HashMap<>();

	public GeneticAlgorithm(PresentationProblem p) {
		this.p = p;
	}

	public boolean run() {
		childrenList = new LinkedList<>();
		LinkedList<Integer> selectedList = new LinkedList<>();
		Map<Integer, String> p1Map = new HashMap<>();
		Map<Integer, String> p2Map = new HashMap<>();
		Parent p1 = null;
		Parent p2 = null;
		LinkedList<Integer> p1List = new LinkedList<>();
		LinkedList<Integer> p2List = new LinkedList<>();
		LinkedList<Integer> tmpList = null;
		Map<String, LinkedList<Integer>> tmpMap = new HashMap<>();
		Map<String, LinkedList<Integer>> childrenMap = new HashMap<>();
		initalizeStartParents(p.getOperators());
		getCostMap(parentList, p.getOperators(), p.getSection());
		Collections.sort(parentList, (a, b) -> a.getCost().compareTo(b.getCost()));
		childrenList.add(parentList.get(0));
		childrenList.add(parentList.get(1));
		for (int i = 0; i < parentList.size(); i++) {
			originalParentList.add(parentList.get(i));
		}
		boolean change = false;
		int tryred = 0;
		boolean checkFalse = false;
		while (true) {
			for (int d = 0; d < p.getSection().getDays().size(); d++) {
				childrenList = new LinkedList<>();
				tryred = 0;
				parentList = new LinkedList<>();
				for (int i = 0; i < originalParentList.size(); i++) {
					parentList.add(originalParentList.get(i));
				}
				while (tryred < 10000) {
					for (int i = 0; i < parentList.size(); i++) {
						if (i % 2 == 0) {
							p1 = parentList.get(i);
							continue;
						} else if (i % 2 == 1) {
							p2 = parentList.get(i);
						}
						if (p1 != null && p2 != null) {
							checkFalse = false;
							p1Map = new HashMap<>();
							p2Map = new HashMap<>();
							p1List = new LinkedList<>();
							p2List = new LinkedList<>();
							tmpMap = p1.parentState.getMapTabel();
							for (Map.Entry<String, LinkedList<Integer>> map : tmpMap.entrySet()) {
								if (map.getKey().contains(p.getSection().getDays().get(d))) {
									tmpList = map.getValue();
									p1List.addAll(map.getValue());
									for (int j = 0; j < tmpList.size(); j++) {
										p1Map.put(tmpList.get(j), map.getKey());
									}
								}
							}
							tmpMap = p2.parentState.getMapTabel();
							for (Map.Entry<String, LinkedList<Integer>> map : tmpMap.entrySet()) {
								if (map.getKey().contains(p.getSection().getDays().get(d))) {
									tmpList = map.getValue();
									p2List.addAll(map.getValue());
									for (int j = 0; j < tmpList.size(); j++) {
										p2Map.put(tmpList.get(j), map.getKey());
									}
								}
							}
							selectedList = kivalasztas(p1List, p2List);
							childrenMap = keresztezes(p1Map, p2Map, selectedList, p.getSection().getSections());
							Random rand = new Random();
							int randomNumber = rand.nextInt(100) + 1;
							if (randomNumber <= 10) {
								childrenMap = mutacio(childrenMap);
							}
							if (checkState(childrenMap, p.getOperators(), p.getSection())) {
								PresentationState state = new PresentationState();
								state.setMapTabel(childrenMap);
								Parent parent = new Parent(state);
								childrenList.add(parent);
							}
							p1List = new LinkedList<>();
							p2List = new LinkedList<>();
							p1 = null;
							p2 = null;
							if (childrenList.size() == 10) {
								change = true;
								break;
							}
						}
					}
					if (change) {
						parentList = new LinkedList<>();
						for (int i = 0; i < childrenList.size(); i++) {
							PresentationState tmpState = childrenList.get(i).getParentState();
							Map<String, LinkedList<Integer>> tmpM = tmpState.getMapTabel();
							Map<String, LinkedList<Integer>> insertMap = new HashMap<>();
							for (Map.Entry<String, LinkedList<Integer>> map : tmpM.entrySet()) {
								insertMap.put(new String(map.getKey()), new LinkedList<>(map.getValue()));
							}
							PresentationState insertState = new PresentationState();
							insertState.setMapTabel(insertMap);
							Parent tmp = new Parent(insertState);
							parentList.add(tmp);
						}
						childrenList = new LinkedList<>();
						change = false;
						getCostMap(parentList, p.getOperators(), p.getSection());
						Collections.sort(parentList, (a, b) -> a.getCost().compareTo(b.getCost()));
						childrenList.add(parentList.get(0));
						childrenList.add(parentList.get(1));
						tryred++;
					}
				}
				childrenMap = childrenList.get(0).getParentState().getMapTabel();
				for (Map.Entry<String, LinkedList<Integer>> map : childrenMap.entrySet()) {
					actualMap.put(new String(map.getKey()), new LinkedList<>(map.getValue()));
				}
			}
			return false;
		}
	}

	public void getCostMap(LinkedList<Parent> parent, LinkedList<PresentationOperator> operators, Section section) {
		List<String> typeList = new ArrayList<>();
		Map<String, Integer> typeCostMap = new HashMap<>();
		for (PresentationOperator operator : operators) {
			typeCostMap.put(operator.getTopic(), -1);
		}
		typeList = new ArrayList<>();
		int sumDb = 0;
		int db = 0;
		int cost = 0;
		for (int i = 0; i < parent.size(); i++) {
			cost = 0;
			Map<String, LinkedList<Integer>> temp = parent.get(i).parentState.getMapTabel();
			PresentationOperator presentation = null;
			sumDb = 0;
			for (int k = 0; k < section.getDays().size(); k++) {
				db = 0;
				for (Map.Entry<String, LinkedList<Integer>> entry : temp.entrySet()) {
					typeList = new ArrayList<>();
					if (entry.getKey().contains(section.getDays().get(k))) {
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
						db = 0;
					}
				}
				sumDb += cost;
				cost = 0;
			}
			parent.get(i).setCost((sumDb));
		}
	}

	private LinkedList<Integer> kivalasztas(LinkedList<Integer> p1List, LinkedList<Integer> p2List) {
		if (first) {
			LinkedList<Integer> tmpList = new LinkedList<>();
			LinkedList<Integer> reverseList = new LinkedList<>();
			for (int i = 0; i < p2List.size(); i++) {
				reverseList.addFirst(p2List.get(i));
			}
			for (int i = 0; i < p1List.size(); i++) {
				if (i % 2 == 0 && !tmpList.contains(p1List.get(i))) {
					tmpList.addFirst(p1List.get(i));
				}
				if (i % 2 == 1 && !tmpList.contains(reverseList.get(i))) {
					tmpList.addLast(reverseList.get(i));
				}
			}
			if (!p1List.contains(tmpList)) {
				for (int i = 0; i < p1List.size(); i++) {
					if (!tmpList.contains(p1List.get(i))) {
						tmpList.add(p1List.get(i));
					}
				}
			}
			first = false;
			return tmpList;
		} else {
			LinkedList<Integer> tmpList = new LinkedList<>();
			LinkedList<Integer> reverseList = new LinkedList<>();
			for (int i = 0; i < p1List.size(); i++) {
				reverseList.addFirst(p1List.get(i));
			}
			for (int i = 0; i < p2List.size(); i++) {
				if (i % 2 == 0 && !tmpList.contains(p2List.get(i))) {
					tmpList.addFirst(p2List.get(i));
				}
				if (i % 2 == 1 && !tmpList.contains(reverseList.get(i))) {
					tmpList.addLast(reverseList.get(i));
				}
			}
			if (!p2List.contains(tmpList)) {
				for (int i = 0; i < p2List.size(); i++) {
					if (!tmpList.contains(p2List.get(i))) {
						tmpList.add(p2List.get(i));
					}
				}
			}
			first = true;
			return tmpList;
		}
	}

	public void initalizeStartParents(LinkedList<PresentationOperator> operators) {
		parentList = new LinkedList<>();
		Random randomGenerator = new Random();
		while (parentList.size() < 10) {
			PresentationState tmpState = new PresentationState(operators, p.getSection().getSections());
			Map<String, LinkedList<Integer>> stateMap = new HashMap<>();
			stateMap = new HashMap<>();
			stateMap = tmpState.getMapTabel();
			LinkedList<PresentationOperator> tmpOperators = new LinkedList<>();
//			for (PresentationOperator op : operators) {
//				tmpOperators.add(new PresentationOperator(op.getId(), op.getPresentationTitle(), op.getActor(),
//						op.getTopic(), op.getInter(), op.getFrom(), op.getTo(), op.isPiority(), op.getWeight()));
//			}
			for (int i = 0; i < p.getSection().getDays().size(); i++) {
				LinkedList<PresentationOperator> actualDaysOperator = new LinkedList<>();
				for (PresentationOperator presentationOperator : tmpOperators) {
					if (presentationOperator.getFrom().contains(p.getSection().getDays().get(i))) {
						actualDaysOperator.add(presentationOperator);
					}
				}
				while (!actualDaysOperator.isEmpty()) {
					int randomInt = randomGenerator.nextInt(p.getSection().getSections().size());
					String insertStr = p.getSection().getSections().get(randomInt);
					for (Map.Entry<String, LinkedList<Integer>> entry : stateMap.entrySet()) {
						if (entry.getKey().contains(insertStr + "_" + p.getSection().getDays().get(i))) {
							insertStr = entry.getKey();
							break;
						}
					}
					LinkedList<Integer> listId = stateMap.get(insertStr);
					listId.add(actualDaysOperator.removeFirst().getId());
					stateMap.put(insertStr, listId);
				}
			}
			if (checkState(stateMap, operators, p.getSection(), p.getSection().getDays())) {
				parentList.add(new Parent(tmpState));
			}
		}
	}

	private boolean checkState(Map<String, LinkedList<Integer>> stateMap, LinkedList<PresentationOperator> operators,
			Section section, LinkedList<String> dayList) {
		boolean hasNullSection = false;
		LocalTime localtime = null;
		for (int d = 0; d < dayList.size(); d++) {
			for (Map.Entry<String, LinkedList<Integer>> entry : stateMap.entrySet()) {
				if (entry.getKey().contains(dayList.get(d))) {
					LinkedList<Integer> idList = entry.getValue();
					if (idList.isEmpty()) {
						hasNullSection = true;
					}
					localtime = LocalTime.parse(section.getFrom(), formatter);
					PresentationOperator actualPresentation = null;
					for (int i = 0; i < idList.size(); i++) {
						for (PresentationOperator operator : operators) {
							if (idList.get(i).equals(operator.getId())) {
								actualPresentation = operator;
								break;
							}
						}
						String[] from = actualPresentation.getFrom().split(" ");
						String[] to = actualPresentation.getTo().split(" ");
						LocalTime fromLT = LocalTime.parse(from[3], formatter);
						// fromLT = fromLT.plusNanos(1);
						LocalTime toLT = LocalTime.parse(to[3], formatter);
						if (localtime.isBefore(fromLT) || localtime.isBefore(toLT)) {
							String tmpInter = actualPresentation.getInter();
							String[] interArray = tmpInter.split("\\.");
							localtime = localtime.plusMinutes(Integer.parseInt(interArray[0]));
						} else {
							return false;
						}
					}
				}
				if (hasNullSection) {
					return false;
				}
			}
		}
		return true;
	}

	private Map<String, LinkedList<Integer>> mutacio(Map<String, LinkedList<Integer>> childrenMap) {
		Map<String, LinkedList<Integer>> actualMap = new HashMap<>();
		String maxSection = "";
		int maxSectionNumber = Integer.MIN_VALUE;
		int minSectionNumber = Integer.MAX_VALUE;
		String minSection = "";
		// Map<String, Integer> sectionCostMap = new HashMap<String, Integer>();
		for (Map.Entry<String, LinkedList<Integer>> entry : childrenMap.entrySet()) {
			// sectionCostMap.put(entry.getKey(), entry.getValue().size());
			if (entry.getValue().size() < minSectionNumber) {
				minSection = entry.getKey();
				minSectionNumber = entry.getValue().size();
			}
			if (entry.getValue().size() > maxSectionNumber) {
				maxSection = entry.getKey();
				maxSectionNumber = entry.getValue().size();
			}
		}
		LinkedList<Integer> minList = new LinkedList<>();
		LinkedList<Integer> maxList = new LinkedList<>();
		minList = childrenMap.get(minSection);
		maxList = childrenMap.get(maxSection);
		for (int i = maxList.size() / 2; i < maxList.size(); i++) {
			minList.add(maxList.remove(i));
		}
		actualMap.put(minSection, minList);
		actualMap.put(maxSection, maxList);
		for (Map.Entry<String, LinkedList<Integer>> entry : childrenMap.entrySet()) {
			if (!actualMap.containsKey(entry.getKey())) {
				actualMap.put(new String(entry.getKey()), new LinkedList<>(entry.getValue()));
			}
		}
		return actualMap;
	}

	private Map<String, LinkedList<Integer>> keresztezes(Map<Integer, String> p1Map, Map<Integer, String> p2Map,
			LinkedList<Integer> selectedList, LinkedList<String> sections) {
		Map<String, LinkedList<Integer>> tmpMap = new HashMap<>();
		// for (int i = 0; i < sections.size(); i++) {
		// tmpMap.put(sections.get(i), new LinkedList<>());
		// }
		for (Map.Entry<Integer, String> entry : p1Map.entrySet()) {
			{
				if (!tmpMap.containsKey(entry.getValue())) {
					tmpMap.put(entry.getValue(), new LinkedList<>());
				}
			}
		}
		for (Map.Entry<Integer, String> entry : p2Map.entrySet()) {
			{
				if (!tmpMap.containsKey(entry.getValue())) {
					tmpMap.put(entry.getValue(), new LinkedList<>());
				}
			}
		}

		// N szédszedés
		boolean fel = true;
		int border = selectedList.size() / (p.getSection().getSectionNumber());
//		int border = 2;
		int db = 0;
		for (int i = 0; i < selectedList.size(); i++) {
			if (fel && db >= border) {
				fel = false;
				db = 0;
			} else if (!fel && db >= border) {
				fel = true;
				db = 0;
			}
			if (fel) {
				String section = p1Map.get(selectedList.get(i));
				LinkedList<Integer> tmpList = tmpMap.get(section);
				tmpList.add(selectedList.get(i));
				tmpMap.put(section, tmpList);
			}
			if (!fel) {
				String section = p2Map.get(selectedList.get(i));
				LinkedList<Integer> tmpList = tmpMap.get(section);
				tmpList.add(selectedList.get(i));
				tmpMap.put(section, tmpList);
			}
			db++;
		}

		// 2 felé szedés
		// int border = selectedList.size() / 2;
		// for (int i = 0; i < selectedList.size(); i++) {
		// if (i < border) {
		// String section = p1Map.get(selectedList.get(i));
		// LinkedList<Integer> tmpList = tmpMap.get(section);
		// tmpList.add(selectedList.get(i));
		// tmpMap.put(section, tmpList);
		// }
		// if (i >= border) {
		// String section = p2Map.get(selectedList.get(i));
		// LinkedList<Integer> tmpList = tmpMap.get(section);
		// tmpList.add(selectedList.get(i));
		// tmpMap.put(section, tmpList);
		// }
		// }
		Map<String, LinkedList<Integer>> tmpMapSorted = new HashMap<>();
		for (Entry<String, LinkedList<Integer>> entry : tmpMap.entrySet()) {
			List<DateOperator> operatorList = new LinkedList<>();
			LinkedList<Integer> list = entry.getValue();
			for (int i = 0; i < list.size(); i++) {
				for (PresentationOperator operator : p.getOperators()) {
					if (list.get(i).equals(operator.getId())) {
						String[] from = operator.getFrom().split(" ");
						String[] to = operator.getTo().split(" ");
						LocalTime fromLT = LocalTime.parse(from[3], formatter);
						LocalTime toLT = LocalTime.parse(to[3], formatter);
						operatorList.add(new DateOperator(operator.getId(), fromLT, toLT));
						break;
					}
				}

			}
			Collections.sort(operatorList, (a, b) -> a.getFrom().compareTo(b.getFrom()));
			list = new LinkedList<>();
			for (int i = 0; i < operatorList.size(); i++) {
				list.add(operatorList.get(i).getId());
			}
			tmpMapSorted.put(entry.getKey(), list);
		}
		return tmpMapSorted;
	}

	private LinkedList<Map<String, LinkedList<Integer>>> keresztezesGetTwo(Map<Integer, String> p1Map,
			Map<Integer, String> p2Map, LinkedList<Integer> selectedList, LinkedList<String> sections) {
		LinkedList<Map<String, LinkedList<Integer>>> mapList = new LinkedList<>();
		Map<String, LinkedList<Integer>> tmpMap = new HashMap<>();
		Map<String, LinkedList<Integer>> tmpMapInverz = new HashMap<>();
		// for (int i = 0; i < sections.size(); i++) {
		// tmpMap.put(sections.get(i), new LinkedList<>());
		// }
		for (Map.Entry<Integer, String> entry : p1Map.entrySet()) {
			{
				if (!tmpMap.containsKey(entry.getValue())) {
					tmpMap.put(entry.getValue(), new LinkedList<>());
					tmpMapInverz.put(entry.getValue(), new LinkedList<>());
				}
			}
		}
		for (Map.Entry<Integer, String> entry : p2Map.entrySet()) {
			{
				if (!tmpMap.containsKey(entry.getValue())) {
					tmpMap.put(entry.getValue(), new LinkedList<>());
					tmpMapInverz.put(entry.getValue(), new LinkedList<>());
				}
			}
		}

		// N szédszedés
		boolean fel = true;
		int border = selectedList.size() / (p.getSection().getSectionNumber() - 1);
		int db = 0;
		for (int i = 0; i < selectedList.size(); i++) {
			if (fel && db >= border) {
				fel = false;
				db = 0;
			} else if (!fel && db >= border) {
				fel = true;
				db = 0;
			}
			if (fel) {
				String section = p1Map.get(selectedList.get(i));
				LinkedList<Integer> tmpList = tmpMap.get(section);
				tmpList.add(selectedList.get(i));
				tmpMap.put(section, tmpList);

				section = p2Map.get(selectedList.get(i));
				tmpList = tmpMapInverz.get(section);
				tmpList.add(selectedList.get(i));
				tmpMapInverz.put(section, tmpList);
			}
			if (!fel) {
				String section = p2Map.get(selectedList.get(i));
				LinkedList<Integer> tmpList = tmpMap.get(section);
				tmpList.add(selectedList.get(i));
				tmpMap.put(section, tmpList);

				section = p1Map.get(selectedList.get(i));
				tmpList = tmpMapInverz.get(section);
				tmpList.add(selectedList.get(i));
				tmpMapInverz.put(section, tmpList);
			}
			db++;
		}

		// 2 felé szedés
		// int border = selectedList.size() / 2;
		// for (int i = 0; i < selectedList.size(); i++) {
		// if (i < border) {
		// String section = p1Map.get(selectedList.get(i));
		// LinkedList<Integer> tmpList = tmpMap.get(section);
		// tmpList.add(selectedList.get(i));
		// tmpMap.put(section, tmpList);
		//
		// section = p2Map.get(selectedList.get(i));
		// tmpList = tmpMapInverz.get(section);
		// tmpList.add(selectedList.get(i));
		// tmpMapInverz.put(section, tmpList);
		// }
		// if (i >= border) {
		// String section = p2Map.get(selectedList.get(i));
		// LinkedList<Integer> tmpList = tmpMap.get(section);
		// tmpList.add(selectedList.get(i));
		// tmpMap.put(section, tmpList);
		//
		// section = p1Map.get(selectedList.get(i));
		// tmpList = tmpMapInverz.get(section);
		// tmpList.add(selectedList.get(i));
		// tmpMapInverz.put(section, tmpList);
		// }
		// }
		mapList.add(tmpMap);
		mapList.add(tmpMapInverz);
		return mapList;
	}

	public boolean checkState(Map<String, LinkedList<Integer>> stateMap, LinkedList<PresentationOperator> operators,
			Section section) {
		boolean hasNullSection = false;
		LocalTime localtime = null;
		for (Map.Entry<String, LinkedList<Integer>> entry : stateMap.entrySet()) {
			LinkedList<Integer> idList = entry.getValue();
			if (idList.isEmpty()) {
				hasNullSection = true;
			}
			localtime = LocalTime.parse(section.getFrom(), formatter);
			PresentationOperator actualPresentation = null;
			for (int i = 0; i < idList.size(); i++) {
				for (PresentationOperator operator : operators) {
					if (idList.get(i).equals(operator.getId())) {
						actualPresentation = operator;
						break;
					}
				}
				String[] from = actualPresentation.getFrom().split(" ");
				String[] to = actualPresentation.getTo().split(" ");
				LocalTime fromLT = LocalTime.parse(from[3], formatter);
				fromLT = fromLT.plusNanos(1);
				LocalTime toLT = LocalTime.parse(to[3], formatter);
				if (fromLT.isAfter(localtime) || localtime.isBefore(toLT)) {
					String tmpInter = actualPresentation.getInter();
					String[] interArray = tmpInter.split("\\.");
					localtime = localtime.plusMinutes(Integer.parseInt(interArray[0]));
				} else {
					return false;
				}
			}
		}
		if (hasNullSection) {
			return false;
		}
		return true;
	}

	public Map<String, LinkedList<Integer>> GetMapTabla() {
		return childrenList.get(0).getParentState().getMapTabel();
	}

	public Map<String, LinkedList<Integer>> GetMapFullTabla() {
		return actualMap;
	}

	public Map<String, LinkedList<Integer>> getActualMap() {
		return actualMap;
	}

	public void setActualMap(Map<String, LinkedList<Integer>> actualMap) {
		this.actualMap = actualMap;
	}
}
