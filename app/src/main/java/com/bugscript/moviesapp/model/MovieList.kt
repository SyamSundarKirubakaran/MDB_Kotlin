package com.bugscript.moviesapp.model

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.bugscript.moviesapp.R
import com.bugscript.moviesapp.R.id.movieList
import com.bugscript.moviesapp.adapter.MoviesAdapter
import kotlinx.android.synthetic.main.list_movie.*

class MovieList : AppCompatActivity() {

    lateinit var movieAdapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_movie)

        movieAdapter = MoviesAdapter(this){
            val detailsIntent = Intent(this, DetailsActivity::class.java)
            startActivity(detailsIntent)
        }
        movieList.adapter = movieAdapter
        val layoutManager = LinearLayoutManager(this)
        movieList.layoutManager = layoutManager
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> true
            R.id.action_refresh -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}
