package com.example.hnb;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

//this class also functions as Manager for handling business logic and can be further expanded
@Component
public class WebScraping implements CommandLineRunner {

	//method which adds currencies and their exchange values on user-inputted date to the database
	//method is programmed to run on application startup
	public void run(String...args) throws URISyntaxException, IOException, SQLException {
		
		//new instance of our custom DAO class
		ExchangeRepository er = new ExchangeRepository();
		er.createTable();		
		
		LocalDate fromDate = null, toDate = null;
		int i = 0; //variable i used for mapping particular error in case of exception regarding user-inputted dates
		
		//this try-catch block has the same function as a date validator
		try {
			
			System.out.println();
			System.out.println();
			Scanner sc = new Scanner(System.in);
			System.out.println("Unesite datum početka pretrage u obliku [YYYY-MM-DD]: ");
			fromDate = LocalDate.parse(sc.next());
			
			i++;
			
			System.out.println("Unesite datum završetka pretrage u obliku [YYYY-MM-DD]: ");
			toDate = LocalDate.parse(sc.next());
			sc.close();
			
			i++;
			
			if(fromDate.isAfter(toDate))
				throw new Exception();
			
			i++;
			
			if(fromDate.isAfter(LocalDate.now()) || toDate.isAfter(LocalDate.now()))
				throw new Exception();
			
		}catch(Exception e){
			
			if(i == 0) {
				System.out.println("Datum početka je neispravan!");
			}else if(i == 1){
				System.out.println("Datum završetka je neispravan!");
			}else if(i == 2){
				System.out.println("Datum početka ne smije biti prije datuma završetka!");
			}else {
				System.out.println("Nijedan od dva datuma ne smije biti nakon današnjeg datuma!");
			}
			
			System.exit(1);			
		}
		
		
		//JSON from bank website is parsed for each date in the fromDate-toDate range
		while(!fromDate.equals(toDate.plusDays(1))) {
			
			//custom URL with appended date variable
			URL jsonUrl = new URL("https://api.hnb.hr/tecajn/v1?datum=" + fromDate);
			
			//JSON to Java object conversion which uses the custom deserializer
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addDeserializer(Exchange.class, new CustomDeserializer());
			mapper.registerModule(module);
			
			//Exchange array used because input JSON is in array form, even if only one currency element is present
			Exchange[] exchange = mapper.readValue(jsonUrl, Exchange[].class);
						
			//System.out.println("Tečajna lista na dan " + exchange[0].getDate() + ".:");
			
			for(Exchange element : exchange) {
				element.setDate(dateSanitizer(element.getDate()));
				element.setBuyValue(priceSanitizer(element.getBuyValue()));
				element.setMeanValue(priceSanitizer(element.getMeanValue()));
				element.setSellValue(priceSanitizer(element.getSellValue()));
				er.insertTable(element.getDate(), element.getCurrencyName(), element.getBuyValue(), element.getMeanValue(), element.getSellValue());
			}
			
			fromDate = fromDate.plusDays(1);
			
		}
		
		System.out.println("Table successfully updated");
				
		//exiting program is necessary to close port 8080 for future use
		System.exit(0); 
	}
	
	
	//necesarry because SQL only supports decimal numbers usinf the decimal point(.) and not the comma(,)
	//some prices(notably, for the XDR currency) are left blank and must be made into NULL before passing them over to SQL processing
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
