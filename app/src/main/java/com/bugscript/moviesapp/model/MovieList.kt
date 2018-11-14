package com.bugscript.moviesapp.model

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bugscript.moviesapp.R
import com.bugscript.moviesapp.adapter.MoviesAdapter
import com.bugscript.moviesapp.data.Movie
import com.bugscript.moviesapp.network.NetworkClient
import com.bugscript.moviesapp.utils.URLHelper.POPULAR_MOVIES_URL
import com.bugscript.moviesapp.utils.URLHelper.generateMovieSpecificURL
import kotlinx.android.synthetic.main.list_movie.*

class MovieList : AppCompatActivity(), SearchView.OnQueryTextListener {
    lateinit var movieAdapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_movie)
        onProgressView()
        performLoading()
    }

    private fun performLoading() {
        if(isNetworkAvailable()){
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

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                val searchView = item.actionView as SearchView
                searchView.setOnQueryTextListener(this)
            }
            R.id.action_refresh -> {
                onProgressView()
                performLoading()
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        Toast.makeText(this,"Search..!!!!!!!!",Toast.LENGTH_LONG).show()
        val userInput = p0!!.toLowerCase()
        val tempList = ArrayList<Movie>()

        NetworkClient.listOfMovies.forEach { movie ->
            if(movie.movieName.toLowerCase().contains(userInput)){
                tempList.add(movie)
            }
        }

        movieAdapter.updateList(tempList)
        performLoading()
        return true
    }
}
