package com.bugscript.moviesapp.model

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bugscript.moviesapp.R
import com.bugscript.moviesapp.adapter.MoviesAdapter
import com.bugscript.moviesapp.network.NetworkClient
import com.bugscript.moviesapp.utils.URLHelper.POPULAR_MOVIES_URL
import com.bugscript.moviesapp.utils.URLHelper.generateMovieSpecificURL
import kotlinx.android.synthetic.main.list_movie.*

class MovieList : AppCompatActivity() {

    lateinit var movieAdapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_movie)

        movieList.visibility = View.INVISIBLE

        if(isNetworkAvailable()) {
            NetworkClient.GetJsonWithOkHttpClient(POPULAR_MOVIES_URL).execute();
            movieAdapter = MoviesAdapter(this){
                val detailsIntent = Intent(this, DetailsActivity::class.java)
                startActivity(detailsIntent)
            }
            movieList.adapter = movieAdapter
            val layoutManager = LinearLayoutManager(this)
            movieList.layoutManager = layoutManager
            movieList.visibility = View.VISIBLE
        }else{
            Toast.makeText(this, "Check your Network connection.!", Toast.LENGTH_LONG).show()
        }
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

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }
}
