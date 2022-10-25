package com.example.hnb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

//functions as applications's Data Access Object
public class ExchangeRepository {

	  //JDBC and H2 DB initialization
	  //can be manually changed to switch database to different modes
	  private static final String JDBC_DRIVER = "org.h2.Driver";
	  private static final String DB_URL = "jdbc:h2:tcp://localhost/~/test";
	  private static final String USER = "sa";
	  private static final String PASS = "";
	  
	  
	  //needed to pass connection parameters to ReportService class
	  public String[] connectionParameters() {
		  String[] param = new String[3];
		  param[0] = DB_URL;
		  param[1] = USER;
		  param[2] = PASS;
		  return param;
	  }
	  
	  
	  //method for creating a table and passing connection parameters to ReportService class
	  public void createTable() {
		  
		  Connection con = null;
		  Statement st = null;
		  
		  try {
			  
			  Class.forName(JDBC_DRIVER);
			  
			  con = DriverManager.getConnection(DB_URL, USER, PASS);
			  
			  st = con.createStatement(); 
			  //table is dropped if it exists, meaning new data will overwrite previous data
			  String dropSql = "DROP TABLE IF EXISTS Exchange;";
			  //new Exchange table is created in H2 DB
			  String createSql = "CREATE TABLE Exchange(excDate DATE, country VARCHAR(40), currencyCode CHAR(3), currencyName CHAR(3), unit INT, buyValue DECIMAL(8,6), meanValue DECIMAL(8,6), sellValue DECIMAL(8,6), PRIMARY KEY(excDate, currencyName));";
			  //SQL command execution
			  st.execute(dropSql);
			  st.execute(createSql);
			  
			  //resources need to be closed
			  st.close();
			  con.close();
			  		  
		  }catch(Exception e) {
			  
			  e.printStackTrace();
			  System.exit(1);
		  }finally {
			  //an exception occuring would prevent resources from closing normally
			  //these resources still need to be closed, which is implemented in this finally block
			  try {
				  if(st != null)
					  st.close();
			  }catch(Exception e) {
				  System.out.println("Couldn't close statement object!");
			  }
			  
			  try {
				  if(con != null)
					  con.close();
			  }catch(Exception e) {
				  System.out.println("Couldn't close server object!");
			  }
		  }
	  
	  }
	  
	  
	  //analogous to the createTable() method, except this method is used for inserting data into the newly created table
	  public void insertTable(String date, String country, String currencyCode, String currencyName, String unit, String buyValue, String meanValue, String sellValue) {
	  
		  Connection con = null; 
	      Statement st = null; 
		  
	      try {
	    	  
	    	  Class.forName(JDBC_DRIVER);
			  
			  con = DriverManager.getConnection(DB_URL, USER, PASS);
			  
			  st = con.createStatement();
			  String insertSql = "INSERT INTO Exchange VALUES ('" + date + "', '" + country + "', '" + currencyCode + "', '" + currencyName + "', " + unit + ", " + buyValue + ", " + meanValue + ", " + sellValue + ");";
			  st.executeUpdate(insertSql);
			  
			  st.close();
			  con.close();
	    	  
	      }catch(Exception e) {
	    	  
	    	  e.printStackTrace();
	    	  System.exit(1);
	      }finally {
	    	  
	    	  try {	    		  
	    		  if(st != null)
	    			  st.close();	    		  
	    	  }catch(Exception e) {
	    		  System.out.println("Couldn't close statement object!");
	    	  }
	    	  	    	  
	    	  try {    		  
	    		  if(con != null)
	    			  con.close();	    		  
	    	  }catch(Exception e) {
	    		  System.out.println("Couldn't close server object!");
	    	  }
	    	  
	      }		  	  
	  }
	  
}
