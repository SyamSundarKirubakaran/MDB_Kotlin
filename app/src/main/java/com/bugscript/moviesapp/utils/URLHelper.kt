package com.bugscript.moviesapp.utils

import com.bugscript.moviesapp.data.Movie

object URLHelper{

    val API_KEY = "47ea50d7ece7e74f25725d5937da4586"
    val POPULAR_MOVIES_URL = "https://api.themoviedb.org/3/discover/movie?api_key=${API_KEY}&language=en-US&sort_by=popularity.desc"

    fun generateMovieSpecificURL(movieID : String) : String {
        return "https://api.themoviedb.org/3/movie/${movieID}?api_key=${API_KEY}&language=en-US"
    }

}
