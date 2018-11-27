package intapp.servicesimp;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import intapp.algorythm.NewGenericAlgorythm;
import intapp.daoimp.PresentationsDaoImp;
import intapp.model.Section;
import intapp.model.Show;
import intapp.model.Tab;
import intapp.services.PresentationsServices;
import intapp.sort.Optimal;
import intapp.sort.PresentationOperator;
import intapp.sort.PresentationProblem;

@ManagedBean
@SessionScoped
public class PresentationServicesImp implements PresentationsServices, Serializable{

	private static final long serialVersionUID = 1L;
	private LinkedList<PresentationOperator> operators = new LinkedList<>();
	private PresentationProblem problem = new PresentationProblem();
	private Section section;
	private Map<String, Map<String, LinkedList<Integer>>> tmpDays;
	private Map<String, LinkedList<Integer>> tmp;
	private Map<String, Map<String, LinkedList<Integer>>> tmpMap;
	
	@ManagedProperty("#{presentationsDaoImp}")
	private PresentationsDaoImp preresentaionDaoImp;

	public PresentationsDaoImp getPreresentaionDaoImp() {
		return preresentaionDaoImp;
	}

	public void setPreresentaionDaoImp(PresentationsDaoImp preresentaionDaoImp) {
		this.preresentaionDaoImp = preresentaionDaoImp;
	}

	@Override
	public void generate() {
		PresentationProblem.setOperators(operators);
		problem.setSection(section);
		NewGenericAlgorythm algorithm = new NewGenericAlgorythm(problem);
		boolean run = algorithm.run();
		if(run){
//			tmp = algorithm.GetMapFullTabla();
			tmpMap = algorithm.GetFullTabla();
		}
	}

	@Override
	public void readFile(InputStream value) {
		operators = preresentaionDaoImp.getPresentations(value);
		section = preresentaionDaoImp.getSection(value);
	}
	
	public void testOptimalSearch(){
		PresentationProblem p = new PresentationProblem();
		p.setOperators(operators);
		p.setSection(section);
		Optimal algorithm = new Optimal(p);
		long startTime = System.currentTimeMillis();
		System.out.println("Az eloadasok beosztasa elkezdodott!");
		boolean run = algorithm.run();
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Az eloadasok beosztva: "+totalTime);
		if(run){
			tmpMap = algorithm.GetFullTabla();
		}
	}

	public Map<String, LinkedList<Integer>> getTmp() {
		return tmp;
	}	

	public Map<String, Map<String, LinkedList<Integer>>> getTmpMap() {
		return tmpMap;
	}

	public void setTmpMap(Map<String, Map<String, LinkedList<Integer>>> tmpMap) {
		this.tmpMap = tmpMap;
	}

	public LinkedList<PresentationOperator> getOperators() {
		return operators;
	}

	public Section getSection() {
		return section;
	}

	@Override
	public void writeXlsx(List<Tab> tabs) {
		preresentaionDaoImp.writePresentationsXlsx(tabs);
	}

	@Override
	public void writeXls(List<Tab> tabs) {
		preresentaionDaoImp.writePresentationsXls(tabs);
	}
}
