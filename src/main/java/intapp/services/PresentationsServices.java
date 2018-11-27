package intapp.services;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import intapp.model.Tab;

public interface PresentationsServices{
	
	void generate();
	
	void readFile(InputStream value);
	
	void writeXlsx(List<Tab> tabs);

	void writeXls(List<Tab> tabs);

}
