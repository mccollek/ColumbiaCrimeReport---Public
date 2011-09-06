package com.udhaa.ColumbiaCrimeReport;

import java.io.IOException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



public class ColumbiaCrimeReportActivity extends Activity implements OnClickListener, OnTouchListener {
    /** Called when the activity is first created. */
	private static final String TAG = "CAECrimeReport";
	String message = null;
	ArrayList<CrimeReport> ReportList = new ArrayList<CrimeReport>();
	Date requestdate;
	String RequestURL=null;
	int alertcolor = Color.RED;
	int regularcolor = Color.GRAY;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ProgressBar mainpb = (ProgressBar)findViewById(R.id.progressBar);
        final TextView statusarea = (TextView)findViewById(R.id.textMissingReport);
        DatePicker dp=(DatePicker)findViewById(R.id.datePicker1);
        final Button buttonGetReport = (Button)findViewById(R.id.getreport);
        new ReportLibInterface().execute("@string/SourceURL");
        String BaseURL = "@string/BaseURL" ;
        buttonGetReport.setOnClickListener(this);		
        
        OnDateChangedListener listener = new OnDateChangedListener(){

			@Override
			public void onDateChanged(DatePicker dp, int year,
					int monthOfYear, int dayOfMonth) {
				Calendar c = Calendar.getInstance();
				//Log.d(TAG,"You selected "+ dp.getDayOfMonth()+"/"+(dp.getMonth()+1)+"/"+dp.getYear());
				SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
				c.set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), 0,0,0);
				Date searchdate = c.getTime();
				//Log.d(TAG,"Searchdate is: " + searchdate.toString());
				for (CrimeReport CrimeReport: ReportList){
					if ((CrimeReport.ReportDate.getMonth() == searchdate.getMonth()) && (CrimeReport.ReportDate.getDate() == searchdate.getDate())){
//						Log.d(TAG,"Found a match!");
						RequestURL = CrimeReport.BaseURL;
//						Log.d(TAG,"This date is valid for: " + RequestURL);
						buttonGetReport.setEnabled(true);
						statusarea.setVisibility(4);
						break;
					}else {
//						Log.d(TAG,"Searchdate: " + searchdate.toString() + "is not matching this record's date: " + CrimeReport.ReportDate.toString()  );
						buttonGetReport.setEnabled(false);
						statusarea.setVisibility(0);
					}
				}
			}
        	
        };
        Calendar d = Calendar.getInstance();        
        dp.init(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH), listener);    
    }

	class ReportLibInterface extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... statuses) 
		{
			String url = "http://www.columbiapd.net/pdfs/bulletins/";
			String linkHref = null;
			URLConnection conn;
			Document doc;
			try {
//				Log.d(TAG, "Loading Doc");
				doc = Jsoup.connect(url).timeout(0).get();
			} catch (IOException e) {
				message = "Could not load doc";
				e.printStackTrace();
				return message;
			}
//			Log.d(TAG, "getting content");
//			Element content = doc.getElementById("content");
//			Log.d(TAG, "parsing a elements");
//			Elements links = content.getElementsByTag("a");
			Elements links = doc.select("a[href$=.pdf]");
			int x = 0;
			Date linkdate=null;
			SimpleDateFormat formatter = new SimpleDateFormat("MMddyy");
//			Log.d(TAG, "done parsing, the list of links is:");
			for (Element link : links) {
			  // String tempstring = link.text();	
			  String[] datestring = link.text().split("\\.");
//			  Log.d(TAG, "result of split:" + datestring[0]);
			  try {
				linkdate = (Date)formatter.parse(datestring[0]);
//				Log.d(TAG, "parsed date as: " + linkdate.toString());
			} catch (Exception e) {
//				Log.d(TAG, "couldn't parse: " + datestring[0]);
				e.printStackTrace();
				continue;
			}
			  
			  ReportList.add(new CrimeReport( url + link.text(), linkdate));
			  //String linkText = link.text();
//			  Log.d(TAG, "Successfully added Crime Report to Report List!");
			  x=x+1;
			}
			return "Select a Date!";
		}
		
		@Override
	    protected void onProgressUpdate(Integer... values) { // 
	      super.onProgressUpdate(values);
	      // TODO: link the loading to the progress indicator
	    }
		
	    // Called once the background activity has completed
	    @Override
	    protected void onPostExecute(String result) { // 
	        ProgressBar mainpb = (ProgressBar)findViewById(R.id.progressBar);
	        TextView statusarea = (TextView)findViewById(R.id.StatusMessage);
	        DatePicker dp = (DatePicker)findViewById(R.id.datePicker1);
			Button buttonGetReport = (Button)findViewById(R.id.getreport);
    		statusarea.setText("");
    		mainpb.setVisibility(8);
    		dp.setVisibility(0);
    		//buttonGetReport.setEnabled(true);
    		Toast.makeText(ColumbiaCrimeReportActivity.this, result, Toast.LENGTH_LONG).show();
	    }
	  
		
	}
	
	
	
	public void onClick(View v) {
		Intent i;
		switch (v.getId()){
		case R.id.getreport:
			{
			//get date from date selector
//			Log.d(TAG,"Once this works I'm going to get: " + RequestURL);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(RequestURL));
                try {
                    startActivity(intent);
                } 
                catch (ActivityNotFoundException e) {
//                        Log.d(TAG,"No Application Available to View PDF"); 
                }
            }
			return;
		default:	
			return;
		}
	}

	

	@Override
	
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	

}