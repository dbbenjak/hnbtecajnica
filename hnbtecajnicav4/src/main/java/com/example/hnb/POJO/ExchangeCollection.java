package com.example.hnb.POJO;

import java.util.ArrayList;
import java.util.Collection;

//necessary to get individual subreport for each date in the interval
public class ExchangeCollection {

	Collection<Exchange> collection = new ArrayList<Exchange>();

	public ExchangeCollection(Collection<Exchange> collection) {
		this.collection = collection;
	}

	public Collection<Exchange> getCollection() {
		return collection;
	}

	public void setCollection(Collection<Exchange> collection) {
		this.collection = collection;
	}
	
}
