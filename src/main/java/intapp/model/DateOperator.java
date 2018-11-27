package intapp.model;

import java.time.LocalTime;

public class DateOperator {
	
	int id;
	LocalTime from;
	LocalTime to;
	
	public DateOperator(int id, LocalTime from, LocalTime to) {
		super();
		this.id = id;
		this.from = from;
		this.to = to;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LocalTime getFrom() {
		return from;
	}
	public void setFrom(LocalTime from) {
		this.from = from;
	}
	public LocalTime getTo() {
		return to;
	}
	public void setTo(LocalTime to) {
		this.to = to;
	}
}
