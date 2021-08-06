package com.swvl.adecadeofmovies.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.swvl.adecadeofmovies.data.repositories.PhotosRepository
import com.swvl.adecadeofmovies.data.repositories.PhotosRepositoryImpl
import com.swvl.adecadeofmovies.models.Movie
import com.swvl.adecadeofmovies.models.flicker_photo.FlickerPhotoResponse
import com.swvl.adecadeofmovies.utils.Constants.CAST_SHOW_FULL
import com.swvl.adecadeofmovies.utils.Constants.CAST_SHOW_LESS
import com.swvl.adecadeofmovies.utils.Constants.HIDE_SHOW_CAST_TEXT
import com.swvl.adecadeofmovies.utils.Constants.SHOW_LESS
import com.swvl.adecadeofmovies.utils.Constants.VIEW_ALL_CAST
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoviesDetailsFragmentViewModel @Inject constructor() : ViewModel() {

    private val _pageNumber: MutableLiveData<Int> = MutableLiveData()
    private lateinit var movieName: String
    private lateinit var movie: Movie
    private var photosRepository: PhotosRepository = PhotosRepositoryImpl()

    val movieMutableLiveData: MutableLiveData<Movie> = MutableLiveData()

    val movieCastList: MutableLiveData<MutableList<String>> = MutableLiveData()

    val showAllCastText: MutableLiveData<String> = MutableLiveData()

    val noNetworkConnection: MutableLiveData<Boolean> = MutableLiveData()

    val flickerPhotoResponse: LiveData<FlickerPhotoResponse> = Transformations
        .switchMap(_pageNumber) { pageNumber ->
            photosRepository.getPhotos(
                movieName,
                pageNumber
            )
        }

    fun setPhotosByApiCall(movieName: String, pageNumber: Int, isNetworkAvailable: Boolean) {
        this.movieName = movieName

        if (isNetworkAvailable) {
            _pageNumber.value = pageNumber
            noNetworkConnection.value = true
        } else noNetworkConnection.value = false
    }

    fun setMovie(movie: Movie) {
        this.movie = movie
        this.movieMutableLiveData.value = movie

        if (movie.cast.size > 3) {
            movieCastList.value = addFirstThreeCast()
        } else {
            val castLessList: MutableList<String> = mutableListOf()
            castLessList.addAll(movie.cast)
            movieCastList.value = castLessList
            showAllCastText.value = HIDE_SHOW_CAST_TEXT
        }
    }

    fun setMockPhotosRepository(photosRepository: PhotosRepository) {
        this.photosRepository = photosRepository
    }

    fun castClick(tvShowAllCastText: String) {
        when (tvShowAllCastText) {
            VIEW_ALL_CAST -> {
                val castLessList: MutableList<String> = mutableListOf()
                castLessList.addAll(movie.cast)
                movieCastList.value = castLessList
                showAllCastText.value = CAST_SHOW_LESS
            }

            SHOW_LESS -> {
                movieCastList.value = addFirstThreeCast()
                showAllCastText.value = CAST_SHOW_FULL
            }
        }
    }

    private fun addFirstThreeCast(): MutableList<String> {
        val castLessList: MutableList<String> = mutableListOf()
        for (i in 0 until 3)
            castLessList.add(movie.cast[i])
        return castLessList
    }

}