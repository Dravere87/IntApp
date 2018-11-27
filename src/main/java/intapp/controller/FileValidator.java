package intapp.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import intapp.model.Section;
import intapp.sort.PresentationOperator;

@FacesValidator(value = "fileValidator")
public class FileValidator implements Validator {

	HSSFWorkbook workbook = null;

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		Part file = (Part) value;
		FacesMessage message = null;

		try {
			if (file == null || file.getSize() <= 0 || file.getContentType().isEmpty())
				message = new FacesMessage("Jelöljön ki egy fájlt");
			else if (file != null) {
				String header = FilenameUtils.getName(getSubmittedFileName(file));
				if (!header.contains("xls"))
					message = new FacesMessage("A kijelölt fájl nem xls vagy xlsx kiterjesztésű fájl");
				if(header.contains("xls")){
					try{
					InputStream input = file.getInputStream();
					getPresentations(input);
					getSection(input);
					}
					catch (Exception e) {
						message = new FacesMessage("Az Excel fájl formailag nem megfelelő, kérem nézze meg a minta fájlt.");
					}
				}
			}
			if (message != null && !message.getDetail().isEmpty()) {
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(message);
			}
		} catch (Exception ex) {
			throw new ValidatorException(new FacesMessage(ex.getMessage()));
		}
	}

	public static String getSubmittedFileName(Part filePart) {
		String header = filePart.getHeader("content-disposition");
		if (header == null)
			return null;
		for (String headerPart : header.split(";")) {
			if (headerPart.trim().startsWith("filename")) {
				return headerPart.substring(headerPart.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}
	
	public LinkedList<PresentationOperator> getPresentations(InputStream input) {
		LinkedList<PresentationOperator> operators = new LinkedList<>();
		try {
			workbook = new HSSFWorkbook(input);
			HSSFSheet sheet = workbook.getSheetAt(1);
			int i = 1;
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (row.getRowNum() > 0) {
					String presentationTitle = null;
					String actor = null;
					String topic = null;
					String from = null;
					String shortFrom = null;
					String intervallum = null;
					String to = null;
					Iterator<Cell> cellIterator = row.cellIterator();
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						switch (cell.getColumnIndex()) {
						case 0:
							presentationTitle = cell.getStringCellValue();
							break;
						case 1:
							actor = cell.getStringCellValue();
							break;
						case 2:
							topic = cell.getStringCellValue();
							break;
						case 3:
							intervallum = new DataFormatter().formatCellValue(cell);
							break;
						case 4:
							from = new DataFormatter().formatCellValue(cell);
							break;
						case 5:
							to = new DataFormatter().formatCellValue(cell);
							break;
						}
					}
					if ("Délután".equals(from)) {
						shortFrom = "DU";
					} else if ("Délelőtt".equals(from)) {
						shortFrom = "DE";
					} else {
						shortFrom = "BAR";
					}
					operators.add(
							new PresentationOperator(i, presentationTitle, actor, topic, intervallum, shortFrom, to));
					i++;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return operators;
	}
	
	public Section getSection(InputStream input) {
		LinkedList<String> sections = new LinkedList<>();
		Section section = null;
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		int sectionNumber = 0;
		String from = null;
		String to = null;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (row.getRowNum() == 1) {
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getColumnIndex()) {
					case 0:
						sectionNumber = (int) cell.getNumericCellValue();
						break;
					case 1:
						from = new DataFormatter().formatCellValue(cell);
						break;
					case 2:
						to = new DataFormatter().formatCellValue(cell);
						break;
					}
				}
			} else if (row.getRowNum() > 1) {
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if (cell.getStringCellValue() != null && !"".equals(cell.getStringCellValue())) {
						sections.add(cell.getStringCellValue());
					}
				}
			}

		}
		if (from.length() != 5) {
			from = "0" + from;
		}
		if (to.length() != 5) {
			to = "0" + to;
		}
		section = new Section(sectionNumber, from, to, sections);
		return section;
	}
}
