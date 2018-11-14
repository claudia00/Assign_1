package com.example.logan.movieapp.models;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie {
    private int id;
    private String posterPath, title, overView;
    private double rating;
    public static String POSTER_PATH = "POSTER_PATH";
    public static String TITLE = "TITLE";
    public static final String OVERVIEW = "OVERVIEW";

    //empty constructor needed by the parcel library
    public Movie() {
    }

    public Movie(JSONObject jsonObject) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overView = jsonObject.getString("overview");
        rating = jsonObject.getDouble("vote_average");
        id = jsonObject.getInt("id");
    }

    public Movie(Bundle bundle) {
        this.posterPath = bundle.getString(POSTER_PATH);
        this.title = bundle.getString(TITLE);
        this.overView = bundle.getString(OVERVIEW);
    }

    //This method takes a JsonArray
    // and iterates through it
    // and generates a list of movies
    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s", posterPath);
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(POSTER_PATH, posterPath);
        bundle.putString(TITLE, title);
        bundle.putString(OVERVIEW, overView);
        return bundle;
    }
}
