package com.example.hnb;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.example.hnb.POJO.Exchange;
import com.example.hnb.POJO.ExchangeCollection;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

//class for report generation and manipulation
public class ReportService {

	//necessary objects for report generation
	String path = "../hnb/src/main/resources/main.jrxml";
	String pdfPath = "../hnb/target/output.pdf";
	JasperReport report;
	HashMap<String, Object> parameters = new HashMap<>();
	Collection<Exchange> subcoll = new ArrayList<Exchange>();
	Collection<ExchangeCollection> maincoll = new ArrayList<ExchangeCollection>();
	
	//compiles main report and gives it necessary parameters
	public void start(String fromDate, String toDate) throws JRException {
		JasperDesign jasperDesign = JRXmlLoader.load(path);
		report = JasperCompileManager.compileReport(jasperDesign);
		parameters.put("fromDate", fromDate);
		parameters.put("toDate", toDate);
	}
	
	//encapsulates existing collections into another collection
	//this way, each individual collection is used in subreport generation
	public void addToMainColl(Collection<Exchange> subcoll) {
		ExchangeCollection ec = new ExchangeCollection(subcoll);
		maincoll.add(ec);
	}
	
	//generates main report with its subreports
	public void generateReport() throws SQLException, JRException {	
				
		//newly created collection is used as data source instead of the database
		JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(maincoll));
		JasperExportManager.exportReportToPdfFile(jprint, pdfPath);	
	}
	
}
