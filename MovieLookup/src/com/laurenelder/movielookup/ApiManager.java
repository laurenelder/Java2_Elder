/*
 * Project:			MovieLookup
 * Package:			com.laurenelder.movielookup
 * Author:			Devin "Lauren" Elder
 * Date:			Jul 9, 2014
 * Class:			Java 2 Term 1407
 */
/*
package com.laurenelder.movielookup;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.util.Log;

public class ApiManager {

	static String tag = "ApiManager";
	static getAPIdata getData;
	static String fullURL = "";

	// API Method
    public static String getAPIresponse(URL url) {
    	String apiResponse = "";
    	try {
			URLConnection apiConnection = url.openConnection();
			BufferedInputStream bufferedInput = new BufferedInputStream(apiConnection
					.getInputStream());
			byte[] contextByte = new byte[1024];
			int bytesRead = 0;
			StringBuffer responseBuffer = new StringBuffer();
			while ((bytesRead = bufferedInput.read(contextByte)) != -1) {
				apiResponse = new String(contextByte, 0, bytesRead);
				responseBuffer.append(apiResponse);
			}
			apiResponse = responseBuffer.toString();
//			Log.i(tag, apiResponse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(tag, "No data returned in APIresponse");
			
			e.printStackTrace();
		}
    	Log.i(tag, apiResponse.toString());
    	return apiResponse;
    }
    
    // API Class
    static class getAPIdata extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String APIresponseStr = "";

			// API Call based on Selected API
			if (!fullURL.matches("")) {
				try {
					URL url = new URL(fullURL);
					APIresponseStr = getAPIresponse(url);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					Log.i(tag, "Something went wrong in getAPIdata");
					Log.e("error", e.getMessage().toString());
					e.printStackTrace();
				}
			}
			return APIresponseStr;
		}
		
		@Override
		protected void onPostExecute(String result) {
//			Log.i(tag, result);
			super.onPostExecute(result);
		}
    }

    // Call API Method and return response
	public static String getData(String myURL) {
		Log.i(tag, "API Manager Class hit");
		// TODO Auto-generated method stub
		Log.i(tag, fullURL.toString());
		fullURL = myURL;
		getData = new getAPIdata();
		String response = "";
		try {
			response = getData.execute(fullURL).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
}*/
