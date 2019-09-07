package com.example.tanushree.popularmovies.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by tanushree on 18/02/19.
 */

@Entity(tableName = "movie_favorite")
public class Movie implements Parcelable
{
    @PrimaryKey
    private int id;
    @ColumnInfo(name = "original_title")
    private String originalTitle;
    @ColumnInfo(name = "poster_path")
    private String posterPath;
    @ColumnInfo(name = "plot_synopsis")
    private String plotSynopsis;
    private double rating;
    @ColumnInfo(name = "release_date")
    private String releaseDate;

    public Movie(int id, String originalTitle, String posterPath, String plotSynopsis, double rating, String releaseDate)
    {
        this.id = id;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.plotSynopsis = plotSynopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeInt(id);
        parcel.writeString(originalTitle);
        parcel.writeString(posterPath);
        parcel.writeString(plotSynopsis);
        parcel.writeDouble(rating);
        parcel.writeString(releaseDate);
    }

    public Movie()
    {
    }

    private Movie(Parcel parcel)
    {
        id = parcel.readInt();
        originalTitle = parcel.readString();
        posterPath = parcel.readString();
        plotSynopsis = parcel.readString();
        rating = parcel.readDouble();
        releaseDate = parcel.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>()
    {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}