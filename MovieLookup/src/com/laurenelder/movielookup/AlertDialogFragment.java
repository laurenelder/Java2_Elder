/*
 * Project:			MovieLookup
 * Package:			com.laurenelder.movielookup
 * Author:			Devin "Lauren" Elder
 * Date:			Jul 31, 2014
 * Class:			Java 2 Term 1407
 */

package com.laurenelder.movielookup;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class AlertDialogFragment extends DialogFragment{
	
	// Set Global Variables
	public static DialogType type;
	public static ArrayList<String> fList;
	public static ArrayList<String> mList;
	public static ArrayList<String> yList;
	public static Context context;
	public enum DialogType {
		FAVORITES,
		SETTINGS,
		SEARCH
	}
	
	// Establish Instance of AlertDialogFragmentClass
	static AlertDialogFragment newInstance(Context appContext, DialogType dialogType, ArrayList<String> movies, 
			ArrayList<String> fullMovies, ArrayList<String> years) {
		type = dialogType;
		fList = movies;
		mList = fullMovies;
		yList = years;
		context = appContext;
		return new AlertDialogFragment();
	}

	// Build and Display the AlertDialogFragment
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		final LayoutInflater dialogInflater = getActivity().getLayoutInflater();
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		switch(type) {
		case FAVORITES:
			Log.i("AlertFrag", mList.toString());
			View favView = dialogInflater.inflate(R.layout.favorites_dialog, null);
			dialogBuilder.setView(favView)
			.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					AlertDialogFragment.this.getDialog().cancel();
				}
			});
			
			// Set up list and adapter
			ListView favList = (ListView)favView.findViewById(R.id.favList);
			ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, fList);
			favList.setAdapter(listAdapter);
			break;
		case SETTINGS:
			View settingsView = dialogInflater.inflate(R.layout.settings_dialog, null);
			dialogBuilder.setView(settingsView)
			.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					AlertDialogFragment.this.getDialog().cancel();
				}
			});
			
			ArrayList<String> backgroundList = new ArrayList<String>();
			backgroundList.add("Background 1");
			backgroundList.add("Background 2");
			backgroundList.add("Background 3");
			backgroundList.add("Background 4");
			backgroundList.add("Background 5");
			
			Spinner settingsSpinner = (Spinner)settingsView.findViewById(R.id.settingsSpinner);
			ArrayAdapter<String> settingsAdapter = new ArrayAdapter<String>
				(context, android.R.layout.simple_spinner_item, backgroundList);
			settingsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			settingsSpinner.setAdapter(settingsAdapter);
			settingsSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					
					// TODO Auto-generated method stub
					if (position == 0) {
						prefs.edit().putString("background", "blue_and_green").apply();
					}
					if (position == 1) {
						prefs.edit().putString("background", "bright_star").apply();
					}
					if (position == 2) {
						prefs.edit().putString("background", "faint_stars").apply();
					}
					if (position == 3) {
						prefs.edit().putString("background", "nebula").apply();
					}
					if (position == 4) {
						prefs.edit().putString("background", "planets_and_nebula").apply();
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			break;
		case SEARCH:
			View searchView = dialogInflater.inflate(R.layout.search_dialog, null);
			dialogBuilder.setView(searchView)
			.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					AlertDialogFragment.this.getDialog().cancel();
				}
			});
			
			final ArrayList<String> filteredMovies = new ArrayList<String>();
			
			final EditText inputText = (EditText)searchView.findViewById(R.id.searchDialogField);
			final Button inputButton = (Button)searchView.findViewById(R.id.searchDialogButton);
			final ListView searchList = (ListView)searchView.findViewById(R.id.searchList);
			

			
			final ArrayAdapter<String> searchListAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, filteredMovies);
			searchList.setAdapter(searchListAdapter);
			
			inputButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					filteredMovies.removeAll(filteredMovies);
					
					for (int a = 0; a < yList.size(); a++) {
						if (yList.get(a).toString().matches(inputText.getText().toString())) {
							filteredMovies.add(mList.get(a).toString());
							
						}
					}
					
					if(filteredMovies.isEmpty()) {
						filteredMovies.add("No Results Found");
					}
					
					searchListAdapter.notifyDataSetChanged();
				}
				
			});
			
			break;
		}
		
		return dialogBuilder.create();
	}
	
}
