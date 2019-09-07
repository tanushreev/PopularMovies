package com.example.tanushree.popularmovies.controller;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tanushree.popularmovies.R;
import com.example.tanushree.popularmovies.model.AppDatabase;
import com.example.tanushree.popularmovies.model.AppExecutors;
import com.example.tanushree.popularmovies.model.Movie;
import com.example.tanushree.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by tanushree on 28/02/19.
 */

public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String>, TrailersAdapter.CustomOnClickHandler
{
    //private static final String TAG = DetailActivity.class.getSimpleName();

    private static final int MOVIE_TRAILERS_LOADER = 100;

    private static final String EXTRA_URL = "url";

    private Movie mMovie;
    private TextView mTitleTv;
    private ImageView mPosterIv;
    private TextView mPlotSynopsisTv;
    private TextView mRatingTv;
    private TextView mReleaseDateTv;
    private Button mFavoriteButton;
    private TextView mTrailersTv;
    private RecyclerView mTrailersRv;
    private TrailersAdapter mTrailersAdapter;
    private Button mReviewsButton;

    private AppDatabase mDatabase;

    private boolean mFavFlag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDatabase = AppDatabase.getInstance(getApplicationContext());

        mTitleTv = findViewById(R.id.tvTitle);
        mPosterIv = findViewById(R.id.ivMoviePoster);
        mPlotSynopsisTv = findViewById(R.id.tvPlotSynopsis);
        mRatingTv = findViewById(R.id.tvRating);
        mReleaseDateTv = findViewById(R.id.tvReleaseYear);

        // Mark as favourite button.
        mFavoriteButton = findViewById(R.id.bFavorite);

        mTrailersTv = findViewById(R.id.tvTrailerLabel);

        mTrailersRv = findViewById(R.id.rvTrailers);

        mReviewsButton = findViewById(R.id.bReadReviews);
        mReviewsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ReviewsActivity.class);
                intent.putExtra(MainActivity.KEY_MOVIE_DATA, mMovie);
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager
                (this, LinearLayoutManager.VERTICAL, false);

        mTrailersRv.setLayoutManager(layoutManager);
        mTrailersAdapter = new TrailersAdapter(this);
        mTrailersRv.setAdapter(mTrailersAdapter);
        mTrailersRv.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));

        Intent intentThatStartedThisActivity = getIntent();

        if(intentThatStartedThisActivity!=null)
        {
            if(intentThatStartedThisActivity.hasExtra(MainActivity.KEY_MOVIE_DATA))
            {
                mMovie = intentThatStartedThisActivity
                        .getParcelableExtra(MainActivity.KEY_MOVIE_DATA);

                populateUI();

                setTitle(R.string.detail_activity_title);

                final int movieId = mMovie.getId();

                DetailViewModelFactory factory = new DetailViewModelFactory(mDatabase, movieId);
                final DetailViewModel viewModel = ViewModelProviders.of(this, factory).get(DetailViewModel.class);

                viewModel.getMovie().observe(this, new Observer<Movie>()
                {
                    @Override
                    public void onChanged(@Nullable Movie movieEntry)
                    {
                        //Log.d(TAG, "Receiving database update from LiveData");
                        if(movieEntry!=null)
                        {
                            mFavFlag = true;
                            mFavoriteButton.setText(R.string.detail_remove_fav_button_text);
                        }
                    }
                });

                URL url = NetworkUtils.buildTrailerOrReviewUrl(movieId, "videos");

                Bundle bundle = new Bundle();

                bundle.putString(EXTRA_URL, url.toString());

                LoaderManager loaderManager = getSupportLoaderManager();
                Loader<String> movieTrailersLoader = loaderManager.getLoader(MOVIE_TRAILERS_LOADER);

                //Log.d(TAG, "In method onCreate of DetailActivity");

                if(isNetworkAvailable()) {
                    if (movieTrailersLoader == null)
                        loaderManager.initLoader(MOVIE_TRAILERS_LOADER, bundle, this);
                    else
                        loaderManager.restartLoader(MOVIE_TRAILERS_LOADER, bundle, this);
                }
                else {
                    Toast.makeText
                            (this, R.string.network_unavailable_message, Toast.LENGTH_LONG).show();
                }

                mFavoriteButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if(mFavFlag == false)
                        {
                            //Insert in the database table.
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    mDatabase.movieDao().insertMovie(mMovie);
                                }
                            });
                            mFavFlag = true;
                            mFavoriteButton.setText(R.string.detail_remove_fav_button_text);
                            Toast.makeText(DetailActivity.this, R.string.added_to_fav, Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            //Delete from the database table.
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    mDatabase.movieDao().deleteMovie(mMovie);
                                }
                            });
                            mFavFlag = false;
                            mFavoriteButton.setText(R.string.detail_add_fav_button_text);
                            Toast.makeText(DetailActivity.this, R.string.removed_from_fav, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }

    private void populateUI()
    {
        String path = MoviesAdapter.BASE_POSTER_PATH + mMovie.getPosterPath();

        Picasso.get()
                .load(path)
                .error(R.drawable.poster_error)
                .into(mPosterIv);

        mTitleTv.setText(mMovie.getOriginalTitle());

        if(mMovie.getPlotSynopsis().equals(""))
            mPlotSynopsisTv.setText(R.string.no_plot_synopsis);
        else
            mPlotSynopsisTv.setText(mMovie.getPlotSynopsis());

        String rating = String.valueOf(mMovie.getRating()) + "/10";
        mRatingTv.setText(rating);

        if(mMovie.getReleaseDate().equals(""))
            mReleaseDateTv.setText(R.string.no_data_present);
        else if(mMovie.getReleaseDate().length() > 4) {
            mReleaseDateTv.setText(mMovie.getReleaseDate().substring(0,4));
        }

    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args)
    {
        //Log.d(TAG, "In method onCreateLoader");
        return new AsyncTaskLoader<String>(this) {

            //This is like onPreExecute().
            @Override
            protected void onStartLoading() {
                //Log.d(TAG, "In method onStartLoading");
                super.onStartLoading();
                if(args == null) {
                    //Log.d(TAG, "Bundle is null");
                    return;
                }
                forceLoad();
            }

            //This is like doInBackground().
            @Override
            public String loadInBackground()
            {
                //Log.d(TAG, "In method loadInBackground");
                String urlString = args.getString(EXTRA_URL);
                if (urlString == null)
                    return null;

                try {
                    URL url = new URL(urlString);
                    String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
                    //Log.v(TAG, jsonResponse);
                    return jsonResponse;
                }
                catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    //This is like onPostExecute().
    @Override
    public void onLoadFinished(Loader<String> loader, String data)
    {
        //Log.d(TAG, "In method onLoadFinished.");
        if (data!=null) {
            //Log.v(TAG, data);
            try {
                List<Trailer> trailerList =  MoviesJsonUtils.getTrailerListFromJson(data);
                if(trailerList.size() == 0)
                {
                    mTrailersTv.setText(R.string.no_trailers);
                }
                else {
                    mTrailersTv.setText(R.string.detail_trailers_label);
                    mTrailersAdapter.setTrailersData(trailerList);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(DetailActivity.this, R.string.no_response_from_server,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public void onClick(Trailer trailer)
    {
        Intent appIntent = new Intent
                (Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.getKey()));

        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=" + trailer.getKey()));

        try {
            startActivity(appIntent);
        }
        catch (ActivityNotFoundException e) {
            startActivity(webIntent);
        }
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo!=null && activeNetworkInfo.isConnected());
    }
}
