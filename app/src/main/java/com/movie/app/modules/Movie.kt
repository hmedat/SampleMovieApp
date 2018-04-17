package com.movie.app.modules

import com.google.gson.annotations.SerializedName

class Movie {
    /**
     * vote_count : 7602
     * id : 198663
     * video : false
     * vote_average : 7
     * title : The Maze Runner
     * poster_path : /coss7RgL0NH6g4fC2s5atvf3dFO.jpg
     * original_language : en
     * original_title : The Maze Runner
     * genre_ids : [28,9648,878,53]
     * backdrop_path : /lkOZcsXcOLZYeJ2YxJd3vSldvU4.jpg
     * adult : false
     * overview : Set in a post-apocalyptic world, young Thomas is deposited in a community of boys after his memory is erased, soon learning they're all trapped in a maze that will require him to join forces with fellow “runners” for a shot at escape.
     * release_date : 2014-09-10
     */

    @SerializedName("vote_count")
    val voteCount: Int = 0
    var id: Int = 0
    var isVideo: Boolean = false
    @SerializedName("vote_average")
    var voteAverage: Double = 0.toDouble()
    var title: String? = null
    @SerializedName("poster_path")
    var posterPath: String? = null
    @SerializedName("backdrop_path")
    var backdropPath: String? = null
    var overview: String? = null
    @SerializedName("release_date")
    var releaseDate: String? = null
    @SerializedName("genre_ids")
    var genreIds: List<Int>? = null

}
