/*
 * Project:			MovieLookup
 * Package:			com.laurenelder.movielookup
 * Author:			Devin "Lauren" Elder
 * Date:			Jul 24, 2014
 * Class:			Java 2 Term 1407
 */

package com.laurenelder.movielookup;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
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

public class MainFragment extends Fragment implements OnClickListener, OnItemClickListener {
	
	// Global Variables
	EditText searchField;
	Button findButton;
	ListView listView;
	Context context;
	ArrayAdapter<MovieListItems> listAdapter;
	
	// Interface to MainActivity methods
	public interface OnSelected {
		public boolean onFileCheck();
		public void onListViewClick(final int position);
		public boolean onButtonClick(String inputSTR);
	}
	
	private OnSelected parentActivity;
	
	// Class List to access movieList Objects
	List<MovieListItems> movieList = new ArrayList<MovieListItems>();

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		context = getActivity();
		
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
		
		View view = inflater.inflate(R.layout.activity_main, container);
		
        searchField = (EditText)view.findViewById(R.id.searchField);
        findButton = (Button)view.findViewById(R.id.findButton);
        listView = (ListView)view.findViewById(R.id.list);

    	// ListView Adapter Code 
		listAdapter = new customListAdapter();
        ListView listView = (ListView)view.findViewById(R.id.list);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(this);

		// Set onClickListener for search button
        findButton.setOnClickListener(this);
		
		return view;
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

			if(movieList != null) {
				name.setText(movieList.get(position).movieName.toString());
				year.setText(movieList.get(position).movieYear.toString());
				type.setText(movieList.get(position).movieType.toString());
			}

			return customItemView;
		}
    }
    
/*    public boolean checkForFile() {
    	
    }*/

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(parentActivity.onButtonClick(searchField.getText().toString())) {
			findButton.setEnabled(false);
			listAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		parentActivity.onListViewClick(position);
	}

	public void setObjects(String name,String year,String movieType,String ID) {
		MovieListItems newMovie = new MovieListItems(name, year, movieType, ID);
		movieList.add(newMovie);
//		findButton.setEnabled(false);
		listAdapter.notifyDataSetChanged();
	}
	
	public void setButtonFalse() {
		findButton.setEnabled(false);	
	}

}
