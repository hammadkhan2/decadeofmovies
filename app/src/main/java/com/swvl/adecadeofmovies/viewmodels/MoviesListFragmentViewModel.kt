package com.swvl.adecadeofmovies.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.swvl.adecadeofmovies.models.Movie
import com.swvl.adecadeofmovies.utils.AppUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoviesListFragmentViewModel @Inject constructor() : ViewModel() {
    val movies: MutableLiveData<List<Movie>> = MutableLiveData<List<Movie>>()

    fun getMovies(): LiveData<List<Movie>> {
        return movies
    }

    fun setMovies(jsonFileName: String) {
        movies.value = AppUtils.readJSONData(jsonFileName)
    }
}