/*
 * Project:			MovieLookup
 * Package:			com.laurenelder.movielookup
 * Author:			Devin "Lauren" Elder
 * Date:			Jul 31, 2014
 * Class:			Java 2 Term 1407
 */

package com.laurenelder.movielookup;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class ApiService extends IntentService {
	
	static String tag = "ApiService";
	public static final String MESSENGER_KEY = "messenger";
	public static final String INPUT_KEY = "input";

	public ApiService() {
		super("ApiService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
//		Log.i(tag, "API Service Started");
		
		// Set Intent Attributes
		Bundle extras = intent.getExtras();
		Messenger theMessenger = (Messenger)extras.get(MESSENGER_KEY);
		String url = extras.getString(INPUT_KEY);
		Message message = Message.obtain();
		message.arg1 = Activity.RESULT_OK;
		
		// Fetch API Data and format inputStream to String
    	String apiResponse = "";
    	
    	try {
    		URL fullURL = new URL(url);
			URLConnection apiConnection = fullURL.openConnection();
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
			message.obj = apiResponse;
			try {
				theMessenger.send(message);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(tag, "No data returned in APIresponse");
			
			e.printStackTrace();
		}
    	Log.i(tag, apiResponse.toString());
	}
}
