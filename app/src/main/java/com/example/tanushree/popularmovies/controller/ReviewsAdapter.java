package com.example.tanushree.popularmovies.controller;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tanushree.popularmovies.R;
import com.example.tanushree.popularmovies.model.Review;

import java.util.List;

/**
 * Created by tanushree on 10/04/19.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    //private static final String TAG = ReviewsAdapter.class.getSimpleName();

    private List<Review> mReviewList;

    public void setReviewsData(List<Review> reviewList)
    {
        mReviewList=reviewList;
        /*for(int i=0; i<mReviewList.size();i++)
        {
            Log.v(TAG, mReviewList.get(i).getContent());
        }*/
        notifyDataSetChanged();
    }


    @Override
    public ReviewsAdapter.ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviews_list_item,parent,false);

        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ReviewsViewHolder holder, int position) {
        Review review = mReviewList.get(position);
        holder.bindReview(review);

    }

    @Override
    public int getItemCount() {
        if(mReviewList!=null) {
            //Log.d(TAG, mReviewList.size() + "");
            return mReviewList.size();
        }
        return 0;
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {

        TextView mAuthorTv;
        TextView mContentTv;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            mAuthorTv = itemView.findViewById(R.id.tvAuthor);
            mContentTv = itemView.findViewById(R.id.tvContent);
        }

        public void bindReview(Review review) {
            mContentTv.setText(review.getContent());
            String author = "- "+ review.getAuthor();
            mAuthorTv.setText(author);
        }
    }
}
