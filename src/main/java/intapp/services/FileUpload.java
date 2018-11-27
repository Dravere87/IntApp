package intapp.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import intapp.model.Show;

public interface FileUpload {
	
	List<Show> createShowsWithParam(Map<String, LinkedList<Integer>> tmp);
}
