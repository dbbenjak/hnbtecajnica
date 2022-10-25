package com.example.hnb;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HnbApplication {

	public static void main(String[] args) throws URISyntaxException, IOException, SQLException{
		SpringApplication.run(HnbApplication.class, args);
	}

}
