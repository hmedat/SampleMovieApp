package com.movie.app.modules

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

class MoviesResult {
    var page: Int = 0
    @SerializedName("total_pages")
    var totalPages: Int = 0
    var results: List<Movie>? = null

    fun isFirstPage(): Boolean = page == MovieSearchFilter.First_PAGE

    fun isFinished(): Boolean = page == totalPages

    fun isEmptyData(): Boolean = results.isNullOrEmpty()
}

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey var id: Long = 0,
    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average") var voteAverage: Double = 0.toDouble(),

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path") var posterPath: String? = null,

    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path") var backdropPath: String? = null,

    @ColumnInfo(name = "overview") var overview: String? = null,

    @ColumnInfo(name = "popularity")
    var popularity: Double? = null,

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date") var releaseDate: String? = null,

    @ColumnInfo(name = "release_date_long")
    var releaseDateLong: Long? = null,

    @ColumnInfo(name = "home_page")
    var homepage: String? = null,

    @ColumnInfo(name = "is_Fav")
    var isFav: Boolean = false,

    @Ignore
    var genres: List<Genre>? = null,
    @Ignore
    @SerializedName("videos") var videoResult: VideoResult? = null,
    @Ignore
    var videosList: List<Video>? = null
)

@Entity(tableName = "genre")
data class Genre(
    @PrimaryKey var id: Long,
    @ColumnInfo(name = "name") var name: String? = null
)


class VideoResult(vararg videos: Video) {
    @SerializedName("results")
    var videos: ArrayList<Video>? = null

    init {
        this.videos = ArrayList()
        for (video in videos) {
            this.videos?.add(video)
        }
    }
}

@Entity(
    tableName = "video", foreignKeys = [(ForeignKey(
    entity = Movie::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("movie_id"),
    onDelete = ForeignKey.CASCADE
))], indices = [Index("movie_id")]
)
data class Video(
    @NonNull @PrimaryKey() var id: String,
    @ColumnInfo(name = "key") var key: String? = null,
    @ColumnInfo(name = "size") var size: Int = 0,
    @ColumnInfo(name = "type") var type: String? = null,
    @ColumnInfo(name = "movie_id") var movieId: Long = 0
)



