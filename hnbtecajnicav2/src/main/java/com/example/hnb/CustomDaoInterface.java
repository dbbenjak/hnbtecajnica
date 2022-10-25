package com.example.hnb;


//interface used by the ExchangeRepository class
public interface CustomDaoInterface {

	public void createTable();
	
	public void insertTable(String date, String currencyName, String buyValue, String meanValue, String sellValue);
	
}
