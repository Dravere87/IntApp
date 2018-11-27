package intapp.model;

import java.util.List;

public class Tab {
    private String title;
    private String content;
    private List<Show> shows;

    public Tab(String title, String content) {
        this.title = title;
        this.content = content;
    }
    
    public Tab(String title, List<Show> shows) {
        this.title = title;
        this.shows = shows;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

	public List<Show> getShows() {
		return shows;
	}
}
