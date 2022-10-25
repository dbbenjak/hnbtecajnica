package com.example.hnb;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Component
public class WebScraping implements CommandLineRunner {

	//method which prints out currencies and their exchange values on today's date
	//method is programmed to run on application startup
	public void run(String...args) throws URISyntaxException, IOException {
		
		//URL can be manually modified to return different exchange lists, for example on a different date
		URL jsonUrl = new URL("https://api.hnb.hr/tecajn/v1");
		
		//JSON to Java object conversion which uses the custom deserializer
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Exchange.class, new CustomDeserializer());
		mapper.registerModule(module);
		
		//Exchange array used because input JSON is in array form, even if only one currency element is present
		Exchange[] exchange = mapper.readValue(jsonUrl, Exchange[].class);
		
		System.out.println();
		System.out.println();
		System.out.println("Tečajna lista na dan " + exchange[0].getDate() + ".:");
		
		for(Exchange element : exchange) {
			System.out.println(element.toString());
		}
		
		//exiting program is necessary to close port 8080 for future use
		System.exit(0); 
	}
	
}
