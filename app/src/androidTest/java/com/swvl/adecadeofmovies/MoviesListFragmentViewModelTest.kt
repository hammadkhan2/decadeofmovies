package com.swvl.adecadeofmovies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.swvl.adecadeofmovies.models.Movie
import com.swvl.adecadeofmovies.utils.Constants.MOVIES_JSON_FILE_NAME
import com.swvl.adecadeofmovies.viewmodels.MoviesListFragmentViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

class MoviesListFragmentViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MoviesListFragmentViewModel

    @Before
    fun setUp() {
        viewModel = MoviesListFragmentViewModel()
    }

    @Test
    fun checkIfMoviesIsSetWithDataFromJsonFile() {
        val observer = Observer<List<Movie>> {}
        try {
            viewModel.setMovies(MOVIES_JSON_FILE_NAME)
            viewModel.movies.observeForever(observer)

            assertThat(viewModel.movies.value).isNotEmpty()
        } finally {
            viewModel.movies.removeObserver(observer)
        }
    }
}