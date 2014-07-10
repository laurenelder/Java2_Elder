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
