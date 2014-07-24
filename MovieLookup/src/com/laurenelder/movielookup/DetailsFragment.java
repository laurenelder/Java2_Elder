/*
 * Project:			MovieLookup
 * Package:			com.laurenelder.movielookup
 * Author:			Devin "Lauren" Elder
 * Date:			Jul 23, 2014
 * Class:			Java 2 Term 1407
 */
package com.laurenelder.movielookup;

import java.io.InputStream;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class DetailsFragment extends Fragment implements OnRatingBarChangeListener, OnClickListener{
	
	static Context context;
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
	RatingBar rBar;
	Bitmap moviePoster;
	String IMAGEURL;
	
	public interface OnSelected {
		public void setRating(float myRating);
		public void onClickImage();
	}
	
	private OnSelected parentActivity;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
//		context = getActivity();
		
		if(activity instanceof OnSelected) {
			parentActivity = (OnSelected) activity;

		} else {
			throw new ClassCastException((activity.toString()) + "Did not impliment onSelected interface");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.detail_layout, container);
		
		titleTxt = (TextView)view.findViewById(R.id.movieTitle);
		yearTxt = (TextView)view.findViewById(R.id.movieYear);
		directorTxt = (TextView)view.findViewById(R.id.movieDirector);
		ratedTxt = (TextView)view.findViewById(R.id.movieRating);
		runtimeTxt = (TextView)view.findViewById(R.id.movieRuntime);
		genreTxt = (TextView)view.findViewById(R.id.movieGenre);
		actorTxt = (TextView)view.findViewById(R.id.movieActor);
		awardTxt = (TextView)view.findViewById(R.id.movieAwards);
		scoreTxt = (TextView)view.findViewById(R.id.movieScore);
		plotTxt = (TextView)view.findViewById(R.id.moviePlot);
		image = (ImageView)view.findViewById(R.id.movieImage);
		rBar = (RatingBar)view.findViewById(R.id.ratingBar);
		
		rBar.setOnRatingBarChangeListener(this);
		image.setOnClickListener(this);
			
		return view;
	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
		// TODO Auto-generated method stub
		parentActivity.setRating(rating);
	}

	public void getStoredData(String title, String year, String director, String rated, String runtime, 
			String genre, String actors, String awards, String score, String plot, String url) {
		
		// Call Async Task to load movie image
		new downloadImage().execute(url);
		
		String IMAGEURL = url;
		titleTxt.setText(title);
		yearTxt.setText(year);
		directorTxt.setText(director);
		ratedTxt.setText(rated);
		runtimeTxt.setText(runtime);
		genreTxt.setText(genre);
		actorTxt.setText(actors);
		awardTxt.setText(awards);
		scoreTxt.setText(score);
		plotTxt.setText(plot);
		
	}
	
	// Async Task to fetch image from url and set ImageView on post execute
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		parentActivity.onClickImage();
	} 
}
