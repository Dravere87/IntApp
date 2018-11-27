package intapp.servicesimp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import intapp.model.Show;
import intapp.services.FileUpload;
import intapp.sort.PresentationOperator;

@ManagedBean
@SessionScoped
public class FileUploadServices implements FileUpload, Serializable{

	private static final long serialVersionUID = 1L;
	
	public FileUploadServices(PresentationServicesImp presentationService) {
		super();
		this.presentationService = presentationService;
	}

	@ManagedProperty(value = "#{presentationServicesImp}")
	PresentationServicesImp presentationService;

	public PresentationServicesImp getPresentationService() {
		return presentationService;
	}

	public void setPresentationService(PresentationServicesImp presentationService) {
		this.presentationService = presentationService;
	}
	
	public Map<String, Map<String, LinkedList<Integer>>> getTmpMap() {
		return presentationService.getTmpMap();
	}

	@Override
	public List<Show> createShowsWithParam(Map<String, LinkedList<Integer>> tmp) {
		List<Show> shows = new LinkedList<>();
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
}
