package com.swvl.adecadeofmovies.utils

import android.R
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.swvl.adecadeofmovies.MyApplication.Companion.myApplicationContext
import com.swvl.adecadeofmovies.models.Movie
import com.swvl.adecadeofmovies.models.MoviesList
import com.swvl.adecadeofmovies.models.flicker_photo.Photo
import com.swvl.adecadeofmovies.utils.Constants.SNACK_BAR_DISMISS_TEXT


object AppUtils {

    fun readJSONData(jsonFileName: String): List<Movie> {
        val str = myApplicationContext?.assets?.open(jsonFileName)?.bufferedReader()
            .use { it?.readText() }
        return Gson().fromJson(str, MoviesList::class.java).movies
    }

    fun getFlickrPhotoUrl(photo: Photo): String? {
        var photoUrl: String?

        with(photo) {
            photoUrl = "https://farm$farm.static.flickr.com/$server/${id}_$secret.jpg"
        }

        return photoUrl
    }

    fun getMoviesSortedByYearWithCount(moviesList: MutableList<Movie>): MutableList<Movie> {

        val moviesSortedByRating: MutableList<Movie> =
            moviesList.sortedWith(compareByDescending { it.rating }).toMutableList()

        moviesList.clear()

        val groupedMovies: Map<Int, List<Movie>> = moviesSortedByRating.groupBy { it.year }
        groupedMovies.forEach {
            moviesList.addAll(it.value.take(5))
        }

        return moviesList.sortedByDescending { it.year }.toMutableList()
    }

    @Suppress("DEPRECATION")
    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            myApplicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }

    fun Activity.showSnackBar(message: CharSequence) {

        val viewGroup = (this.findViewById(R.id.content) as ViewGroup).getChildAt(0) as ViewGroup

        val snackBar = Snackbar.make(viewGroup, message, Snackbar.LENGTH_LONG)
        snackBar.setAction(SNACK_BAR_DISMISS_TEXT) { snackBar.dismiss() }
        snackBar.setActionTextColor(
            ContextCompat.getColor(
                this.applicationContext,
                R.color.white
            )
        )
        snackBar.show()
    }

}