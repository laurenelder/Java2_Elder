package com.laurenelder.movielookup;

import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends Activity {
	
	static String tag = "DETAILSACTIVITY";
	ImageView image;
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
	String imageURL;
	Bitmap moviePoster;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.detail_layout);
		super.onCreate(savedInstanceState);
		
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
		
		imageURL = getIntent().getExtras().getString("image");
		Log.i(tag, imageURL.toString());
		
		new downloadImage().execute(imageURL);
		
		titleTxt.setText(getIntent().getExtras().getString("title"));
		yearTxt.setText(getIntent().getExtras().getString("year"));
		directorTxt.setText(getIntent().getExtras().getString("director"));
		ratedTxt.setText(getIntent().getExtras().getString("rated"));
		runtimeTxt.setText(getIntent().getExtras().getString("runtime"));
		genreTxt.setText(getIntent().getExtras().getString("genre"));
		actorTxt.setText(getIntent().getExtras().getString("actors"));
		awardTxt.setText(getIntent().getExtras().getString("awards"));
		scoreTxt.setText(getIntent().getExtras().getString("score"));
		plotTxt.setText(getIntent().getExtras().getString("plot"));
		
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(tag, "User Clicked on Image!!");
				Uri website = Uri.parse(imageURL);
				Intent websiteIntent = new Intent(Intent.ACTION_VIEW, website);
				startActivity(websiteIntent);
			}
		});
	}

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
	}
}
