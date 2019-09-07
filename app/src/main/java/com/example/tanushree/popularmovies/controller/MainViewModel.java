package com.example.tanushree.popularmovies.controller;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tanushree.popularmovies.model.AppDatabase;
import com.example.tanushree.popularmovies.model.Movie;

import java.util.List;

/**
 * Created by tanushree on 21/04/19.
 */

public class MainViewModel extends AndroidViewModel {

    //private static String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Movie>> movieList;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        //Log.d(TAG, "Actively retrieving favorite movies from the Database");
        movieList = database.movieDao().loadAllFavoriteMovies();
    }

    LiveData<List<Movie>> getMovies()
    {
        return movieList;
    }
}