package com.example.tanushree.popularmovies.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tanushree.popularmovies.R;
import com.example.tanushree.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tanushree on 18/02/19.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>
{

    //private static final String TAG = MoviesAdapter.class.getSimpleName();

    public static final String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w185/";

    private List<Movie> mMovieList;
    private Context mContext;
    private final MoviesAdapterOnClickHandler mClickHandler;

    public MoviesAdapter(MoviesAdapterOnClickHandler clickHandler)
    {
        mClickHandler = clickHandler;
    }

    public interface MoviesAdapterOnClickHandler
    {
        void onClick(Movie movie);
    }

    public void setMoviesData(List<Movie> movieList)
    {
        mMovieList = movieList;
        notifyDataSetChanged();
    }

    @Override
    public MoviesAdapter.MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.movies_list_item, parent, false);

        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.width = parent.getWidth() / 2;
        layoutParams.height = parent.getHeight() / 2;

        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.MoviesViewHolder holder, int position)
    {
        Movie movie = mMovieList.get(position);
        holder.bindMovie(movie);
    }

    @Override
    public int getItemCount() {
        if(mMovieList!=null) {
            //Log.i(TAG, mMovieList.size() + "");
            return mMovieList.size();
        }
        return 0;
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder implements OnClickListener
    {

        ImageView mPosterImageView;

        public MoviesViewHolder(View itemView) {
            super(itemView);

            mPosterImageView = itemView.findViewById(R.id.iv_movie);
            itemView.setOnClickListener(this);
        }

        public void bindMovie(Movie movie)
        {
            //Log.i(TAG, movie.getPosterPath());

            String path = BASE_POSTER_PATH + movie.getPosterPath();

            //Log.i(TAG, path);

            Picasso.get()
                    .load(path)
                    .error(R.drawable.poster_error)
                    .into(mPosterImageView);

        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovieList.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }
}