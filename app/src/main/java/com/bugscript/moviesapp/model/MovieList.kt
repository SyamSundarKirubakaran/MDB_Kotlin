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
        onProgressView()
        isNetworkAvailable{ status ->
            if(status) {
                NetworkClient.GetJsonWithOkHttpClient(POPULAR_MOVIES_URL) { success ->
                    if (success) {
                        initRecyclerView()
                        onCompleteView()
                    }
                }.execute();
            }else{
                Toast.makeText(this, "Check your Network connection.!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initRecyclerView() {
        movieAdapter = MoviesAdapter(this, NetworkClient.listOfMovies) { movie ->
            val detailsIntent = Intent(this, DetailsActivity::class.java)
            detailsIntent.putExtra("URL", generateMovieSpecificURL(movieID = movie.id))
            detailsIntent.putExtra("OBJ",movie)
            startActivity(detailsIntent)
        }
        movieList.adapter = movieAdapter
        val layoutManager = LinearLayoutManager(this)
        movieList.layoutManager = layoutManager
    }

    private fun onCompleteView() {
        movieList.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun onProgressView() {
        movieList.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }

    private fun isNetworkAvailable(status : (Boolean) -> Unit) {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            if(networkInfo?.isConnected!!)
                status(true)
            else
                status(false)
        } else status(false)
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
