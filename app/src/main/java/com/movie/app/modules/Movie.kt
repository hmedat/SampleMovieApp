package com.movie.app.modules

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.movie.app.api.result.ReviewResult
import com.movie.app.api.result.VideoResult

@Entity(tableName = "movie")
data class Movie(
        @PrimaryKey() var id: Long = 0,
        @ColumnInfo(name = "vote_average")
        @SerializedName("vote_average")
        var voteAverage: Double = 0.toDouble(),

        @ColumnInfo(name = "title")
        var title: String? = null,

        @ColumnInfo(name = "poster_path")
        @SerializedName("poster_path")
        var posterPath: String? = null,

        @ColumnInfo(name = "backdrop_path")
        @SerializedName("backdrop_path")
        var backdropPath: String? = null,

        @ColumnInfo(name = "overview")
        var overview: String? = null,

        @ColumnInfo(name = "release_date")
        @SerializedName("release_date")
        var releaseDate: String? = null,

        @ColumnInfo(name = "home_page")
        var homepage: String? = null,

        @Ignore
        var genres: List<Genre>? = null,
        @Ignore
        var genresString: String? = null,
        @Ignore
        var videos: VideoResult? = null,
        @Ignore
        var firstVideoUrl: String? = null,
        @Ignore
        var firstVideoImageUrl: String? = null,
        @Ignore
        var reviews: ReviewResult? = null
)