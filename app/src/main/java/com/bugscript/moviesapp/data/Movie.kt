package com.bugscript.moviesapp.data

import java.io.Serializable

data class Movie(val id : String, val movieName : String, val movieDescription : String, val movieRating : String, val releaseDate: String, val language : String, val poster : String) : Serializable