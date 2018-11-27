package intapp.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;


import intapp.model.Show;
import intapp.model.Tab;
import intapp.servicesimp.PresentationServicesImp;
import intapp.sort.PresentationOperator;

@ManagedBean
@ViewScoped
public class TableViewController implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<Show> shows;
	private List<Tab> tabs;
	private Map<String, Map<String, LinkedList<Integer>>> tmp;
	private Map<String, Map<String, LinkedList<Integer>>> treeMap;
	
	private PresentationOperator selectedPresetationOperator;
	
	@ManagedProperty("#{fileUpload}")
	private FileUpload service;
	
	public Map<String, Map<String, LinkedList<Integer>>> getTreeMap() {
		return treeMap;
	}

	public void setTreeMap(Map<String, Map<String, LinkedList<Integer>>> treeMap) {
		this.treeMap = treeMap;
	}

	@ManagedProperty(value = "#{presentationServicesImp}")
	PresentationServicesImp presentationService;

	public PresentationServicesImp getPresentationService() {
		return presentationService;
	}

	public void setPresentationService(PresentationServicesImp presentationService) {
		this.presentationService = presentationService;
	}

	@PostConstruct
	public void init() {
		tmp = service.getTmpMap();
		tabs= new ArrayList<Tab>();
		treeMap = new TreeMap<String, Map<String, LinkedList<Integer>>>(tmp);
		for(Entry<String, Map<String, LinkedList<Integer>>> days : treeMap.entrySet()){
			shows = service.createShowsWithParam(days.getValue(), service.getPresentationService().getSection().getFrom());
//			shows = fileUploadService.createShowsWithParam(days.getValue());
			String tmp = days.getKey().replace("/", ".");
			String[] tmpDate = tmp.split("\\.");
			tabs.add(new Tab("20"+tmpDate[2]+"."+tmpDate[0]+"."+tmpDate[1], shows));
		}
	}

	public void submit() {
        String result = "Submitted values: " + selectedPresetationOperator.getActor() + ", " + selectedPresetationOperator.getPresentationTitle();
        System.out.println(result);
    }
	
	public void addPresentation(Show show) {
		System.out.println(show.getSectionTitle());
	}
	
	public void deletePresentation(PresentationOperator operator) {
		List<PresentationOperator>  tmpList = new LinkedList<>();
		boolean delete = false; 
		for (Show show : shows) {
			for (PresentationOperator preOperator : show.getPresentatonList()) {
				tmpList = show.getPresentatonList();
				if(preOperator.getId() == operator.getId()){
					tmpList.remove(preOperator);
					show.setOperator1(null);
					show.setOperator2(null);
					show.setOperator3(null);
					show.setOperator4(null);
					show.setOperator5(null);
					show.setOperator6(null);
					show.setOperator7(null);
					show.setOperator8(null);
					show.setOperator9(null);
					delete = true;
					int i = 1;
					for (PresentationOperator newOperator : tmpList) {
						switch (i) {
						case 1:
							show.setOperator1(newOperator);
							break;
						case 2:
							show.setOperator2(newOperator);
							break;
						case 3:
							show.setOperator3(newOperator);
							break;
						case 4:
							show.setOperator4(newOperator);
							break;
						case 5:
							show.setOperator5(newOperator);
							break;
						case 6:
							show.setOperator6(newOperator);
							break;
						case 7:
							show.setOperator7(newOperator);
							break;
						case 8:
							show.setOperator8(newOperator);
							break;
						case 9:
							show.setOperator9(newOperator);
							break;
						}
						i++;
					}
					break;
				}
			}
			if(delete){
				show.setPresentatonList(tmpList);
				break;
			}
		}
	}
	
	public void doAction(PresentationOperator operator){
		System.out.println(operator.getPresentationTitle());
		setSelectedPresetationOperator(operator);
    }
	
	public FileUpload getService() {
		return service;
	}

	public void setService(FileUpload service) {
		this.service = service;
	}

	public List<Show> getShows() {
		return shows;
	}

	public void setShows(List<Show> shows) {
		this.shows = shows;
	}
	
	public boolean hasColumn(int column){
		for (Show show : shows){
			if(show.getPresentatonList().size()>=column){
				return true;
			}
		}
		return false;
	}
	
	public List<Tab> getTabs() {
		return tabs;
	}

	public void setTabs(List<Tab> tabs) {
		this.tabs = tabs;
	}

	public PresentationOperator getSelectedPresetationOperator() {
		return selectedPresetationOperator;
	}

	public void setSelectedPresetationOperator(PresentationOperator selectedPresetationOperator) {
		this.selectedPresetationOperator = selectedPresetationOperator;
	}
	
	public void writeXls(){
		presentationService.writeXls(tabs);
	}
	
	public void writeXlsx(){
		presentationService.writeXlsx(tabs);
	}
}
