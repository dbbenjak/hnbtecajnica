package com.example.hnb;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.hnb.POJO.Dates;

//class for setting up the HTML web components of the prompt
@Component
@Controller
@RequestMapping(path = "")
public class WebBrowser implements CommandLineRunner {

	//page loads on program startup
	@Override
	public void run(String... args) throws Exception {
		
		try {
			System.setProperty("java.awt.headless", "false"); //necessary to avoid HeadlessException
            URI page = new URI("http://localhost:8080/form"); //user is redirected to the specified URI
            Desktop.getDesktop().browse(page);
                       
        } catch (Exception e) {
            e.printStackTrace();
        }		
	}
	
	
	//a form is presented to the user for input when at specified URI
	@GetMapping("/form")
	public String getForm(Model model) {
		Dates dates = new Dates();
		model.addAttribute("dates", dates);
		return "form";
	}
	
	
	//method that handles user input on the form element
	@PostMapping("/form")
    public void submit(@ModelAttribute Dates dates, Model model) throws URISyntaxException, IOException, SQLException, ClassNotFoundException {
			
		//user must input both dates for the program to advance
		if(dates.getFromdate().isEmpty() && dates.getTodate().isEmpty()) {
			
			System.out.println("Datumi ne smiju biti prazni!");
			
		}else if(dates.getTodate().isEmpty()) {
			
			System.out.println("Datum završetka ne smije biti prazan!");
			
		}else if(dates.getFromdate().isEmpty()) {
			
			System.out.println("Datum početka ne smije biti prazan!");
			
		}else {
			
			//dates are validated and, if valid, are sent to further processing
			WebScraping ws = new WebScraping();
			boolean test = ws.dateValidator(dates.getFromdate(), dates.getTodate());
			
			if(test) {
				if(ws.isGenerated(dates.getFromdate(), dates.getTodate())) {
					try {
						System.setProperty("java.awt.headless", "false");
			            URI page = new URI("http://localhost:8080/success"); //user is redirected to new URI
			            Desktop.getDesktop().browse(page);
			                       
			        } catch (Exception e) {
			            e.printStackTrace();
			            System.exit(1);
			        }
				}				
			}else {
				System.out.println("Loši datumi!"); //in case the dates are invalid
			}
			
		}
    }

	
	//new site is presented to the user when target PDF is made
	@GetMapping("/success")
	public String getSuccess() {
		new Thread() {
		    @Override
		    public void run() {
		        try {
					Thread.sleep(2000);
					System.exit(0); //program closes automatically after success message is returned
				} catch (InterruptedException e) {
				}
		    }
		}.start();
		return "success";
	}
	
	
}
