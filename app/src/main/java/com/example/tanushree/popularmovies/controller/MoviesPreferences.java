package com.example.tanushree.popularmovies.controller;

import android.content.Context;

/**
 * Created by tanushree on 27/02/19.
 */

public final class MoviesPreferences
{
    private static final String DEFAULT_SORT_BY = "popular";

    private static String mSortBy = null;

    private static String getDefaultSortBy()
    {
        return DEFAULT_SORT_BY;
    }

    public static String getPreferredSortBy(Context context){

        if(mSortBy == null)
            return getDefaultSortBy();
        else
            return mSortBy;
    }

    public static void setPreferredSortBy(String sortBy)
    {
        mSortBy = sortBy;
    }
}
