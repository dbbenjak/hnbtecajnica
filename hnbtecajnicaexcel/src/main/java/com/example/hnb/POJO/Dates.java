package com.example.hnb.POJO;

public class Dates {

	private String fromdate;
	private String todate;
	
	public Dates() {}

	public Dates(String fromdate, String todate) {
		super();
		this.fromdate = fromdate;
		this.todate = todate;
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
	
}
