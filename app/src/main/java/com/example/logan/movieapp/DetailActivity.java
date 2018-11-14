package com.example.logan.movieapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.logan.movieapp.adapters.MoviesAdapter;
import com.example.logan.movieapp.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class DetailActivity extends YouTubeBaseActivity {
    private TextView tvTitle, tvOverView;
    private RatingBar ratingBar;
    private Movie movie;
    private static final String YOUTUBE_API_KEY = "AIzaSyDDrbsueEz0DWfF5z5A2L0s8Y3IKVkf9Mg";
    public static final String TRAILIER_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tvTitle = findViewById(R.id.title_detail);
        tvOverView = findViewById(R.id.tv_overview_dt);
        ratingBar = findViewById(R.id.ratingBar);
        youTubePlayerView = findViewById(R.id.player);
//        Bundle bundle = getIntent().getBundleExtra(MoviesAdapter.MVBUNDLE);
//        Movie movie = new Movie(bundle);

        // get the parcel object, unwrap it, and then cast it to a movie object
        movie = Parcels.unwrap(getIntent().getParcelableExtra(MoviesAdapter.MVBUNDLE));
        tvTitle.setText(movie.getTitle());
        tvOverView.setText(movie.getOverView());
        ratingBar.setRating((float) movie.getRating());

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(TRAILIER_URL, movie.getId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    if (results.length() == 0) {
                        //should show some images instead over here
                        return;
                    }
                    JSONObject movieTrailer = results.getJSONObject(0);
                    String trailerType = movieTrailer.getString("site");
                    if (!trailerType.equalsIgnoreCase("YouTube")) {
                        //should show some images instead over here
                        return;
                    }
                    String youtubeKey = movieTrailer.getString("key");
                    initiliazeYoutube(youtubeKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void initiliazeYoutube(String youtubeKey) {
        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo(youtubeKey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(DetailActivity.this, "Failured to Initialize!", Toast.LENGTH_LONG).show();
                Log.d("SMILE", "onInitializationFailure: ");
            }
        });
    }
}
