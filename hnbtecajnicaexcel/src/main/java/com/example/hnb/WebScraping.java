package com.example.hnb;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import com.example.hnb.POJO.Exchange;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

//functions as Manager for handling business logic and can be further expanded
public class WebScraping {

	//method for retrieving data from bank API and initializing Jasper Report construction
	public boolean isGenerated(String fromDate, String toDate) throws URISyntaxException, IOException, SQLException, ClassNotFoundException {
		
		ExchangeRepository er = new ExchangeRepository();
		er.createTable(); //creates new Exchange table in database
		
		ExcelService es = new ExcelService();
		
		LocalDate newFromDate = LocalDate.parse(fromDate);
		LocalDate newToDate = LocalDate.parse(toDate);
		
		//JSON from bank website is parsed for each date in the fromDate-toDate range, in descending order
		while(!newToDate.equals(newFromDate.minusDays(1))) {
			
			//new subreport collection instance
			Collection<Exchange> collection = new ArrayList<Exchange>();
			
			//custom URL with appended date variable
			URL jsonUrl = new URL("https://api.hnb.hr/tecajn/v1?datum=" + newToDate);
			
			//JSON to Java object conversion which uses the custom deserializer
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addDeserializer(Exchange.class, new CustomDeserializer());
			mapper.registerModule(module);
			
			//Exchange array used because input JSON is in array form, even if only one currency element is present
			Exchange[] exchange = mapper.readValue(jsonUrl, Exchange[].class);			
			
			for(Exchange element : exchange) {
				element.setDate(dateSanitizer(element.getDate()));
				element.setBuyValue(priceSanitizer(element.getBuyValue()));
				element.setMeanValue(priceSanitizer(element.getMeanValue()));
				element.setSellValue(priceSanitizer(element.getSellValue()));
				er.insertTable(element.getDate(), element.getCountry(), element.getCurrencyCode(), element.getCurrencyName(), element.getUnit(), element.getBuyValue(), element.getMeanValue(), element.getSellValue());
				collection.add(element); //adds each Exchange object to Excel sheet collection
			}
			
			String excelDate = newToDate.toString();
			es.generateSheet(collection, excelDate); //new sheet is generated for each date in the interval
						
			newToDate = newToDate.minusDays(1);			
		}
		
		es.generateDocument(); //document is generated after each Exchange object is added to spreadsheet
				
		return true;
	}
	
	
	//validator which checks if dates are in proper format
	public boolean dateValidator(String fromDate, String toDate) {
		
		try {
			
			LocalDate newFromDate = LocalDate.parse(fromDate);
			
			LocalDate newToDate = LocalDate.parse(toDate);
			
			if(newFromDate.isAfter(newToDate)) {
				System.out.println("Datum početka ne smije biti prije datuma završetka!");
				throw new Exception();
			}				
			
			if(newFromDate.isAfter(LocalDate.now()) || newToDate.isAfter(LocalDate.now())) {
				System.out.println("Nijedan od dva datuma ne smije biti nakon današnjeg datuma!");
				throw new Exception();
			}				
			
			return true;			
		}catch(Exception e) {
			return false;			
		}				
	}
	
	
	//necesarry because SQL only supports decimal numbers usinf the decimal point(.) and not the comma(,)
	//some prices(notably, for the XDR currency) are left blank and must be made into NULL before  them over to SQL processing
	public String priceSanitizer(String price) {
		
		if(price.isBlank())
			return "NULL";
		
		String newPrice = price.replace(',', '.');
		return newPrice;		
	}
	
	
	//default dates from the JSON files are in the [DD.MM.YYYY] format
	//they must be converted to the [YYYY-MM-DD] format to be properly processed in SQL
	public String dateSanitizer(String date) {
		
		String day = date.substring(0, 2);
		String month = date.substring(3, 5);
		String year = date.substring(6, 10);
		String newDate = year + "-" + month + "-" + day;
		return newDate;		
	}
	
}
