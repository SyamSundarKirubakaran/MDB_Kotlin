package com.bugscript.moviesapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bugscript.moviesapp.R
import com.bugscript.moviesapp.data.Movie
import com.squareup.picasso.Picasso

class MoviesAdapter(val context : Context,val movieList : ArrayList<Movie>, val itemClick : (Movie) -> Unit) : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item,p0,false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return movieList.count()
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindViews(movieList[p1])
    }

    inner class ViewHolder(itemView : View, val itemClick : (Movie) -> Unit) : RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.movieHeader)
        val desc = itemView.findViewById<TextView>(R.id.movieDescription)
        val rating = itemView.findViewById<TextView>(R.id.movieRating)
        val date = itemView.findViewById<TextView>(R.id.releaseDate)
        val lang = itemView.findViewById<TextView>(R.id.movieLanguage)
        val poster = itemView.findViewById<ImageView>(R.id.moviePoster)

        fun bindViews(movie : Movie){
            name.text = movie.movieName
            desc.text = "${movie.movieDescription.substring(0,80)}..."
            rating.text = movie.movieRating
            date.text = movie.releaseDate
            lang.text = movie.language
            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w200/${movie.poster}")
                    .into(poster)
            itemView.setOnClickListener { itemClick(movie) }
        }
    }

    fun updateList(newList : ArrayList<Movie>){
        movieList.clear()
        movieList.addAll(newList)
        notifyDataSetChanged()
    }

}