/*
 * Project:			MovieLookup
 * Package:			com.laurenelder.movielookup
 * Author:			Devin "Lauren" Elder
 * Date:			Jul 9, 2014
 * Class:			Java 2 Term 1407
 */

package com.laurenelder.movielookup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity {
	
	// Global Variables
	FileManager fileManager;
	static String tag = "MAINACTIVITY";
	EditText searchField;
	Button findButton;
	static Context context;
//	String externalFileName = getResources().getString(R.string.file_name);
	
	// Array Adapter
	ArrayAdapter<MovieListItems> listAdapter;
	
	// Class List
	List<MovieListItems> movieList = new ArrayList<MovieListItems>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        context = this;
        fileManager = FileManager.getInstance();
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
								String externalFileName = getResources().getString(R.string.file_name);
								fileManager.writeToFile(context, externalFileName, msg.obj.toString());
								parseData(msg.obj.toString());
								// Check to make sure raw JSON was saved
								// Run parse method and save parsed data
								// Update UI with parsed data
							}
						}
					};
					
					if (checkNetworkConnection(context)) {
						Messenger apiMessenger = new Messenger(apiHandler);
						
						String myURL = getResources().getString(R.string.main_api);
						myURL = myURL.replace("_", "&");
						
						Intent startApiIntent = new Intent(context, ApiService.class);
						startApiIntent.putExtra(ApiService.MESSENGER_KEY, apiMessenger);
						startApiIntent.putExtra(ApiService.INPUT_KEY, myURL);
						startService(startApiIntent);
						showNotfication("searching");
					} else {
						showNotfication("connection");
					}
				}
			}
        });
        
        // ListView Code 
		listAdapter = new customListAdapter();
        ListView listView = (ListView)findViewById(R.id.list);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				// Load Movie Details UI here
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
	
	// Get and Parse JSON Function
	public void parseData(String apiData) {

		String jsonString = null;
		if (apiData != null) {
			jsonString = apiData;
		}

		// Parse JSON
		try {
			// Creating JSONObject from String
				JSONObject mainObject = new JSONObject(jsonString);
				JSONArray subObject = mainObject.getJSONArray("Search");
				Log.i(tag, subObject.toString());

				for (int i = 0; i < subObject.length(); i ++) {
					JSONObject movieObject = subObject.getJSONObject(i);
					// Class Specific Data
					
					String thisName = movieObject.getString("Title");
					String thisYear = movieObject.getString("Year");
					String thisType = movieObject.getString("Type");
					String thisID = movieObject.getString("imdbID");
					Log.i(tag, thisName);
					Log.i(tag, thisYear);
					Log.i(tag, thisType);
					Log.i(tag, thisID);

					// Save Data Here
					setClass(thisName, thisYear, thisType, thisID);
				}
				listAdapter.notifyDataSetChanged();
		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e(tag, e.getMessage().toString());
			e.printStackTrace();
		}
	}

	// Save Parsed Data to Class
	public void setClass(String name, String year, String type, String ID) {
		MovieListItems newMovie = new MovieListItems(name, year, type, ID);
		movieList.add(newMovie);
		Log.i(tag, newMovie.toString());
	}
	
    // Check Network Connection Method
    public Boolean checkNetworkConnection (Context context) {
    	Boolean connected = false;
    	ConnectivityManager connManag = (ConnectivityManager) MainActivity.context
    			.getSystemService
    			(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo networkInfomation = connManag.getActiveNetworkInfo();
    	if (networkInfomation != null) {
    		if (networkInfomation.isConnected()) {
    			Log.i(tag, "Connection Type: " + networkInfomation.getTypeName()
    					.toString());
    			connected = true;
    		}
    	}
    	return connected;
    }
    
    // Custom ListAdapter Class
    public class customListAdapter extends ArrayAdapter <MovieListItems> {
    	public customListAdapter() {
    		super(context, R.layout.custom_list_item, movieList);
    	}

    	// Set List Item Information
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View customItemView = convertView;
			
			Log.i(tag, "Custom Adapter Hit");

			if (customItemView == null) {
				LayoutInflater viewInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				customItemView = viewInflater.inflate(R.layout.custom_list_item, parent, false);
			}

			TextView name = (TextView)customItemView.findViewById(R.id.movieTitle);
			TextView year = (TextView)customItemView.findViewById(R.id.movieYear);
			TextView type = (TextView)customItemView.findViewById(R.id.movieType);

			name.setText(movieList.get(position).movieName.toString());
			year.setText(movieList.get(position).movieYear.toString());
			type.setText(movieList.get(position).movieType.toString());

			return customItemView;
		}
    }
}
