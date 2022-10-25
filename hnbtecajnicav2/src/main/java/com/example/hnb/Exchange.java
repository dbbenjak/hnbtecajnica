package com.example.hnb;


//class for setting up JSON to Java object deserialization
public class Exchange {

	//variable, constructor, getter, setter and custom toString() declarations below
	
	private String exchangeRate;
	private String date;
	private String country;
	private String currencyCode;
	private String currencyName;
	private String unit;
	private String buyValue;
	private String meanValue;
	private String sellValue;
	
	public Exchange() {
	}

	public Exchange(String exchangeRate, String date, String country, String currencyCode, String currencyName,
			String unit, String buyValue, String meanValue, String sellValue) {
		this.exchangeRate = exchangeRate;
		this.date = date;
		this.country = country;
		this.currencyCode = currencyCode;
		this.currencyName = currencyName;
		this.unit = unit;
		this.buyValue = buyValue;
		this.meanValue = meanValue;
		this.sellValue = sellValue;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getBuyValue() {
		return buyValue;
	}

	public void setBuyValue(String buyValue) {
		this.buyValue = buyValue;
	}

	public String getMeanValue() {
		return meanValue;
	}

	public void setMeanValue(String meanValue) {
		this.meanValue = meanValue;
	}

	public String getSellValue() {
		return sellValue;
	}

	public void setSellValue(String sellValue) {
		this.sellValue = sellValue;
	}

	@Override
	public String toString() {
		return currencyName + "	Kupovni tečaj: " + buyValue + "; Srednji tečaj: " + meanValue + "; Prodajni tečaj: " + sellValue;
	}
	
}
