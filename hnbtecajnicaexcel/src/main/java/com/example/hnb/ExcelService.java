package com.example.hnb;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.hnb.POJO.Exchange;

public class ExcelService {

	Workbook workbook = new XSSFWorkbook(); //new Excel spreadsheet object
	
	
	//method for generating an Excel sheet
	//a new sheet is generated for each date in the interval, descending
	public void generateSheet(Collection<Exchange> collection, String date) {
		
		Sheet sheet = workbook.createSheet(date);
		
		//columns correspond to the attributes for each Exchange object
		sheet.setColumnWidth(0, 8000);
		sheet.setColumnWidth(1, 8000);
		sheet.setColumnWidth(2, 8000);
		sheet.setColumnWidth(3, 8000);
		sheet.setColumnWidth(4, 8000);
		sheet.setColumnWidth(5, 8000);
		sheet.setColumnWidth(6, 8000);
		
		//initiates and styles the header row
		Row header = sheet.createRow(0);
		
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 16);
		font.setBold(true);
		headerStyle.setFont(font);

		//puts attribute names in the header
		Cell headerCell = header.createCell(0);
		headerCell.setCellValue("Država");
		headerCell.setCellStyle(headerStyle);

		headerCell = header.createCell(1);
		headerCell.setCellValue("Šifra valute");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(2);
		headerCell.setCellValue("Ime valute");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(3);
		headerCell.setCellValue("Jedinica");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(4);
		headerCell.setCellValue("Kupovna vrijednost");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(5);
		headerCell.setCellValue("Srednja vrijednost");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(6);
		headerCell.setCellValue("Prodajna vrijednost");
		headerCell.setCellStyle(headerStyle);
		
		
		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);
		
		int rowCount = 1; //necessary for displaying each Exchange object in its own row
		
		//displays each Exchange object on that specific date in the sheet
		for(Exchange exc : collection) {
			
			Row row = sheet.createRow(rowCount);
			
			Cell cell = row.createCell(0);
			cell.setCellValue(exc.getCountry());
			cell.setCellStyle(style);

			cell = row.createCell(1);
			cell.setCellValue(exc.getCurrencyCode());
			cell.setCellStyle(style);
			
			cell = row.createCell(2);
			cell.setCellValue(exc.getCurrencyName());
			cell.setCellStyle(style);
			
			cell = row.createCell(3);
			cell.setCellValue(exc.getUnit());
			cell.setCellStyle(style);
			
			cell = row.createCell(4);
			cell.setCellValue(exc.getBuyValue());
			cell.setCellStyle(style);
			
			cell = row.createCell(5);
			cell.setCellValue(exc.getMeanValue());
			cell.setCellStyle(style);
			
			cell = row.createCell(6);
			cell.setCellValue(exc.getSellValue());
			cell.setCellStyle(style);
			
			rowCount++;
		}
	}
	
	
	//generates new Excel document in the target directory
	public void generateDocument() throws IOException {
		String excelPath = "../hnb/target/output.xlsx";
		FileOutputStream outputStream = new FileOutputStream(excelPath);
		workbook.write(outputStream);
		workbook.close();
	}
	
}
