package com.example.hnb.POJO;

//POJO model for use with HTML form
public class HtmlModel {

	private String fromdate;
	private String todate;
	private String currencyname;
	
	public HtmlModel() {
	}

	public HtmlModel(String fromdate, String todate, String currencyname) {
		super();
		this.fromdate = fromdate;
		this.todate = todate;
		this.currencyname = currencyname;
	}

	public String getFromdate() {
		return fromdate;
	}

	public void setFromdate(String fromdate) {
		this.fromdate = fromdate;
	}

	public String getTodate() {
		return todate;
	}

	public void setTodate(String todate) {
		this.todate = todate;
	}

	public String getCurrencyname() {
		return currencyname;
	}

	public void setCurrencyname(String currencyname) {
		this.currencyname = currencyname;
	}
	
}
