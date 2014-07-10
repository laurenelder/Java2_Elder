/*
 * Project:			MovieLookup
 * Package:			com.laurenelder.movielookup
 * Author:			Devin "Lauren" Elder
 * Date:			Jul 9, 2014
 * Class:			Java 2 Term 1407
 */

package com.laurenelder.movielookup;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class ApiService extends IntentService {
	
	static String tag = "ApiService";
	public static final String MESSENGER_KEY = "messenger";
	public static final String INPUT_KEY = "input";
	String constructedURL = "";

	public ApiService() {
		super("ApiService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(tag, "API Service Started");
		
		Bundle extras = intent.getExtras();
		Messenger theMessenger = (Messenger)extras.get(MESSENGER_KEY);
		String searchInput = extras.getString(INPUT_KEY);
				
		constructedURL = R.string.pre_api + searchInput + R.string.post_api;
//		ApiManager(constructedURL);
//		ApiManager.getData();
//		Log.i(tag, ApiManager.getData().toString());
		Message message = Message.obtain();
		message.arg1 = Activity.RESULT_OK;
		message.obj = ApiManager.getData().toString();
		try {
			theMessenger.send(message);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(tag, e.getMessage().toString());
		}
	}
}
