package com.example.tanushree.popularmovies.controller;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.tanushree.popularmovies.model.AppDatabase;

/**
 * Created by tanushree on 21/04/19.
 */

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory
{
    private final AppDatabase mDatabase;
    private final int mMovieId;

    public DetailViewModelFactory(AppDatabase database, int movieId) {
        mDatabase = database;
        mMovieId = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailViewModel(mDatabase, mMovieId);
    }
}
