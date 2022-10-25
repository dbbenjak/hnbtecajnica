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

import com.example.hnb.POJO.HtmlModel;

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
		model.addAttribute("htmlmodel", new HtmlModel());
		return "form";
	}
	
	
	//method that handles user input on the form element
	@PostMapping("/form")
    public void submit(@ModelAttribute("htmlmodel") HtmlModel data, Model model) throws URISyntaxException, IOException, SQLException, ClassNotFoundException {
			
		//user must input both dates and desired currency code for the program to advance
		if(data.getFromdate().isEmpty() || data.getTodate().isEmpty() || data.getCurrencyname().isEmpty()) {
			
			System.out.println("Sva polja moraju biti ispunjena!");
		}else {
			
			//dates and currency code are validated and, if valid, are sent to further processing
			WebScraping ws = new WebScraping();
			boolean test = ws.dateValidator(data.getFromdate(), data.getTodate());
			
			if(test) {
				if(ws.isGenerated(data.getFromdate(), data.getTodate(), data.getCurrencyname())) {
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
		return "success";
	}
	
	
}
