package com.example.tanushree.popularmovies.controller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tanushree.popularmovies.R;
import com.example.tanushree.popularmovies.model.Movie;
import com.example.tanushree.popularmovies.model.Review;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ReviewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>
{
    //private static final String TAG = ReviewsActivity.class.getSimpleName();

    private static final int MOVIE_REVIEWS_LOADER = 102;

    private static final String EXTRA_REVIEW_URL = "review_url";

    private TextView mReviewsTv;
    private RecyclerView mReviewsRv;
    private Movie mMovie;
    private ReviewsAdapter mReviewsAdapter;
    private ImageView mDividerIv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        mReviewsTv = findViewById(R.id.tvReviews);
        mDividerIv = findViewById(R.id.divider2);
        mReviewsRv = findViewById(R.id.rvReviews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mReviewsRv.setLayoutManager(layoutManager);
        mReviewsAdapter = new ReviewsAdapter();
        mReviewsRv.setAdapter(mReviewsAdapter);
        mReviewsRv.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));

        Intent intentThatStartedThisActivity = getIntent();

        if(intentThatStartedThisActivity!=null) {
            if (intentThatStartedThisActivity.hasExtra(MainActivity.KEY_MOVIE_DATA)) {
                mMovie = intentThatStartedThisActivity
                        .getParcelableExtra(MainActivity.KEY_MOVIE_DATA);

                String movieName = mMovie.getOriginalTitle();
                setTitle(movieName);

                int movieId = mMovie.getId();
                //Log.d(TAG, movieId + "");
                //Log.d(TAG, mMovie.getOriginalTitle());

                URL url = NetworkUtils.buildTrailerOrReviewUrl(movieId, "reviews");
                //Log.d(TAG, url.toString());

                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_REVIEW_URL, url.toString());

                LoaderManager loaderManager =getSupportLoaderManager();

                Loader<String> movieReviewsLoader = loaderManager.getLoader(MOVIE_REVIEWS_LOADER);

                if(isNetworkAvailable())
                {
                    if (movieReviewsLoader == null)
                        loaderManager.initLoader(MOVIE_REVIEWS_LOADER, bundle, this);
                    else
                        loaderManager.restartLoader(MOVIE_REVIEWS_LOADER, bundle, this);
                }
                else
                {
                    Toast.makeText
                            (this, R.string.network_unavailable_message, Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(args==null)
                    return;

                forceLoad();
            }

            @Override
            public String loadInBackground() {
                String urlString = args.getString(EXTRA_REVIEW_URL);
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

    @Override
    public void onLoadFinished(Loader<String> loader, String data)
    {
        if(data!=null)
        {
            try {
                List<Review> reviewList = MoviesJsonUtils.getReviewListFromJson(data);
                if(reviewList.size()==0)
                {
                    mReviewsTv.setText(R.string.no_reviews);
                    mDividerIv.setVisibility(View.GONE);
                }
                else
                {
                    mReviewsTv.setText(R.string.reviews_label);
                    mReviewsAdapter.setReviewsData(reviewList);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(ReviewsActivity.this, R.string.no_response_from_server,
                    Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo!=null && activeNetworkInfo.isConnected());
    }
}
