package com.movie.app.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

object DateUtil {

    fun parseMovieReleaseDate(dateString: String): Long {
        return try {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val date = formatter.parse(dateString)
            date.time
        } catch (e: ParseException) {
            0
        }
    }
}
