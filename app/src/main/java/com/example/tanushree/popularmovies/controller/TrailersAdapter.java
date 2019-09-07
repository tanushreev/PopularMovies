package com.example.tanushree.popularmovies.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tanushree.popularmovies.R;
import com.example.tanushree.popularmovies.model.Trailer;

import java.util.List;

/**
 * Created by tanushree on 07/04/19.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder>
{
    //private static final String TAG = TrailersAdapter.class.getSimpleName();

    private List<Trailer> mTrailerList;
    private Context mContext;
    private final CustomOnClickHandler mClickHandler;

    public TrailersAdapter(CustomOnClickHandler clickHandler)
    {
        mClickHandler = clickHandler;
    }

    public interface CustomOnClickHandler
    {
        void onClick(Trailer trailer);
    }

    public void setTrailersData(List<Trailer> trailerList)
    {
        mTrailerList = trailerList;
        notifyDataSetChanged();
    }

    //Returns a ViewHolder (invoked by the layout manager).
    @Override
    public TrailersAdapter.TrailersViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        mContext=parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.trailers_list_item, parent, false);
        return new TrailersViewHolder(view);
    }

    //Replaces the contents of the view (invoked by the layout manager).
    @Override
    public void onBindViewHolder(TrailersAdapter.TrailersViewHolder holder, int position)
    {
        Trailer trailer = mTrailerList.get(position);
        holder.bindTrailer(trailer);
    }

    @Override
    public int getItemCount() {
        if(mTrailerList!=null)
            return mTrailerList.size();
        return 0;
    }

    //The ViewHolder Inner class.

    public class TrailersViewHolder extends RecyclerView.ViewHolder implements OnClickListener
    {
        TextView mNameTv;
        ImageView mTrailerPlayIv;

        public TrailersViewHolder(View itemView) {
            super(itemView);
            mNameTv = itemView.findViewById(R.id.tvTrailerName);
            mTrailerPlayIv = itemView.findViewById(R.id.ivPlay);
            mTrailerPlayIv.setOnClickListener(this);
            //itemView.setOnClickListener(this);
        }

        public void bindTrailer(Trailer trailer)
        {
            mNameTv.setText(trailer.getName());
        }

        @Override
        public void onClick(View view)
        {
            // Returns the Adapter position of the item represented by this ViewHolder.
            int adapterPosition = getAdapterPosition();
            Trailer trailer = mTrailerList.get(adapterPosition);
            mClickHandler.onClick(trailer);
        }
    }
}