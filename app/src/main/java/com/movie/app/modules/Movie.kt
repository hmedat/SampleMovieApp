package com.movie.app.modules

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Movie(
        @SerializedName("vote_count")
        val voteCount: Int = 0,
        var id: Long = 0,
        val budget: Int = 0,
        @SerializedName("vote_average")
        var voteAverage: Double = 0.toDouble(),
        var title: String? = null,
        @SerializedName("poster_path")
        var posterPath: String? = null,
        @SerializedName("backdrop_path")
        var backdropPath: String? = null,
        var overview: String? = null,
        @SerializedName("release_date")
        var releaseDate: String? = null,
        val homepage: String? = null,
        val genres: List<Genres>? = null,
        var genresString: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readLong(),
            parcel.readInt(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(Genres),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(voteCount)
        parcel.writeLong(id)
        parcel.writeInt(budget)
        parcel.writeDouble(voteAverage)
        parcel.writeString(title)
        parcel.writeString(posterPath)
        parcel.writeString(backdropPath)
        parcel.writeString(overview)
        parcel.writeString(releaseDate)
        parcel.writeString(homepage)
        parcel.writeTypedList(genres)
        parcel.writeString(genresString)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }

}