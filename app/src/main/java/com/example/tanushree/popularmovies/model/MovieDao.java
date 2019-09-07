package com.example.tanushree.popularmovies.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by tanushree on 13/04/19.
 */

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie_favorite")
    LiveData<List<Movie>> loadAllFavoriteMovies();

    @Query("SELECT * FROM movie_favorite WHERE id = :id")
    LiveData<Movie> findMovieById(int id);

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);
}
