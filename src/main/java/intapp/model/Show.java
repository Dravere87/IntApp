package intapp.model;

import java.io.Serializable;
import java.util.List;

import intapp.sort.PresentationOperator;

public class Show implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String sectionTitle;
	private List<PresentationOperator> presentatonList;
	private PresentationOperator operator1;
	private PresentationOperator operator2;
	private PresentationOperator operator3;
	private PresentationOperator operator4;
	private PresentationOperator operator5;
	private PresentationOperator operator6;
	private PresentationOperator operator7;
	private PresentationOperator operator8;
	private PresentationOperator operator9;

	public Show(String sectionTitle) {
		super();
		this.sectionTitle = sectionTitle.substring(0, sectionTitle.length() - 2);
	}

	public Show(String sectionTitle, List<PresentationOperator> presentatonList) {
		super();
		this.sectionTitle = sectionTitle;
		this.presentatonList = presentatonList;
	}

	public String getSectionTitle() {
		return sectionTitle;
	}

	public void setSectionTitle(String sectionTitle) {
		this.sectionTitle = sectionTitle;
	}

	public List<PresentationOperator> getPresentatonList() {
		return presentatonList;
	}

	public void setPresentatonList(List<PresentationOperator> presentatonList) {
		this.presentatonList = presentatonList;
	}

	public PresentationOperator getOperator1() {
		return operator1;
	}

	public void setOperator1(PresentationOperator operator1) {
		this.operator1 = operator1;
	}

	public PresentationOperator getOperator2() {
		return operator2;
	}

	public void setOperator2(PresentationOperator operator2) {
		this.operator2 = operator2;
	}

	public PresentationOperator getOperator3() {
		return operator3;
	}

	public void setOperator3(PresentationOperator operator3) {
		this.operator3 = operator3;
	}

	public PresentationOperator getOperator4() {
		return operator4;
	}

	public void setOperator4(PresentationOperator operator4) {
		this.operator4 = operator4;
	}

	public PresentationOperator getOperator5() {
		return operator5;
	}

	public void setOperator5(PresentationOperator operator5) {
		this.operator5 = operator5;
	}

	public PresentationOperator getOperator6() {
		return operator6;
	}

	public void setOperator6(PresentationOperator operator6) {
		this.operator6 = operator6;
	}

	public PresentationOperator getOperator7() {
		return operator7;
	}

	public void setOperator7(PresentationOperator operator7) {
		this.operator7 = operator7;
	}

	public PresentationOperator getOperator8() {
		return operator8;
	}

	public void setOperator8(PresentationOperator operator8) {
		this.operator8 = operator8;
	}

	public PresentationOperator getOperator9() {
		return operator9;
	}

	public void setOperator9(PresentationOperator operator9) {
		this.operator9 = operator9;
	}

	@Override
	public String toString() {
		return "Show [sectionTitle=" + sectionTitle + ", presentatonList=" + presentatonList + ", operator1="
				+ operator1 + ", operator2=" + operator2 + ", operator3=" + operator3 + ", operator4=" + operator4
				+ ", operator5=" + operator5 + ", operator6=" + operator6 + ", operator7=" + operator7 + ", operator8="
				+ operator8 + ", operator9=" + operator9 + "]";
	}
}
