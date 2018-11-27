package intapp.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import org.primefaces.model.DefaultStreamedContent;

import intapp.model.Show;
import intapp.servicesimp.PresentationServicesImp;
import intapp.sort.PresentationOperator;

@ManagedBean
@ViewScoped
public class FileUpload implements Serializable {

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	protected Map<String, LinkedList<Integer>> map;
	private static final long serialVersionUID = 1L;
	private String name;
	private Part file;
	
	@ManagedProperty(value = "#{presentationServicesImp}")
	PresentationServicesImp presentationService;
	
	private DefaultStreamedContent download;

	public void setDownload(DefaultStreamedContent download) {
	    this.download = download;
	}

	public DefaultStreamedContent getDownload() throws Exception {
	    return download;
	}

	public void eloDownload() throws Exception {
	    InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/Minta.xls");
	    setDownload(new DefaultStreamedContent(stream, "application/xls", "Minta.xls"));
	}

	public PresentationServicesImp getPresentationService() {
		return presentationService;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPresentationService(PresentationServicesImp presentationService) {
		this.presentationService = presentationService;
	}

	public Part getFile() {
		return file;
	}

	public void setFile(Part file) {
		this.file = file;
	}

	public String upload() {
		try {
			InputStream input = file.getInputStream();
			presentationService.readFile(input);
//			presentationService.testOptimalSearch();
			presentationService.generate();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "table";
	}
	
	public Map<String, Map<String, LinkedList<Integer>>> getTmpMap() {
		return presentationService.getTmpMap();
	}

	public List<Show> createShows() {
		List<Show> shows = new LinkedList<>();
		Map<String, LinkedList<Integer>> tmp = presentationService.getTmp();
		Map<String, LinkedList<Integer>> sortedTmp = new HashMap<>();
		Show show = null;
		List<PresentationOperator> presentationList = new LinkedList<>();
		for (Map.Entry<String, LinkedList<Integer>> entry : tmp.entrySet()) {
			if(entry.getKey().contains("DE")){
			show = new Show(entry.getKey());
			presentationList = new LinkedList<>();
			if (entry.getValue() != null) {
				for (Integer id : entry.getValue()) {
					PresentationOperator operator = null;
					for (PresentationOperator presentation : presentationService.getOperators()) {
						if (id.equals(presentation.getId())) {
							operator = presentation;
							presentationList.add(operator);
							break;
						}
					}
					switch (presentationList.size()) {
					case 1:
						show.setOperator1(operator);
						break;
					case 2:
						show.setOperator2(operator);
						break;
					case 3:
						show.setOperator3(operator);
						break;
					case 4:
						show.setOperator4(operator);
						break;
					case 5:
						show.setOperator5(operator);
						break;
					case 6:
						show.setOperator6(operator);
						break;
					case 7:
						show.setOperator7(operator);
						break;
					case 8:
						show.setOperator8(operator);
						break;
					case 9:
						show.setOperator9(operator);
						break;
					}
				}
			}
			show.setPresentatonList(presentationList);
			}
			else if(entry.getKey().contains("DU")){
				if(show == null){
					show = new Show(entry.getKey());
				}
				presentationList = new LinkedList<>();
				if (entry.getValue() != null) {
					for (Integer id : entry.getValue()) {
						PresentationOperator operator = null;
						for (PresentationOperator presentation : presentationService.getOperators()) {
							if (id.equals(presentation.getId())) {
								operator = presentation;
								presentationList.add(operator);
								break;
							}
						}
						switch (presentationList.size()) {
						case 1:
							show.setOperator1(operator);
							break;
						case 2:
							show.setOperator2(operator);
							break;
						case 3:
							show.setOperator3(operator);
							break;
						case 4:
							show.setOperator4(operator);
							break;
						case 5:
							show.setOperator5(operator);
							break;
						case 6:
							show.setOperator6(operator);
							break;
						case 7:
							show.setOperator7(operator);
							break;
						case 8:
							show.setOperator8(operator);
							break;
						case 9:
							show.setOperator9(operator);
							break;
						}
					}
				}
				show.setPresentatonList(presentationList);
				shows.add(show);
				}
		}
		return shows;
	}
	
	public List<Show> createShowsWithParam(Map<String, LinkedList<Integer>> tmp, String time) {
		LocalTime localtime = null;
		List<Show> shows = new LinkedList<>();
		Map<String, LinkedList<Integer>> sortedTmp = new HashMap<>();
		Show show = null;
		boolean first = false;
		List<PresentationOperator> presentationList = new LinkedList<>();
		for (Map.Entry<String, LinkedList<Integer>> entry : tmp.entrySet()) {
			if(entry.getKey().contains("DE")){
			show = new Show(entry.getKey());
			presentationList = new LinkedList<>();
			if (entry.getValue() != null) {
				localtime = LocalTime.parse(time, formatter);
				for (Integer id : entry.getValue()) {
					PresentationOperator operator = null;
					for (PresentationOperator presentation : presentationService.getOperators()) {
						if (id.equals(presentation.getId()) && !first) {
							operator = presentation;
							operator.setFrom(localtime.toString());
							presentationList.add(operator);
							first = true;
							break;
						}
						else if(id.equals(presentation.getId()) && first) {
								operator = presentation;
								//Ido osszeadas
								String tmpInter = operator.getInter();
								String[] interArray = tmpInter.split("\\.");
								localtime = localtime.plusMinutes(Integer.parseInt(interArray[0]));
								operator.setFrom(localtime.toString());
								presentationList.add(operator);
								break;
						}
					}
					switch (presentationList.size()) {
					case 1:
						show.setOperator1(operator);
						break;
					case 2:
						show.setOperator2(operator);
						break;
					case 3:
						show.setOperator3(operator);
						break;
					case 4:
						show.setOperator4(operator);
						break;
					case 5:
						show.setOperator5(operator);
						break;
					case 6:
						show.setOperator6(operator);
						break;
					case 7:
						show.setOperator7(operator);
						break;
					case 8:
						show.setOperator8(operator);
						break;
					case 9:
						show.setOperator9(operator);
						break;
					}
				}
			}
//			show.setPresentatonList(presentationList);
//			shows.add(show);
			}
			else if(entry.getKey().contains("DU")){
				first = false;
				if(show == null){
					show = new Show(entry.getKey());
				}
				localtime = LocalTime.parse("12:30", formatter);
				if (entry.getValue() != null) {
					for (Integer id : entry.getValue() ) {
						PresentationOperator operator = null;
						for (PresentationOperator presentation : presentationService.getOperators()) {
							if (id.equals(presentation.getId()) && !first ) {
								operator = presentation;
								presentationList.add(operator);
								operator.setFrom("12:30");
								first = true;
								break;
							}
							else if (id.equals(presentation.getId()) && first) {
								operator = presentation;
								String tmpInter = operator.getInter();
								String[] interArray = tmpInter.split("\\.");
								localtime = localtime.plusMinutes(Integer.parseInt(interArray[0]));
								operator.setFrom(localtime.toString());
								presentationList.add(operator);
								break;
							}
						}
						switch (presentationList.size()) {
						case 1:
							show.setOperator1(operator);
							break;
						case 2:
							show.setOperator2(operator);
							break;
						case 3:
							show.setOperator3(operator);
							break;
						case 4:
							show.setOperator4(operator);
							break;
						case 5:
							show.setOperator5(operator);
							break;
						case 6:
							show.setOperator6(operator);
							break;
						case 7:
							show.setOperator7(operator);
							break;
						case 8:
							show.setOperator8(operator);
							break;
						case 9:
							show.setOperator9(operator);
							break;
						}
					}
				}
				show.setPresentatonList(presentationList);
				shows.add(show);
				}
		}
		return shows;
	}
	
	private boolean showPopup;
	
	public void show(){
        showPopup=true;
    }
    public void hide(){
        showPopup=false;
    }

    public boolean isShowPopup() {
        return showPopup;
    }

    public void setShowPopup(boolean showPopup) {
        this.showPopup = showPopup;
}
}
