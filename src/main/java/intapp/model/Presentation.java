package intapp.model;

import java.util.Date;
import java.util.List;

public class Presentation {
      
	private String performer;
	private String presentaionName;
	private List<Date> dates;
	
	public Presentation(String performer, String presentaionName, List<Date> dates) {
		super();
		this.performer = performer;
		this.presentaionName = presentaionName;
		this.dates = dates;
	}

	public String getPerformer() {
		return performer;
	}

	public void setPerformer(String performer) {
		this.performer = performer;
	}

	public String getPresentaionName() {
		return presentaionName;
	}

	public void setPresentaionName(String presentaionName) {
		this.presentaionName = presentaionName;
	}

	public List<Date> getDates() {
		return dates;
	}

	public void setDates(List<Date> dates) {
		this.dates = dates;
	}	
}
