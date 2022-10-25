package com.example.hnb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

//functions as applications's Data Access Object
public class ExchangeRepository implements CustomDaoInterface {

	  //JDBC and H2 DB initialization
	  private static final String JDBC_DRIVER = "org.h2.Driver";
	  private static final String DB_URL = "jdbc:h2:tcp://localhost/~/test"; //can be changed to use different modes, for example embedded
	  private static final String USER = "sa";
	  private static final String PASS = "";
	  
	  //method for creating a table
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
			  String createSql = "CREATE TABLE Exchange(excDate DATE, currencyName CHAR(3), buyValue DECIMAL(7,6), meanValue DECIMAL(7,6), sellValue DECIMAL(7,6), PRIMARY KEY(excDate, currencyName));";
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
	  public void insertTable(String date, String currencyName, String buyValue, String meanValue, String sellValue) {
	  
		  Connection con = null; 
	      Statement st = null; 
		  
	      try {
	    	  
	    	  Class.forName(JDBC_DRIVER);
			  
			  con = DriverManager.getConnection(DB_URL, USER, PASS);
			  
			  st = con.createStatement();
			  String insertSql = "INSERT INTO Exchange VALUES ('" + date + "', '" + currencyName + "', " + buyValue + ", " + meanValue + ", " + sellValue + ");";
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
