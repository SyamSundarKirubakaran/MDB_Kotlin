package com.bugscript.moviesapp.network

import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import com.bugscript.moviesapp.data.Movie
import com.bugscript.moviesapp.data.MovieDetails
import com.bugscript.moviesapp.utils.URLHelper
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

object NetworkClient{

    val listOfMovies = ArrayList<Movie>()
    lateinit var movieSpecifics : MovieDetails

    fun get(url: String): InputStream {
        val request = Request.Builder().url(url).build()
        val response = OkHttpClient().newCall(request).execute()
        val body = response.body()
        return body!!.byteStream()
    }

    open class GetJsonWithOkHttpClient(val url: String, val complete : (Boolean) -> Unit) : AsyncTask<Unit, Unit, String>() {
        override fun doInBackground(vararg params: Unit?): String? {
            val stream = BufferedInputStream(
                    NetworkClient.get(url))
            return readStream(stream)
        }
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val JO = JSONObject(result)
            if(url.equals(URLHelper.POPULAR_MOVIES_URL)){
                val JA = JO.getJSONArray("results")
                for (i in 0 until JA.length()){
                    val Jinside = JA.getJSONObject(i)
                    listOfMovies.add(Movie(
                            id = Jinside.getString("id"),
                            movieName = Jinside.getString("title"),
                            movieDescription = Jinside.getString("overview"),
                            movieRating = Jinside.getString("vote_average"),
                            releaseDate = Jinside.getString("release_date"),
                            language = Jinside.getString("original_language"),
                            poster = Jinside.getString("poster_path")))
                }
                complete(true)
            }else{
                val JA = JO.getJSONArray("genres")
                val genres = ArrayList<String>()
                for(i in 0 until JA.length()){
                    val Jinside = JA.getJSONObject(i)
                    genres.add(Jinside.getString("name"))
                }
                movieSpecifics = MovieDetails(
                        movieID = JO.getString("id"),
                        runtime = JO.getString("runtime"),
                        movieGenres = genres,
                        budget = JO.getString("budget"),
                        revenue = JO.getString("revenue"),
                        backdrop = JO.getString("backdrop_path"))
                complete(true)
            }
        }
        fun readStream(inputStream: BufferedInputStream): String {
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            bufferedReader.forEachLine { stringBuilder.append(it) }
            return stringBuilder.toString()
        }
    }

}