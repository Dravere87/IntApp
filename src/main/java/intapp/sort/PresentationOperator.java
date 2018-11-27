package intapp.sort;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import intapp.model.Section;

public class PresentationOperator {
	
	DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
	final String FORMAT = "mm:ss";

	private String insertPosition;
	private int id;
	private String timeTo;
	private String timeFrom;
	private String presentationTitle;
	private String actor;
	private String topic;
	private String from;
	private String inter;
	private String to;
	private boolean piority;
	private int weight;
	private LinkedList<String> insertPos;

	public PresentationOperator( int id, String presentationTitle,
			String actor, String topic, String inter, String timeTo, String to) {
		super();
		this.id = id;
		this.timeTo = timeTo;
		this.presentationTitle = presentationTitle;
		this.actor = actor;
		this.topic = topic;
		this.inter = inter;
		this.to= to;
	}

	public PresentationOperator(int id, String presentationTitle, String actor, String topic, String from, String inter, String timeFrom, String timeTo) {
		super();
		this.id = id;
		this.presentationTitle = presentationTitle;
		this.actor = actor;
		this.topic = topic;
		this.from = from;
		this.inter = inter;
		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
	}

	public PresentationOperator() {
		super();
	}

	public boolean isApplicableMap(PresentationState s, PresentationOperator o, LinkedList<PresentationOperator> operators, Section section){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime localtime = null;
		LocalTime localtimeEnd = null;
		LocalTime localtimeDel = null;
		LocalTime localtime13 = null;
		int steep = -1;
		insertPos = new LinkedList<>();
		Map<String, LinkedList<Integer>> presentationMap = new HashMap<>();
		presentationMap = s.getMapTabel();
		boolean canInsert = true;
		if(presentationMap.isEmpty()){
			return true;
		}
		Set<String> keys = presentationMap.keySet();
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(keys.size());
		insertPosition = "";
		for (Map.Entry<String, LinkedList<Integer>> entry : presentationMap.entrySet()) {
			steep++;
			LinkedList<Integer> idList = entry.getValue();
			for (int i = 0; i < idList.size(); i++) {
				if(idList.get(i) == o.getId()){
					return false;
				}
			}
			if(idList.size()!=0){
				localtime = LocalTime.parse(section.getFrom(), formatter);
				localtimeEnd = LocalTime.parse(section.getTo(), formatter);
				localtimeDel = LocalTime.parse("12:00", formatter);
				localtime13 = LocalTime.parse("13:00", formatter);
				for (Integer integer : idList) {
					for (PresentationOperator presentationOperator : operators) {
						if (integer == presentationOperator.getId()) {
							if (presentationOperator.getTimeTo().contains("DE")) {
								if ("DU".equals(presentationOperator.getTimeTo())) {
									canInsert = false;
								}
								String tmpInter = presentationOperator.getInter();
								String[] interArray = tmpInter.split("\\.");
								localtime = localtime.plusMinutes(Integer.parseInt(interArray[0]));
								if (localtime.isAfter(localtimeDel)) {
									canInsert = false;
								}
							} else if (presentationOperator.getTimeTo().contains("DU")) {
								if ("DE".equals(presentationOperator.getTimeTo())) {
									canInsert = false;
								}
								String tmpInter = presentationOperator.getInter();
								String[] interArray = tmpInter.split("\\.");
								localtime13 = localtime13.plusMinutes(Integer.parseInt(interArray[0]));
								if (localtime13.isAfter(localtimeEnd)) {
									canInsert = false;
								}
							}
						}
						if(o.getTimeTo().contains("DE") && steep == randomInt && insertPosition.isEmpty()){
							String tmpInter = o.getInter();
							String[] interArray = tmpInter.split("\\.");
							localtime = localtime.plusMinutes(Integer.parseInt(interArray[0]));
							if (localtime.isAfter(localtimeDel)) {
								canInsert = false;
							}
							insertPosition = entry.getKey();
						}else if(o.getTimeTo().contains("DU") && steep == randomInt && insertPosition.isEmpty()) {
							String tmpInter = o.getInter();
							String[] interArray = tmpInter.split("\\.");
							localtime13 = localtime13.plusMinutes(Integer.parseInt(interArray[0]));
							if (localtime13.isAfter(localtimeEnd)) {
								canInsert = false;
							}
							insertPosition = entry.getKey();
						}else if(steep == randomInt && insertPosition.isEmpty()){
							String tmpInter = o.getInter();
							String[] interArray = tmpInter.split("\\.");
							localtime13 = localtime13.plusMinutes(Integer.parseInt(interArray[0]));
							if (localtime13.isAfter(localtimeEnd)) {
								canInsert = false;
							}
							insertPosition = entry.getKey();
						}
					}
				}
				if(!canInsert){
					return false;
				}
			}
			else if(steep == randomInt){
				insertPosition = entry.getKey();
			}
		}
		if(!canInsert){
			return false;
		}
		return true;
	}

	public boolean isApplicable(PresentationState s, PresentationOperator o, LinkedList<PresentationOperator> operators) {
		DateFormat formatter = new SimpleDateFormat(FORMAT);
		Date toDateOperator = null;
		Date toDatePresentation = null;
		Date fromDateOperator = null;
		Date fromDatePresentation = null;
		PresentationState state = (PresentationState) s;
		PresentationOperator operator = (PresentationOperator) o;
		int tabla[][] = state.getTable();
		for (int i = 0; i < tabla.length; i++) {
			for (int j = 0; j < tabla[i].length; j++) {
				if (tabla[i][j] != 0 && tabla[i][j] == operator.getId()) {
					return false;
				}
			}
		}
		for (int i = 0; i < tabla.length; i++) {
			for (int j = 0; j < tabla[i].length; j++) {
				if (tabla[i][j] != 0) {
					PresentationOperator presentation = null;
					for (PresentationOperator ope : operators) {
						if (tabla[i][j] == ope.getId()) {
							presentation = ope;
							break;
						}
					}
					try {
						fromDateOperator = formatter.parse(operator.getFrom());
						fromDatePresentation = formatter.parse(presentation.getFrom());
						toDateOperator = formatter.parse(operator.getTo());
						toDatePresentation = formatter.parse(presentation.getTo());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if (fromDateOperator.getTime() == fromDatePresentation.getTime()
							&& toDateOperator.getTime() <= toDatePresentation.getTime()) {
						return false;
					}
					if (fromDateOperator.getTime() >= fromDatePresentation.getTime()
							&& toDateOperator.getTime() == toDatePresentation.getTime()) {
						return false;
					}
					if (fromDateOperator.getTime() <= fromDatePresentation.getTime()
							&& toDateOperator.getTime() >= toDatePresentation.getTime()) {
						return false;
					}
					if (fromDateOperator.getTime() <= fromDatePresentation.getTime()
							&& toDateOperator.getTime() > fromDatePresentation.getTime()) {
						return false;
					}
					if (toDateOperator.getTime() >= toDatePresentation.getTime()
							&& fromDateOperator.getTime() < toDatePresentation.getTime()) {
						return false;
					}
					if (toDateOperator.getTime() <= fromDatePresentation.getTime()) {
						return true;
					}
					if (fromDateOperator.getTime() >= toDatePresentation.getTime()) {
						return true;
					}
				}
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "PresentationOperator [presentationTitle=" + presentationTitle + ", actor=" + actor + ", topic=" + topic
				+ ", from=" + from + ", to=" + to + "]";
	}

	public String getPresentationTitle() {
		return presentationTitle;
	}

	public void setPresentationTitle(String presentationTitle) {
		this.presentationTitle = presentationTitle;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public boolean isPiority() {
		return piority;
	}

	public void setPiority(boolean piority) {
		this.piority = piority;
	}

	public PresentationState apply(PresentationState state, PresentationOperator operator) {
		int tabla[][] = state.getTable();
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(tabla.length);
		boolean insert = false;
		int newTabla[][] = new int[tabla.length][tabla[0].length];
		for (int i = 0; i < tabla.length; i++) {
			for (int j = 0; j < tabla[i].length; j++) {
				if (tabla[i][j] != 0) {
					newTabla[i][j] = tabla[i][j];
				}
				
			}
		}
		for (int i = 0; i < newTabla[randomInt].length; i++) {
			if (newTabla[randomInt][i] == 0) {
				newTabla[randomInt][i] = operator.getId();
				insert = true;
				break;
			}
		}
		if (insert) {
			PresentationState newState = new PresentationState();
			newState.setTable(newTabla);
			return newState;
		}
		else{
			return null;
		}
	}
	
	public PresentationState applyMap(PresentationState state, PresentationOperator operator, Section section, LinkedList<PresentationOperator> operators) {
		
		Map<String, LinkedList<Integer>> tempMap = state.getMapTabel();
		Map<String, LinkedList<Integer>> actualMap = new HashMap<>();
		String insertKey = null;
		for (Map.Entry<String, LinkedList<Integer>> entry : tempMap.entrySet()) {
			actualMap.put(new String(entry.getKey()), new LinkedList<Integer>(entry.getValue()));
		}
		LinkedList<Integer> inserList = actualMap.get(insertPosition);
		if(operator.getTimeTo().contains("DU")){
			inserList.addFirst(operator.getId());
		}else if(operator.getTimeTo().contains("DE")){
			inserList.addLast(operator.getId());
		}else{
			inserList.add(operator.getId());
		}
		actualMap.put(insertPosition, inserList);
		PresentationState newState = new PresentationState();
		newState.setMapTabel(actualMap);
		insertPosition = null;
		return newState;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getInter() {
		return inter;
	}

	public void setInter(String inter) {
		this.inter = inter;
	}

	public String getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(String timeTo) {
		this.timeTo = timeTo;
	}

	public PresentationState apply(PresentationState state, PresentationOperator op, LinkedList<PresentationOperator> operators) {
		LinkedList<String> typeList = new LinkedList<>();
		int tabla[][] = state.getTable();
		boolean insert = false;
		int newTabla[][] = new int[tabla.length][tabla[0].length];
		for (int i = 0; i < tabla.length; i++) {
			for (int j = 0; j < tabla[i].length; j++) {
				if (tabla[i][j] != 0) {
					newTabla[i][j] = tabla[i][j];
				}
			}
		}
		PresentationOperator presentation = null;
		int typeCount = Integer.MAX_VALUE;
		int index = 0;
		for (int i = 0; i < tabla.length; i++) {
			typeList = new LinkedList<>();
			typeList.add(op.getTopic());
			for (int j = 0; j < tabla[i].length; j++) {
				if (tabla[i][j] != 0) {
					for (PresentationOperator operator : operators) {
						if(operator.getId() == tabla[i][j]){
							presentation  = operator;
						}
					}
					if(!typeList.contains(presentation.getTopic())){
						typeList.add(presentation.getTopic());
					}
				}
			}
			if(typeList.size() < typeCount){
				typeCount = typeList.size()-1;
				index = i;
			}
		}
		for (int i = 0; i < newTabla[index].length; i++) {
			if (newTabla[index][i] == 0) {
				newTabla[index][i] = op.getId();
				insert = true;
				break;
			}
		}
		if (insert) {
			PresentationState newState = new PresentationState();
			newState.setTable(newTabla);
			return newState;
		}
		else{
			return null;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actor == null) ? 0 : actor.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + id;
		result = prime * result + ((inter == null) ? 0 : inter.hashCode());
		result = prime * result + (piority ? 1231 : 1237);
		result = prime * result + ((presentationTitle == null) ? 0 : presentationTitle.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		result = prime * result + ((topic == null) ? 0 : topic.hashCode());
		result = prime * result + weight;
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
		PresentationOperator other = (PresentationOperator) obj;
		if (actor == null) {
			if (other.actor != null)
				return false;
		} else if (!actor.equals(other.actor))
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (id != other.id)
			return false;
		if (inter == null) {
			if (other.inter != null)
				return false;
		} else if (!inter.equals(other.inter))
			return false;
		if (piority != other.piority)
			return false;
		if (presentationTitle == null) {
			if (other.presentationTitle != null)
				return false;
		} else if (!presentationTitle.equals(other.presentationTitle))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		if (topic == null) {
			if (other.topic != null)
				return false;
		} else if (!topic.equals(other.topic))
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}
}
