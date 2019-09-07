package com.example.tanushree.popularmovies.controller;

import android.util.Log;

import com.example.tanushree.popularmovies.model.Movie;
import com.example.tanushree.popularmovies.model.Review;
import com.example.tanushree.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanushree on 23/02/19.
 */

public final class MoviesJsonUtils
{
    //private static final String TAG = MoviesJsonUtils.class.getSimpleName();

    private static final String KEY_RESULTS = "results";
    private static final String KEY_ID = "id";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_PLOT_SYNOPSIS = "overview";
    private static final String KEY_RATING = "vote_average";
    private static final String KEY_RELEASE_DATE = "release_date";

    // Suppress default constructor for noninstantiability.
    private MoviesJsonUtils()
    {
        throw new AssertionError();
    }

    public static List<Movie> getMovieListFromJson(String movieJsonString) throws
            JSONException
    {
        List<Movie> movieList = new ArrayList<Movie>();

        JSONObject movieJSON = new JSONObject(movieJsonString);

        JSONArray results = movieJSON.getJSONArray(KEY_RESULTS);

        JSONObject movieData;
        int id;
        double rating;
        String original_title, poster_path, release_date, plot_synopsis;
        Movie movie;

        for(int i = 0; i<results.length(); i++)
        {
            movieData = results.getJSONObject(i);
            id = movieData.getInt(KEY_ID);
            original_title = movieData.getString(KEY_ORIGINAL_TITLE);
            poster_path = movieData.getString(KEY_POSTER_PATH);
            plot_synopsis = movieData.getString(KEY_PLOT_SYNOPSIS);
            rating = movieData.getDouble(KEY_RATING);
            release_date = movieData.getString(KEY_RELEASE_DATE);

            /*Log.i(TAG, id+"");
            Log.i(TAG, original_title);
            Log.i(TAG, poster_path);
            Log.i(TAG, plot_synopsis);
            Log.i(TAG, release_date);
            Log.i(TAG, rating+"");*/

            movie = new Movie();
            movie.setId(id);
            movie.setOriginalTitle(original_title);
            movie.setPosterPath(poster_path);
            movie.setPlotSynopsis(plot_synopsis);
            movie.setReleaseDate(release_date);
            movie.setRating(rating);
            movieList.add(movie);
        }

        return movieList;
    }

    public static List<Trailer> getTrailerListFromJson(String movieTrailerJsonString) throws JSONException
    {
        List<Trailer> trailerList = new ArrayList<Trailer>();

        JSONObject movieInfo = new JSONObject(movieTrailerJsonString);

        JSONArray results = movieInfo.getJSONArray("results");

        JSONObject movieTrailerData;
        String trailerKey, trailerName;
        //String trailerSite, trailerType;
        Trailer trailer;

        for(int i=0; i<results.length(); i++)
        {
            movieTrailerData = results.getJSONObject(i);
            trailerKey = movieTrailerData.getString("key");
            trailerName = movieTrailerData.getString("name");
            //trailerSite = movieTrailerData.getString("site");
            //trailerType = movieTrailerData.getString("type");
            /*Log.d(TAG, trailerKey);
            Log.d(TAG, trailerName);
            Log.d(TAG, trailerSite);
            Log.d(TAG, trailerType);*/

            trailer = new Trailer();
            trailer.setKey(trailerKey);
            trailer.setName(trailerName);
            //trailer.setSite(trailerSite);
            //trailer.setType(trailerType);
            trailerList.add(trailer);
        }

        return trailerList;
    }

    public static List<Review> getReviewListFromJson(String movieReviewJsonString) throws JSONException
    {
        List<Review> reviewList = new ArrayList<Review>();

        JSONObject movieInfo = new JSONObject(movieReviewJsonString);

        JSONArray results = movieInfo.getJSONArray("results");

        JSONObject movieReviewData;
        String reviewAuthor, reviewContent;
        Review review;

        for(int i=0; i<results.length(); i++)
        {
            movieReviewData = results.getJSONObject(i);
            reviewAuthor = movieReviewData.getString("author");
            reviewContent = movieReviewData.getString("content");

            review = new Review();
            review.setAuthor(reviewAuthor);
            review.setContent(reviewContent);

            //Log.d(TAG, reviewAuthor);
            //Log.v(TAG, reviewContent);

            reviewList.add(review);
        }
        return reviewList;
    }
}
