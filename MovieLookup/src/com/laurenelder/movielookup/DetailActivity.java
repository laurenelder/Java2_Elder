/*
 * Project:			MovieLookup
 * Package:			com.laurenelder.movielookup
 * Author:			Devin "Lauren" Elder
 * Date:			Jul 17, 2014
 * Class:			Java 2 Term 1407
 */

package com.laurenelder.movielookup;

import com.laurenelder.movielookup.MainActivity.PlaceholderFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

public class DetailActivity extends Activity implements DetailsFragment.OnSelected{
	
	// Set Global Variables
	static String tag = "DETAILSACTIVITY";
	FragmentManager fragDetailManag;
	DetailsFragment DetailFrag;
/*	ImageView image;
	TextView titleTxt;
	TextView yearTxt;
	TextView directorTxt;
	TextView ratedTxt;
	TextView runtimeTxt;
	TextView genreTxt;
	TextView actorTxt;
	TextView awardTxt;
	TextView scoreTxt;
	TextView plotTxt;
	RatingBar rBar;*/
	Bitmap moviePoster;
	Intent intent;
	
	String TITLE;
	String IMAGEURL;
	Float FAV;
	String YEAR;
	String DIRECTOR;
	String RATED;
	String RUNTIME;
	String GENRE;
	String ACTORS;
	String AWARDS;
	String SCORE;
	String PLOT;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_details);
		
		
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
		
		intent = this.getIntent();
		
		fragDetailManag = getFragmentManager();
		DetailFrag = (DetailsFragment)fragDetailManag.findFragmentById(R.id.fragment2);
		if(DetailFrag == null) {
			DetailFrag = new DetailsFragment();
		}
		
/*		// Set UI Elements to variables
		titleTxt = (TextView)findViewById(R.id.movieTitle);
		yearTxt = (TextView)findViewById(R.id.movieYear);
		directorTxt = (TextView)findViewById(R.id.movieDirector);
		ratedTxt = (TextView)findViewById(R.id.movieRating);
		runtimeTxt = (TextView)findViewById(R.id.movieRuntime);
		genreTxt = (TextView)findViewById(R.id.movieGenre);
		actorTxt = (TextView)findViewById(R.id.movieActor);
		awardTxt = (TextView)findViewById(R.id.movieAwards);
		scoreTxt = (TextView)findViewById(R.id.movieScore);
		plotTxt = (TextView)findViewById(R.id.moviePlot);
		image = (ImageView)findViewById(R.id.movieImage);
		rBar = (RatingBar)findViewById(R.id.ratingBar);*/
		
		// Get all extras from intent and set to global variables
		TITLE = intent.getExtras().getString("title");
		IMAGEURL = intent.getExtras().getString("image");
		FAV = (float) 0.0;
		YEAR = intent.getExtras().getString("year");
		DIRECTOR = intent.getExtras().getString("director");
		RATED = intent.getExtras().getString("rated");
		RUNTIME = intent.getExtras().getString("runtime");
		GENRE = intent.getExtras().getString("genre");
		ACTORS = intent.getExtras().getString("actors");
		AWARDS = intent.getExtras().getString("awards");
		SCORE = intent.getExtras().getString("score");
		PLOT = intent.getExtras().getString("plot");
		
/*		// Call Async Task to load movie image
		new downloadImage().execute(IMAGEURL);*/
		
/*		// Set UI elements from global variables
		titleTxt.setText(TITLE);
		yearTxt.setText(YEAR);
		directorTxt.setText(DIRECTOR);
		ratedTxt.setText(RATED);
		runtimeTxt.setText(RUNTIME);
		genreTxt.setText(GENRE);
		actorTxt.setText(ACTORS);
		awardTxt.setText(AWARDS);
		scoreTxt.setText(SCORE);
		plotTxt.setText(PLOT);
		
		// Set OnChangeListener for rating bar
		rBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
				// Set Global variable when the rating bar is selected
				FAV = rBar.getRating();
			}
			
		});
		
		// Set OnClickListener for image to use as button to call web service implicit intent
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Log.i(tag, "User Clicked on Image!!");
				Uri website = Uri.parse(IMAGEURL);
				Intent websiteIntent = new Intent(Intent.ACTION_VIEW, website);
				startActivity(websiteIntent);
			}
		});*/
		

		DetailFrag.getStoredData(TITLE, YEAR, DIRECTOR, RATED, RUNTIME, 
				GENRE, ACTORS, AWARDS, SCORE, PLOT, IMAGEURL);
	}
	
	public void setRating(float myRating) {
		FAV = myRating;
	}
	
	public void onClickImage() {
		Uri website = Uri.parse(IMAGEURL);
		Intent websiteIntent = new Intent(Intent.ACTION_VIEW, website);
		startActivity(websiteIntent);
	}

/*	// Async Task to fetch image from url and set ImageView on post execute
	private class downloadImage extends AsyncTask<String, Void, Bitmap> {

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap moviePoster = null;
			try {
				InputStream stream = new java.net.URL(urldisplay).openStream();
				moviePoster = BitmapFactory.decodeStream(stream);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return moviePoster;
		}

		protected void onPostExecute(Bitmap result) {
			image.setImageBitmap(result);
		}
	} */
	
	/* Override methods
	 * Finish method to pass favorite data back to main activity
	 * onBackPressed method to override base android back button and call finish method
	 * Saved instance state to handle save and refresh of all global variables used in UI elements
	 */
	@Override
	public void finish() {
	  // Prepare data intent 
	  Intent data = new Intent();
	  data.putExtra("fav", FAV);
	  data.putExtra("title", TITLE);
	  setResult(RESULT_OK, data);
	  super.finish();
	} 
	
	@Override public void onBackPressed() { 
		super.onBackPressed(); 
		finish(); 
		}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  // Save UI state changes to the savedInstanceState.
	  savedInstanceState.putString("TITLE", TITLE);
	  savedInstanceState.putString("IMAGE", IMAGEURL);
	  savedInstanceState.putFloat("FAV", FAV);
	  savedInstanceState.putString("YEAR", YEAR);
	  savedInstanceState.putString("DIRECTOR", DIRECTOR);
	  savedInstanceState.putString("RATED", RATED);
	  savedInstanceState.putString("RUNTIME", RUNTIME);
	  savedInstanceState.putString("GENRE", GENRE);
	  savedInstanceState.putString("ACTORS", ACTORS);
	  savedInstanceState.putString("AWARDS", AWARDS);
	  savedInstanceState.putString("SCORE", SCORE);
	  savedInstanceState.putString("PLOT", PLOT);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	    // read previously saved data
		TITLE = savedInstanceState.getString("TITLE");
		IMAGEURL = savedInstanceState.getString("IMAGE");
		FAV = savedInstanceState.getFloat("FAV");
		YEAR = savedInstanceState.getString("YEAR");
		DIRECTOR = savedInstanceState.getString("DIRECTOR");
		RATED = savedInstanceState.getString("RATED");
		RUNTIME = savedInstanceState.getString("RUNTIME");
		GENRE = savedInstanceState.getString("GENRE");
		ACTORS = savedInstanceState.getString("ACTORS");
		AWARDS = savedInstanceState.getString("AWARDS");
		SCORE = savedInstanceState.getString("SCORE");
		PLOT = savedInstanceState.getString("PLOT");
	}
}
