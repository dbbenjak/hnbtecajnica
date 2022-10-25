package com.example.hnb;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import java.sql.Connection;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

//class for report generation and manipulation
public class ReportService {

	//necessary objects for report generation
	String path = "../hnb/src/main/resources/Mainreport.jrxml";
	String pdfPath = "../hnb/target/output.pdf";
	JasperReport report;
	private String db_url;
	private String user;
	private String pass;
	HashMap<String, Object> parameters = new HashMap<>();
	
	//necessary for class to get connection parameters from ExchangeRepository class
	//useful for avoiding boilerplate code
	public ReportService(String db_url, String user, String pass) {
		this.db_url = db_url;
		this.user = user;
		this.pass = pass;
	}
	
	//compiles main report and gives it necessary parameters
	public void start(String fromDate, String toDate) throws JRException {
		JasperDesign jasperDesign = JRXmlLoader.load(path);
		report = JasperCompileManager.compileReport(jasperDesign);
		parameters.put("fromDate", fromDate);
		parameters.put("toDate", toDate);
	}
	
	//generates main report with its subreports
	public void generateReport() throws SQLException, JRException {

		Connection con = DriverManager.getConnection(db_url, user, pass);
					
		JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(report, parameters, con);
		JasperExportManager.exportReportToPdfFile(jprint, pdfPath);	
	}
	
}
