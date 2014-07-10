/*
 * Project:			MovieLookup
 * Package:			com.laurenelder.movielookup
 * Author:			Devin "Lauren" Elder
 * Date:			Jul 9, 2014
 * Class:			Java 2 Term 1407
 */

package com.laurenelder.movielookup;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity {
	
	// Global Variables
	static String tag = "MAINACTIVITY";
	EditText searchField;
	Button findButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        searchField = (EditText)findViewById(R.id.searchField);
        findButton = (Button)findViewById(R.id.findButton);
        findButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (searchField.getText().toString().matches("")) {
					showNotfication("input");
				} else {
					Handler apiHandler = new Handler() {

						@Override
						public void handleMessage(Message msg) {
							// TODO Auto-generated method stub
							if (msg.arg1 == RESULT_OK) {
								Log.i(tag, msg.obj.toString());
								// Check to make sure raw JSON was saved
								// Run parse method and save parsed data
								// Update UI with parsed data
							}
						}
					};
					Messenger apiMessenger = new Messenger(apiHandler);
					
					Intent startApiIntent = new Intent(this, ApiService.class);
					startApiIntent.putExtra(ApiService.MESSENGER_KEY, apiMessenger);
					startApiIntent.putExtra(ApiService.INPUT_KEY, searchField.getText().toString());
					startService(startApiIntent);
					showNotfication("searching");
				}
			}
        	
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
    
	// Error Notification
	private void showNotfication(String myNotification) {
    	
    	// Set Alert Text based on Error
    	if (myNotification == "input") {
    		Toast.makeText(this, R.string.input_notification, Toast.LENGTH_LONG).show();
    	}
    	if (myNotification == "connection") {
    		Toast.makeText(this, R.string.connection_notification, Toast.LENGTH_LONG).show();
    	}
    	if (myNotification == "searching") {
    		Toast.makeText(this, R.string.searching_notification, Toast.LENGTH_LONG).show();
    	}
    }

}
