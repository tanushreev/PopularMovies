package com.example.tanushree.popularmovies.controller;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.tanushree.popularmovies.model.AppDatabase;
import com.example.tanushree.popularmovies.model.Movie;

/**
 * Created by tanushree on 21/04/19.
 */

public class DetailViewModel extends ViewModel
{
    private LiveData<Movie> movie;

    public DetailViewModel(AppDatabase database, int movieId)
    {
        movie = database.movieDao().findMovieById(movieId);
    }

    public LiveData<Movie> getMovie()
    {
        return movie;
    }

}