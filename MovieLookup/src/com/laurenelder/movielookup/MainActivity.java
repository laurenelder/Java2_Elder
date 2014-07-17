/*
 * Project:			MovieLookup
 * Package:			com.laurenelder.movielookup
 * Author:			Devin "Lauren" Elder
 * Date:			Jul 9, 2014
 * Class:			Java 2 Term 1407
 */

package com.laurenelder.movielookup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
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

public class MainActivity extends Activity {
	
	// Global Variables
	FileManager fileManager;
	static String tag = "MAINACTIVITY";
	EditText searchField;
	Button findButton;
	static Context context;
	String externalFileName;
	static Handler apiHandler = null;
	boolean detailApi = false;
	String myURL;
	String na;
//	Integer fileNameModifier = 0;
	
	// Array Adapter for ListView
	ArrayAdapter<MovieListItems> listAdapter;
	
	// Class List to access movieList Objects
	List<MovieListItems> movieList = new ArrayList<MovieListItems>();
	List<MovieDetails> movieDetails = new ArrayList<MovieDetails>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        
        // Set Variables after UI has Loaded
        context = this;
        fileManager = FileManager.getInstance();
        searchField = (EditText)findViewById(R.id.searchField);
        findButton = (Button)findViewById(R.id.findButton);
        externalFileName = getResources().getString(R.string.file_name);
        na = getResources().getString(R.string.n_a);
        
        // ListView Adapter Code 
		listAdapter = new customListAdapter();
        ListView listView = (ListView)findViewById(R.id.list);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			// Handle onClick for list items
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				
				// Load Movie Details UI here
				apiHandler = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						if (msg.arg1 == RESULT_OK) {
							
							// Write to internal file and disable button to prevent further api calls
//							Log.i(tag, msg.obj.toString());
				       
					        String detailFileName = movieList.get(position).movieName.toString() + 
					        	getResources().getString(R.string.detail_file_name);
					        
					        //
					        File checkForFile = getBaseContext().getFileStreamPath(detailFileName);
					        if (!checkForFile.exists()) {
					        	fileManager.writeToFile(context, detailFileName, msg.obj.toString());
					        }
					        
//					        String fileContent = fileManager.readFromFile(context, detailFileName);
//					        parseData(fileContent.toString(), "movieDetails");
//					        fileNameModifier = fileNameModifier + 1;
					        
							// Check for saved file and parse if available
					        
					        if (checkForFile.exists()) {
					        	String fileContent = fileManager.readFromFile(context, detailFileName);
					        	if (!fileContent.isEmpty()) {
					        		if (parseData(fileContent.toString(), "movieDetails")) {
					        			
					        			Log.i(tag, "Movie Details Parsed");
					        			Log.i(tag, movieDetails.toString());
					        			
					        			for (int k = 0; k < movieDetails.size(); k++) {
					        				if (movieDetails.get(k).detailTitle.toString()
					        						.matches(movieList.get(position).movieName.toString())) {
					        					
					        					Log.i(tag, movieDetails.get(k)
										        		.detailTitle.toString());
					        					
										        // Start Detail Activity
					        					
										        Intent detailIntent = new Intent(context, DetailActivity.class);
										        detailIntent.putExtra("title", movieDetails.get(k)
										        		.detailTitle.toString());
										        detailIntent.putExtra("year", movieDetails.get(k)
										        		.detailYear.toString());
										        detailIntent.putExtra("rated", movieDetails.get(k)
										        		.detailRated.toString());
										        detailIntent.putExtra("released", movieDetails.get(k)
										        		.detailReleased.toString());
										        detailIntent.putExtra("runtime", movieDetails.get(k)
										        		.detailRuntime.toString());
										        detailIntent.putExtra("genre", movieDetails.get(k)
										        		.detailGenre.toString());
										        detailIntent.putExtra("director", movieDetails.get(k)
										        		.detailDirector.toString());
										        detailIntent.putExtra("actors", movieDetails.get(k)
										        		.detailActors.toString());
										        detailIntent.putExtra("plot", movieDetails.get(k)
										        		.detailPlot.toString());
										        detailIntent.putExtra("awards", movieDetails.get(k)
										        		.detailAwards.toString());
										        detailIntent.putExtra("image", movieDetails.get(k)
										        		.detailImage.toString());
										        detailIntent.putExtra("score", movieDetails.get(k)
										        		.detailScore.toString());
										        startActivityForResult(detailIntent, 0);
					        				}
					        			}
					        		}
					        	}
					        }
						}
					}
				};
				
				// Network connection check
				if (checkNetworkConnection(context)) {
					// Set boolean to determine parse method details
					Messenger apiMessenger = new Messenger(apiHandler);
						
					// Properly format URL
					myURL = getResources().getString(R.string.pre_detail_api) + movieList.get(position).movieID.toString()
							+ getResources().getString(R.string.post_detail_api);
					myURL = myURL.replace("_", "&");
							
					// Start intent service
					Intent startApiIntent = new Intent(context, ApiService.class);
					startApiIntent.putExtra(ApiService.MESSENGER_KEY, apiMessenger);
					startApiIntent.putExtra(ApiService.INPUT_KEY, myURL);
					startService(startApiIntent);
					showNotfication("details");
				} else {
					showNotfication("connection");
				}
			}

		});
        
		// Check for saved file and parse if available
        File checkForFile = getBaseContext().getFileStreamPath(externalFileName);
        if (checkForFile.exists()) {
        	findButton.setEnabled(false);
        	String fileContent = fileManager.readFromFile(context, externalFileName);
//        	Log.i(tag, fileContent.toString());
        	if (!fileContent.isEmpty()) {
        		parseData(fileContent.toString(), "movieList");
        	}
        	for (Integer q = 0; q < 10; q++) {
        		String detailFileName = q.toString() + getResources().getString(R.string.detail_file_name);
        		File checkForDetailsFile = getBaseContext().getFileStreamPath(detailFileName);
        		if (checkForDetailsFile.exists()) {
        			String detailFileContent = fileManager.readFromFile(context, detailFileName);
        			if (!detailFileContent.isEmpty()) {
        				parseData(detailFileContent.toString(), "movieDetails");
        			}
        		}
        	}
        } else {
        	findButton.setEnabled(true);
        }
        
        findButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (searchField.getText().toString().matches("")) {
					showNotfication("input");
				} else {
					
					// apiService Intent, Handler, and Start
					apiHandler = new Handler() {

						@Override
						public void handleMessage(Message msg) {
							// TODO Auto-generated method stub
							if (msg.arg1 == RESULT_OK) {
								
								// Write to internal file and disable button to prevent further api calls
//								Log.i(tag, msg.obj.toString());
					        	findButton.setEnabled(false);
					        	
					        	// Read and parse date from internal file
					        	fileManager.writeToFile(context, externalFileName, msg.obj.toString());
					        	String fileContent = fileManager.readFromFile(context, externalFileName);
					        	parseData(fileContent.toString(), "movieList");
							}
						}
					};
					
					// Network connection check
					if (checkNetworkConnection(context)) {
						// Set boolean to determine parse method details
						Messenger apiMessenger = new Messenger(apiHandler);
							
						// Properly format URL
						myURL = getResources().getString(R.string.main_api);
						myURL = myURL.replace("_", "&");
								
						// Start intent service
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
    
	// User Notifications
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
    	if (myNotification == "details") {
    		Toast.makeText(this, R.string.details_notification, Toast.LENGTH_LONG).show();
    	}
    }
	
	// Get and Parse JSON Function... (apiData = Raw JSON code)(apiType = specifies the type of parsing code)
	public Boolean parseData(String apiData, String apiType) {

		Boolean completed = false;
		String jsonString = null;
		if (apiData != null) {
			jsonString = apiData;
		}

//		String type = null;
		// Parse JSON
		if (apiType.matches("movieList")) {
//			type = "movieList";
			try {
				// Creating JSONObject from String
					JSONObject mainObject = new JSONObject(jsonString);
					JSONArray subObject = mainObject.getJSONArray("Search");
//					Log.i(tag, subObject.toString());

					for (int i = 0; i < subObject.length(); i ++) {
						JSONObject movieObject = subObject.getJSONObject(i);
						// Class Specific Data
						
						String thisName = movieObject.getString("Title");
						String thisYear = movieObject.getString("Year");
						String thisType = movieObject.getString("Type");
						String thisID = movieObject.getString("imdbID");
/*						Log.i(tag, thisName);
						Log.i(tag, thisYear);
						Log.i(tag, thisType);
						Log.i(tag, thisID);*/
						
						// Not Applicable
						String detailName = na;
						String detailYear = na;
						String detailRated = na;
						String detailReleased = na;
						String detailRuntime = na;
						String detailGenre = na;
						String detailDirector = na;
						String detailActors = na;
						String detailPlot = na;
						String detailAwards = na;
						String detailImage = na;
						String detailScore = na;

						// Save Data Here
						setClass(apiType, thisName, thisYear, thisType, thisID, detailName, detailYear, detailRated
								, detailReleased, detailRuntime, detailGenre, detailDirector, detailActors
								, detailPlot, detailAwards, detailImage, detailScore);
					}
					listAdapter.notifyDataSetChanged();
					completed = true;
			} 
			catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e(tag, e.getMessage().toString());
				Log.i(tag, "Something went wrong parsing movieList");
				e.printStackTrace();
			}
		}
		if (apiType.matches("movieDetails")) {
//			type = "movieDetails";
			try {
				
				// Creating JSONObject from String
					JSONObject mainObject = new JSONObject(jsonString);

//					Log.i(tag, mainObject.toString());
					
						// Class Specific Data
						String detailName = mainObject.getString("Title");
						String detailYear = mainObject.getString("Year");
						String detailRated = mainObject.getString("Rated");
						String detailReleased = mainObject.getString("Released");
						String detailRuntime = mainObject.getString("Runtime");
						String detailGenre = mainObject.getString("Genre");
						String detailDirector = mainObject.getString("Director");
						String detailActors = mainObject.getString("Actors");
						String detailPlot = mainObject.getString("Plot");
						String detailAwards = mainObject.getString("Awards");
						String detailImage = mainObject.getString("Poster");
						String detailScore = mainObject.getString("Metascore");
/*						Log.i(tag, detailName);
						Log.i(tag, detailYear);
						Log.i(tag, detailRated);
						Log.i(tag, detailReleased);
						Log.i(tag, detailRuntime);
						Log.i(tag, detailGenre);
						Log.i(tag, detailDirector);
						Log.i(tag, detailActors);
						Log.i(tag, detailPlot);
						Log.i(tag, detailAwards);
						Log.i(tag, detailImage);
						Log.i(tag, detailScore);*/
						
						// Not Applicable
						String thisName = na;
						String thisYear = na;
						String thisType = na;
						String thisID = na;

						// Save Data Here
						setClass(apiType, thisName, thisYear, thisType, thisID, detailName, detailYear, detailRated
								, detailReleased, detailRuntime, detailGenre, detailDirector, detailActors
								, detailPlot, detailAwards, detailImage, detailScore);
					completed = true;
			} 
			catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e(tag, e.getMessage().toString());
				Log.i(tag, "Something went wrong parsing movieDetails");
				e.printStackTrace();
			}
		}
		return completed;
	}

	// Save Parsed Data to Class
	public void setClass(String type, String name, String year, String movieType, String ID, String detailTitle, String detailYear,
			String detailRated, String detailReleased, String detailRuntime, String detailGenre, String detailDirector,
			String detailActors, String detailPlot, String detailAwards, String detailImage, String detailScore) {
		if (type.matches("movieList")) {
			MovieListItems newMovie = new MovieListItems(name, year, movieType, ID);
			movieList.add(newMovie);
//			Log.i(tag, ID.toString());
//			Log.i(tag, movieList.get(0).movieID.toString());
//			Log.i(tag, newMovie.toString());
		}
		if (type.matches("movieDetails")) {
			MovieDetails newMovieDetails = new MovieDetails(detailTitle, detailYear,
					detailRated, detailReleased, detailRuntime, detailGenre, detailDirector,
					detailActors, detailPlot, detailAwards, detailImage, detailScore);
//			Log.i(tag, newMovieDetails.toString());
			movieDetails.add(newMovieDetails);
//			Log.i(tag, newMovieDetails.toString());
		}

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
/*    			Log.i(tag, "Connection Type: " + networkInfomation.getTypeName()
    					.toString());*/
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
		
//			Log.i(tag, "Custom Adapter Hit");

			if (customItemView == null) {
				LayoutInflater viewInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				customItemView = viewInflater.inflate(R.layout.custom_list_item, parent, false);
			}

			// Set Values for UI elements in custom cell
			TextView name = (TextView)customItemView.findViewById(R.id.movieTitle);
			TextView year = (TextView)customItemView.findViewById(R.id.movieYear);
			TextView type = (TextView)customItemView.findViewById(R.id.movieType);

			name.setText(movieList.get(position).movieName.toString());
			year.setText(movieList.get(position).movieYear.toString());
			type.setText(movieList.get(position).movieType.toString());

			return customItemView;
		}
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.i(tag, "onActivityResult has been called");
    	
    	if (resultCode == RESULT_OK && requestCode == 0) {
    		
    		Log.i(tag, "Result and Request are true");
    		
    		if (data.hasExtra("title") && data.hasExtra("fav")) {
    			
    			Log.i(tag, "Extras are not null");
    			
    			String title = data.getStringExtra("title");
    			Float favValue = data.getFloatExtra("fav", 0);

    			String favStr = favValue.toString();
    			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
    			dialogBuilder.setTitle(R.string.alert_title);
    			
    			String dialogMessage = "";
    			
    			if (favValue == 1.0) {
        			dialogMessage = title + " " + getResources().getString(R.string.alert_favMovie) + "\r\n" + 
        					getResources().getString(R.string.alert_favRating) + " " + favStr;
    			} else {
    				dialogMessage = title + " " + getResources().getString(R.string.alert_notFavMovie) + "\r\n" + 
        					getResources().getString(R.string.alert_favRating) + " " + favStr;
    			}
    			
    			dialogBuilder.setMessage(dialogMessage);
    			AlertDialog alertDialog = dialogBuilder.create();
    			alertDialog.show();

    		}
    	}
    }
}
