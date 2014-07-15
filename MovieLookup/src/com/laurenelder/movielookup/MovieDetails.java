package com.laurenelder.movielookup;

public class MovieDetails {
	
	String detailTitle;
	String detailYear;
	String detailRated;
	String detailReleased;
	String detailRuntime;
	String detailGenre;
	String detailDirector;
	String detailActors;
	String detailPlot;
	String detailAwards;
	String detailImage;
	String detailScore;
	
	public MovieDetails(String title, String year,
			String rated, String released, String runtime, String genre, String director,
			String actors, String plot, String awards, String image, String score) {
		
		this.detailTitle = title;
		this.detailYear = year;
		this.detailRated = rated;
		this.detailReleased = released;
		this.detailRuntime = runtime;
		this.detailGenre = genre;
		this.detailDirector = director;
		this.detailActors = actors;
		this.detailPlot = plot;
		this.detailAwards = awards;
		this.detailImage = image;
		this.detailScore = score;
	}
	
	public String toString() {
		return detailTitle;
	}
}
