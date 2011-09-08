package com.udhaa.ColumbiaCrimeReport;

//ColumbiaCrimeReport is an Android application written in Java
//Copyright 2011 Karl McCollester and Palmetto Computer Labs, LLC
//Licensed under the Gnu GPL
//
//  This file is part of the Columbia Crime Report (iCrime) software.
//
//  The Columbia Crime Report (iCrime) software is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  The Columbia Crime Report (iCrime) software is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with Foobar.  If not, see <http://www.gnu.org/licenses/>.


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
