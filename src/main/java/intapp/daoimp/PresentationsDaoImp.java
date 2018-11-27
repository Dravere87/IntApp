package intapp.daoimp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.internal.ContentType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import intapp.dao.PresentationsDao;
import intapp.model.Section;
import intapp.model.Show;
import intapp.model.Tab;
import intapp.sort.Presentation;
import intapp.sort.PresentationOperator;

@ManagedBean
@SessionScoped
public class PresentationsDaoImp implements PresentationsDao, Serializable {

	private static final long serialVersionUID = 1L;
	HSSFWorkbook workbook = null;

	@Override
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

	@Override
	public void writePresentationsXls(List<Tab> tabs) {  
		 final String FILE_NAME = "EloadasExcel.xls";
		HSSFWorkbook workbook = new HSSFWorkbook();
		for(Tab tab : tabs){
			String tmp = tab.getTitle().replace("/", ".");
			String[] tmpDate = tmp.split("\\.");
			HSSFSheet sheet = workbook.createSheet(tmpDate[0]+"."+tmpDate[1]+"."+tmpDate[2]);
			int line = 0;
			for(Show show : tab.getShows()){
				HSSFRow rowhead = sheet.createRow((short)line);
				rowhead.createCell(0).setCellValue(show.getSectionTitle());
				line++;
				if(show.getOperator1() != null){
					rowhead= sheet.createRow((short)line);
					rowhead.createCell(0).setCellValue(show.getOperator1().getFrom());
					rowhead.createCell(1).setCellValue(show.getOperator1().getPresentationTitle());
					rowhead.createCell(2).setCellValue(show.getOperator1().getActor());
					line++;
				}
				if(show.getOperator2() != null){
					rowhead= sheet.createRow((short)line);
					rowhead.createCell(0).setCellValue(show.getOperator2().getFrom());
					rowhead.createCell(1).setCellValue(show.getOperator2().getPresentationTitle());
					rowhead.createCell(2).setCellValue(show.getOperator2().getActor());
					line++;
				}
				if(show.getOperator3() != null){
					rowhead= sheet.createRow((short)line);
					rowhead.createCell(0).setCellValue(show.getOperator3().getFrom());
					rowhead.createCell(1).setCellValue(show.getOperator3().getPresentationTitle());
					rowhead.createCell(2).setCellValue(show.getOperator3().getActor());
					line++;
				}
				if(show.getOperator4() != null){
					rowhead= sheet.createRow((short)line);
					rowhead.createCell(0).setCellValue(show.getOperator4().getFrom());
					rowhead.createCell(1).setCellValue(show.getOperator4().getPresentationTitle());
					rowhead.createCell(2).setCellValue(show.getOperator4().getActor());
					line++;
				}
				if(show.getOperator5() != null){
					rowhead= sheet.createRow((short)line);
					rowhead.createCell(0).setCellValue(show.getOperator5().getFrom());
					rowhead.createCell(1).setCellValue(show.getOperator5().getPresentationTitle());
					rowhead.createCell(2).setCellValue(show.getOperator5().getActor());
					line++;
				}
				if(show.getOperator6() != null){
					rowhead= sheet.createRow((short)line);
					rowhead.createCell(0).setCellValue(show.getOperator6().getFrom());
					rowhead.createCell(1).setCellValue(show.getOperator6().getPresentationTitle());
					rowhead.createCell(2).setCellValue(show.getOperator6().getActor());
					line++;
				}
				if(show.getOperator7() != null){
					rowhead= sheet.createRow((short)line);
					rowhead.createCell(0).setCellValue(show.getOperator7().getFrom());
					rowhead.createCell(1).setCellValue(show.getOperator7().getPresentationTitle());
					rowhead.createCell(2).setCellValue(show.getOperator7().getActor());
					line++;
				}
				if(show.getOperator8() != null){
					rowhead= sheet.createRow((short)line);
					rowhead.createCell(0).setCellValue(show.getOperator8().getFrom());
					rowhead.createCell(1).setCellValue(show.getOperator8().getPresentationTitle());
					rowhead.createCell(2).setCellValue(show.getOperator8().getActor());
					line++;
				}
				if(show.getOperator9() != null){
					rowhead= sheet.createRow((short)line);
					rowhead.createCell(0).setCellValue(show.getOperator9().getFrom());
					rowhead.createCell(1).setCellValue(show.getOperator9().getPresentationTitle());
					rowhead.createCell(2).setCellValue(show.getOperator9().getActor());
					line++;
				}
				line++;
			}
		}

        FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(FILE_NAME);
			workbook.write(fileOut);
	        fileOut.close();
	        workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File file = new File("EloadasExcel.xls");

	    FacesContext facesContext = FacesContext.getCurrentInstance();

	    HttpServletResponse response = 
	            (HttpServletResponse) facesContext.getExternalContext().getResponse();

	    response.reset();
	    response.setHeader("Content-Type", "application/octet-stream");
	    response.setHeader("Content-Disposition", "attachment;filename=EloadasExcel.xls");

	    OutputStream responseOutputStream;
		try {
			responseOutputStream = response.getOutputStream();
			InputStream fileInputStream = new FileInputStream(file);
			
			byte[] bytesBuffer = new byte[2048];
			int bytesRead;
			while ((bytesRead = fileInputStream.read(bytesBuffer)) > 0) 
			{
				responseOutputStream.write(bytesBuffer, 0, bytesRead);
			}
			
			responseOutputStream.flush();
			
			fileInputStream.close();
			responseOutputStream.close();
			
			facesContext.responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writePresentationsXlsx(List<Tab> tabs) {
		final String FILE_NAME = "EloadasExcel.xlsx";
		XSSFWorkbook workbook = new XSSFWorkbook();
		for(Tab tab : tabs){
			String tmp = tab.getTitle().replace("/", ".");
			String[] tmpDate = tmp.split("\\.");
			XSSFSheet sheet = workbook.createSheet("20"+tmpDate[0]+"."+tmpDate[2]+"."+tmpDate[1]);
			int line = 0;
			for(Show show : tab.getShows()){
				int row = 0;
				XSSFRow rowhead = sheet.createRow((short)line);
				rowhead.createCell(row).setCellValue(show.getSectionTitle());
				row++;
				if(show.getOperator1() != null){
					rowhead.createCell(row).setCellValue(show.getOperator1().getPresentationTitle());
					row++;
				}
				if(show.getOperator2() != null){
					rowhead.createCell(row).setCellValue(show.getOperator2().getPresentationTitle());
					row++;
				}
				if(show.getOperator3() != null){
					rowhead.createCell(row).setCellValue(show.getOperator3().getPresentationTitle());
					row++;
				}
				if(show.getOperator4() != null){
					rowhead.createCell(row).setCellValue(show.getOperator4().getPresentationTitle());
					row++;
				}
				if(show.getOperator5() != null){
					rowhead.createCell(row).setCellValue(show.getOperator5().getPresentationTitle());
					row++;
				}
				if(show.getOperator6() != null){
					rowhead.createCell(row).setCellValue(show.getOperator6().getPresentationTitle());
					row++;
				}
				if(show.getOperator7() != null){
					rowhead.createCell(row).setCellValue(show.getOperator7().getPresentationTitle());
					row++;
				}
				if(show.getOperator8() != null){
					rowhead.createCell(row).setCellValue(show.getOperator8().getPresentationTitle());
					row++;
				}
				if(show.getOperator9() != null){
					rowhead.createCell(row).setCellValue(show.getOperator9().getPresentationTitle());
					row++;
				}
				line++;
			}
		}
		
		try {
			FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
			workbook.write(outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File file = new File("EloadasExcel.xlsx");

	    FacesContext facesContext = FacesContext.getCurrentInstance();

	    HttpServletResponse response = 
	            (HttpServletResponse) facesContext.getExternalContext().getResponse();

	    response.reset();
	    response.setHeader("Content-Type", "application/octet-stream");
	    response.setHeader("Content-Disposition", "attachment;filename=EloadasExcel.xlsx");

	    OutputStream responseOutputStream;
		try {
			responseOutputStream = response.getOutputStream();
			InputStream fileInputStream = new FileInputStream(file);
			
			byte[] bytesBuffer = new byte[2048];
			int bytesRead;
			while ((bytesRead = fileInputStream.read(bytesBuffer)) > 0) 
			{
				responseOutputStream.write(bytesBuffer, 0, bytesRead);
			}
			
			responseOutputStream.flush();
			
			fileInputStream.close();
			responseOutputStream.close();
			
			facesContext.responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public LinkedList<PresentationOperator> readPresentations() {
		// TODO Auto-generated method stub
		return null;
	}
}
