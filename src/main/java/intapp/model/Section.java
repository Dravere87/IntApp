package intapp.model;

import java.util.LinkedList;

public class Section {
	
	private int sectionNumber;
	private String from;
	private String to;
	private LinkedList<String> sections;
	private LinkedList<String> days;
	
	public Section(int sectionNumber, String from, String to) {
		super();
		this.sectionNumber = sectionNumber;
		this.from = from;
		this.to = to;
	}

	public Section(int sectionNumber, String from, String to, LinkedList<String> sections) {
		super();
		this.sectionNumber = sectionNumber;
		this.from = from;
		this.to = to;
		this.sections = sections;
	}

	public Section(int sectionNumber, String from, String to, LinkedList<String> sections, LinkedList<String> days) {
		super();
		this.sectionNumber = sectionNumber;
		this.from = from;
		this.to = to;
		this.sections = sections;
		this.days = days;
	}

	public LinkedList<String> getDays() {
		return days;
	}

	public void setDays(LinkedList<String> days) {
		this.days = days;
	}

	public int getSectionNumber() {
		return sectionNumber;
	}
	
	public void setSectionNumber(int sectionNumber) {
		this.sectionNumber = sectionNumber;
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

	public LinkedList<String> getSections() {
		return sections;
	}

	public void setSections(LinkedList<String> sections) {
		this.sections = sections;
	}
}
