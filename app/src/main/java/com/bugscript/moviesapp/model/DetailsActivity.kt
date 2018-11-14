package com.bugscript.moviesapp.model

import android.app.ProgressDialog
import android.app.SearchManager
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.View
import android.widget.Toast
import com.bugscript.moviesapp.R
import com.bugscript.moviesapp.R.id.mainView
import com.bugscript.moviesapp.data.Movie
import com.bugscript.moviesapp.databinding.ActivityDetailsBinding
import com.bugscript.moviesapp.network.NetworkClient
import com.bugscript.moviesapp.network.NetworkClient.movieSpecifics
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*
import org.jetbrains.anko.indeterminateProgressDialog

class DetailsActivity : AppCompatActivity(){
    private lateinit var binding : ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_details)
        binding.invalidateAll()

        val pro = ProgressDialog.show(this,"","Loading")
        mainView.visibility = View.INVISIBLE
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val idURL = intent.getStringExtra("URL")
        val movieSpecific = intent.getSerializableExtra("OBJ") as Movie
        supportActionBar!!.title = movieSpecific.movieName

        NetworkClient.GetJsonWithOkHttpClient(idURL) { success ->
            if (success) {
                movieSpecifics.let {
                    binding.apply {
                        detailsMovieDesc.text = movieSpecific.movieDescription
                        detailsRunTime.text = "${movieSpecifics.runtime} minutes"
                        detailsReleaseDate.text = movieSpecific.releaseDate
                        detailsRating.text = movieSpecific.movieRating
                        detailsMovieType.text = movieSpecifics.movieGenres.toString().substring(1,movieSpecifics.movieGenres.toString().length-1)
                        detailsMovieLanguage.text = movieSpecific.language
                        movieBudget.text = "$${movieSpecifics.budget.toInt()/1000000} Million"
                        movieRevenue.text = "$${movieSpecifics.revenue.toInt()/1000000} Million"
                        Picasso.get()
                                .load("https://image.tmdb.org/t/p/w780/${movieSpecifics.backdrop}")
                                .into(imageView2)
                    }
                    mainView.visibility = View.VISIBLE
                    pro.dismiss()
                }
            }
        }.execute()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
