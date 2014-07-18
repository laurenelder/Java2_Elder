/*
 * Project:			MovieLookup
 * Package:			com.laurenelder.movielookup
 * Author:			Devin "Lauren" Elder
 * Date:			Jul 17, 2014
 * Class:			Java 2 Term 1407
 */

// Movie Objects to be used to populate listView
package com.laurenelder.movielookup;

public class MovieListItems {

	public String movieName;
	public String movieYear;
	public String movieType;
	public String movieID;
	
	public MovieListItems(String thisName, String thisYear, String thisType, String thisID) {
		this.movieName = thisName;
		this.movieYear = thisYear;
		this.movieType = thisType;
		this.movieID = thisID;
	}
	
	public String toString() {
		return movieName;
	}
}
