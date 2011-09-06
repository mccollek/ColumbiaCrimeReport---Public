package com.udhaa.ColumbiaCrimeReport;

import java.util.Date;

public class CrimeReport {
	String BaseURL="";
	Date ReportDate;
	
	public CrimeReport(String t, Date d) {
		BaseURL = t;
		ReportDate = d;
	}

	public void reset (){
		BaseURL="";
		ReportDate=null;
	}
}
