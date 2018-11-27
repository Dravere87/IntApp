package intapp.dao;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import intapp.model.Section;
import intapp.model.Tab;
import intapp.sort.PresentationOperator;

public interface PresentationsDao {
	
	LinkedList<PresentationOperator> readPresentations();
	
	LinkedList<PresentationOperator> getPresentations(InputStream input);

	Section getSection(InputStream input);

	void writePresentationsXls(List<Tab> tabs);

	void writePresentationsXlsx(List<Tab> tabs);
	
}
