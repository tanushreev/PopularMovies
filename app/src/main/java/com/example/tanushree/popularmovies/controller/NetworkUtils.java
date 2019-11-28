package com.example.tanushree.popularmovies.controller;

import android.net.Uri;
import android.util.Log;

import com.example.tanushree.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by tanushree on 18/02/19.
 */

public final class NetworkUtils
{
    //private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie";

    private static final String apiKey = BuildConfig.API_KEY;

    private static final String API_KEY_PARAM = "api_key";

    public static URL buildUrl(String sortBy)
    {
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(sortBy)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //Log.v(TAG, "Built Url " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException
    {
        //Log.d(TAG, "In method getResponseFromHttpUrl");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }
        finally {
            urlConnection.disconnect();
        }
    }

    public static URL buildTrailerOrReviewUrl(int movieId, String customPath)
    {
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(String.valueOf(movieId))
                .appendPath(customPath)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();

        URL url = null;
        try
        {
            url = new URL(builtUri.toString());
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        //Log.v(TAG, "Built Trailer/Review Url " + url);

        return url;
    }
}