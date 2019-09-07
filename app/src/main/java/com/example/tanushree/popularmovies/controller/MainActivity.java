package com.example.tanushree.popularmovies.controller;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tanushree.popularmovies.controller.MoviesAdapter.MoviesAdapterOnClickHandler;
import com.example.tanushree.popularmovies.R;
import com.example.tanushree.popularmovies.model.Movie;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapterOnClickHandler {

    //private static final String TAG = MainActivity.class.getSimpleName();

    private static final String sortByPopularity = "popular";
    private static final String sortByRating = "top_rated";
    private static final String showFavorites = "show_favorites";

    private RecyclerView mMoviesRv;

    private MoviesAdapter mMoviesAdapter;

    //public, so that we can access from another class.
    public static final String KEY_MOVIE_DATA = "MOVIE_DATA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesRv = findViewById(R.id.rv_movies);

        GridLayoutManager layoutManager = new GridLayoutManager
                (this, 2, GridLayoutManager.VERTICAL, false);

        mMoviesRv.setLayoutManager(layoutManager);
        mMoviesRv.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this);
        mMoviesRv.setAdapter(mMoviesAdapter);

        loadMoviesData();
    }

    private void loadMoviesData()
    {
        String sortBy = MoviesPreferences.getPreferredSortBy(this);

        if(sortBy.equals(showFavorites))
        {
            setTitle("Favorite movies");

            //Get movies from the Database via ViewModel.
            MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
            viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    //Log.d(TAG, "Updating list of movies from LiveData in ViewModel");
                    setTitle("Favorite movies");
                    mMoviesAdapter.setMoviesData(movies);
                }
            });
        }
        else {
            if(sortBy.equals(sortByPopularity))
                setTitle("Sort by most popular");
            else
                setTitle("Sort by top rated");

            if (isNetworkAvailable()) {
                new FetchMoviesTask().execute(sortBy);
            } else {
                Toast.makeText
                        (this, R.string.network_unavailable_message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id =  item.getItemId();

        if(id == R.id.action_popular)
        {
            MoviesPreferences.setPreferredSortBy(sortByPopularity);
            mMoviesAdapter.setMoviesData(null);
            //Network call.
            loadMoviesData();
            return true;
        }

        else if(id == R.id.action_topRated)
        {
            MoviesPreferences.setPreferredSortBy(sortByRating);
            mMoviesAdapter.setMoviesData(null);
            //Network call.
            loadMoviesData();
            return true;
        }

        else if(id == R.id.action_favorites)
        {
            MoviesPreferences.setPreferredSortBy(showFavorites);
            mMoviesAdapter.setMoviesData(null);
            //Get from the database via ViewModel.
           loadMoviesData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo!=null && activeNetworkInfo.isConnected());
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(KEY_MOVIE_DATA, movie);
        startActivity(intent);
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>>
    {
        @Override
        protected List<Movie> doInBackground(String... strings)
        {
            String sortBy = strings[0];
            URL moviesRequestUrl = NetworkUtils.buildUrl(sortBy);

            try
            {
                String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);
                //Log.v(TAG, jsonMoviesResponse);

                if(jsonMoviesResponse!=null)
                {
                    List<Movie> movieList =
                            MoviesJsonUtils.getMovieListFromJson(jsonMoviesResponse);

                    /*for(Movie movie : movieList)
                    {
                        Log.i(TAG, movie.getId()+"");
                        Log.i(TAG, movie.getOriginalTitle());
                        Log.i(TAG, movie.getPosterPath());
                    }*/

                    return movieList;
                }
                else
                {
                    Toast.makeText(MainActivity.this, R.string.no_response_from_server,
                            Toast.LENGTH_LONG).show();
                    return null;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies)
        {
            if(movies == null)
            {
                Toast.makeText(MainActivity.this, R.string.no_response_from_server,
                        Toast.LENGTH_LONG).show();
            }

            else if(!movies.isEmpty())
                mMoviesAdapter.setMoviesData(movies);
        }
    }
}