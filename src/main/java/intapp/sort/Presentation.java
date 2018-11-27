package intapp.sort;

public class Presentation {
	private int id;
	private String presentationTitle;
	private String actor;
	private String topic;
	private String from;
	private String to;
	private boolean poirity;
	private int weight;
	private String inter;
	private String fromTime;
	private String toTime;
	
	public Presentation(){
	}

	public Presentation(int id ,String presentationTitle, String actor, String topic, String from, String to, boolean piority, int weight, String inter) {
		super();
		this.id = id;
		this.presentationTitle = presentationTitle;
		this.actor = actor;
		this.topic = topic;
		this.from = from;
		this.to = to;
		this.poirity = piority;
		this.weight = weight;
		this.inter = inter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actor == null) ? 0 : actor.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + (poirity ? 1231 : 1237);
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
		Presentation other = (Presentation) obj;
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
		if (poirity != other.poirity)
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

	public boolean isPoirity() {
		return poirity;
	}

	public void setPoirity(boolean poirity) {
		this.poirity = poirity;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getInter() {
		return inter;
	}

	public void setInter(String inter) {
		this.inter = inter;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	@Override
	public String toString() {
		return "Presentation [presentationTitle=" + presentationTitle + ", actor=" + actor + ", topic=" + topic
				+ ", from=" + from + ", to=" + to + "]";
	}
}
