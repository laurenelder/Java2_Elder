/*
 * Project:			MovieLookup
 * Package:			com.laurenelder.movielookup
 * Author:			Devin "Lauren" Elder
 * Date:			Jul 17, 2014
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
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity implements MainFragment.OnSelected {
	
	// Global Variables
	FileManager fileManager;
	static String tag = "MAINACTIVITY";
	static Context context;
	String externalFileName;
	static Handler apiHandler = null;
	boolean detailApi = false;
	boolean dataAvailable = false;
	FragmentManager fragManag;
	MainFragment frag;
	String myURL;
	String na;
	
	// Class List to access movieList Objects
	List<MovieListItems> movieList = new ArrayList<MovieListItems>();
	List<MovieDetails> movieDetails = new ArrayList<MovieDetails>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        
        // Set Variables after UI has Loaded
        context = this;
        fileManager = FileManager.getInstance();
        externalFileName = getResources().getString(R.string.file_name);
        na = getResources().getString(R.string.n_a);
		
//		onFileCheck();
		fragManag = getFragmentManager();
		frag = (MainFragment)fragManag.findFragmentById(R.id.fragment1);
		if(frag == null) {
			frag = new MainFragment();
		}
/*		if (onFileCheck()) {
			frag.setButton(false);
		}*/
		onFileCheck();
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

/*        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_main, container, false);
            return rootView;
        }*/
    }
    
    /* OnFileCheck method checks to see if local file is available and returns boolean depending on if
     * it is available or not... This boolean is then used in the fragment class to the "clickable" attribute
     * of the search button... If file is available the parse method is then called.
     */
    
    public boolean onFileCheck() {
    	boolean fileAvailable = false;
    	
		// Check for saved file and parse if available
        File checkForFile = getBaseContext().getFileStreamPath(externalFileName);
        if (checkForFile.exists()) {
//        	findButton.setEnabled(false);
        	String fileContent = fileManager.readFromFile(context, externalFileName);
        	Log.i(tag, "File Check is Running");
        	if (!fileContent.isEmpty()) {
        		parseData(fileContent.toString(), "movieList");
        	}
        	for (Integer q = 0; q < 10; q++) {
        		String detailFileName = q.toString() + getResources().getString(R.string.detail_file_name);
        		File checkForDetailsFile = getBaseContext().getFileStreamPath(detailFileName);
        		if (checkForDetailsFile.exists()) {
        			String detailFileContent = fileManager.readFromFile(context, detailFileName);
        			if (!detailFileContent.isEmpty()) {
        				if(parseData(detailFileContent.toString(), "movieDetails")) {
        					fileAvailable = true;
        				}
        			}
        		}
        	}
        	frag.setButtonFalse();
        } else {
//        	findButton.setEnabled(true);
        	fileAvailable = false;
        }
        
        return fileAvailable;
    }
    
	/* OnListViewClick method handles click events from the fragment by running API service 
	 * and fileManager Class... After all code is completed without errors the detail activity is
	 * then started.
	 */
	public void onListViewClick(final int position) {
		// Load Movie Details UI here
		apiHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.arg1 == RESULT_OK) {
					
					// Write to internal file and disable button to prevent further api calls
//					Log.i(tag, msg.obj.toString());
		       
			        String detailFileName = movieList.get(position).movieName.toString() + 
			        	getResources().getString(R.string.detail_file_name);
			        
			        // Check for file (if true doesn't write to file)
			        File checkForFile = getBaseContext().getFileStreamPath(detailFileName);
			        if (!checkForFile.exists()) {
			        	fileManager.writeToFile(context, detailFileName, msg.obj.toString());
			        }
			        
					// Check for saved file and parse if available
			        if (checkForFile.exists()) {
			        	String fileContent = fileManager.readFromFile(context, detailFileName);
			        	if (!fileContent.isEmpty()) {
			        		if (parseData(fileContent.toString(), "movieDetails")) {
			        			
//			        			Log.i(tag, "Movie Details Parsed");
//			        			Log.i(tag, movieDetails.toString());
			        			
			        			// Loop through objects to pull correct data and pass to detail activity
			        			for (int k = 0; k < movieDetails.size(); k++) {
			        				if (movieDetails.get(k).detailTitle.toString()
			        						.matches(movieList.get(position).movieName.toString())) {
			        					
/*					        					Log.i(tag, movieDetails.get(k)
								        		.detailTitle.toString());*/
			        					
								        // Start Detail Activity with extras for detail activity UI
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
					
			// Start intent service with movie details url
			Intent startApiIntent = new Intent(context, ApiService.class);
			startApiIntent.putExtra(ApiService.MESSENGER_KEY, apiMessenger);
			startApiIntent.putExtra(ApiService.INPUT_KEY, myURL);
			startService(startApiIntent);
			showNotfication("details");
		} else {
			showNotfication("connection");
		}
	}
	
	/* OnButtonClick method runs when the search button is clicked and is called from the mainFragment...
	 * This method runs the API service, writes the results to a local file, reads the local file, and
	 * calls the parse method to save the data to a custom object class. This method also returns a boolean
	 * so the search button attribute "clickable" can be set when the data is saved to local file.
	 */
	
	public boolean onButtonClick(String inputSTR) {
		
		dataAvailable = false;
		
		if (inputSTR.matches("")) {
			showNotfication("input");
		} else {
			
			// apiService Intent, Handler, and Start
			apiHandler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					if (msg.arg1 == RESULT_OK) {
						
						// Write to internal file and disable button to prevent further api calls
//						Log.i(tag, msg.obj.toString());
//			        	findButton.setEnabled(false);
			        	dataAvailable = true;
			        	
			        	// Read and parse date from internal file
			        	fileManager.writeToFile(context, externalFileName, msg.obj.toString());
			        	onFileCheck();
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
		
		return dataAvailable;
	}
    
	// User Notifications
	private void showNotfication(String myNotification) {
    	
    	/* Set Alert Text based on Error
    	 * Input error shows if EditText is empty
    	 * Connection error shows if there isn't a valid network connection
    	 * Searching notification shows when the API service is called
    	 * Details notification shows when detail activity is called
    	 */
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

		// Parse JSON
		if (apiType.matches("movieList")) {
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
//					listAdapter.notifyDataSetChanged();
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
			try {
				
				// Creating JSONObject from String
				JSONObject mainObject = new JSONObject(jsonString);

//				Log.i(tag, mainObject.toString());

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
				
/*				Log.i(tag, detailName);
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
			frag.setObjects(name, year, movieType, ID);
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
	
    // Check Network Connection Method and return false if there is no valid network connection
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
    
    // onActivityResult is called when detail activity is closed and displays alert dialog with favorite information
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.i(tag, "onActivityResult has been called");
    	
    	// Clear Movie Details
    	movieDetails.removeAll(movieDetails);
    	
    	if (resultCode == RESULT_OK && requestCode == 0) {
    		
    		Log.i(tag, "Result and Request are true");
    		
    		if (data.hasExtra("title") && data.hasExtra("fav")) {
    			
    			Log.i(tag, "Extras are not null");
    			
    			// Get data and construct dialog
    			String title = data.getStringExtra("title");
    			Float favValue = data.getFloatExtra("fav", 0);

    			String favStr = favValue.toString();
    			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
    			dialogBuilder.setTitle(R.string.alert_title);
    			
    			String dialogMessage = "";
    			
    			if (favValue > 2.0) {
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
