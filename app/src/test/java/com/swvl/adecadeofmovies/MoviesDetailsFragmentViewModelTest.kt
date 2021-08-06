package com.swvl.adecadeofmovies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.swvl.adecadeofmovies.data.repositories.PhotosRepository
import com.swvl.adecadeofmovies.models.Movie
import com.swvl.adecadeofmovies.models.flicker_photo.FlickerPhotoResponse
import com.swvl.adecadeofmovies.models.flicker_photo.Photo
import com.swvl.adecadeofmovies.models.flicker_photo.Photos
import com.swvl.adecadeofmovies.utils.Constants.FLICKER_SUCCESS_RESPONSE_CODE
import com.swvl.adecadeofmovies.utils.Constants.SHOW_LESS
import com.swvl.adecadeofmovies.utils.Constants.VIEW_ALL_CAST
import com.swvl.adecadeofmovies.viewmodels.MoviesDetailsFragmentViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class MoviesDetailsFragmentViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MoviesDetailsFragmentViewModel
    private lateinit var fakeFlickerPhotoSuccessResponse: FlickerPhotoResponse
    private lateinit var fakeFlickerPhotoFailureResponse: FlickerPhotoResponse

    @Mock
    private lateinit var mockedPhotosRepository: PhotosRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        viewModel = MoviesDetailsFragmentViewModel()

        mockedPhotosRepository = mock(PhotosRepository::class.java)

        fakeFlickerPhotoSuccessResponse = FlickerPhotoResponse(
            Photos(1, 1, 1, listOf(Photo(1, "1", 1, 1, 1, "1", "1", "1", "1")), 1),
            "ok"
        )

        fakeFlickerPhotoFailureResponse = FlickerPhotoResponse(
            Photos(1, 1, 1, listOf(Photo(1, "", 1, 1, 1, "", "", "", "")), 1),
            "false"
        )
    }

    @Test
    fun `check with valid request if response is not null`() {

        `when`(
            mockedPhotosRepository.getPhotos(
                anyString(),
                anyInt()
            )
        ).thenReturn(
            MutableLiveData(fakeFlickerPhotoSuccessResponse)
        )

        val observer = Observer<FlickerPhotoResponse> {}

        try {
            viewModel.setMockPhotosRepository(mockedPhotosRepository)
            viewModel.setPhotosByApiCall(
                "F.R.I.E.N.D.S",
                1,
                true
            )
            viewModel.flickerPhotoResponse.observeForever(observer)
            assertThat(viewModel.flickerPhotoResponse.value).isNotNull()
        } finally {
            viewModel.flickerPhotoResponse.removeObserver(observer)
        }
    }

    @Test
    fun `check with valid request if response status is success`() {

        `when`(
            mockedPhotosRepository.getPhotos(
                anyString(),
                anyInt()
            )
        ).thenReturn(
            MutableLiveData(fakeFlickerPhotoSuccessResponse)
        )

        val observer = Observer<FlickerPhotoResponse> {}

        try {
            viewModel.setMockPhotosRepository(mockedPhotosRepository)
            viewModel.setPhotosByApiCall(
                "F.R.I.E.N.D.S",
                1,
                true
            )
            viewModel.flickerPhotoResponse.observeForever(observer)
            assertThat(viewModel.flickerPhotoResponse.value?.stat).isEqualTo(
                FLICKER_SUCCESS_RESPONSE_CODE
            )
        } finally {
            viewModel.flickerPhotoResponse.removeObserver(observer)
        }
    }

    @Test
    fun `check with valid request if response status is failure`() {

        `when`(
            mockedPhotosRepository.getPhotos(
                anyString(),
                anyInt()
            )
        ).thenReturn(
            MutableLiveData(fakeFlickerPhotoFailureResponse)
        )

        val observer = Observer<FlickerPhotoResponse> {}

        try {
            viewModel.setMockPhotosRepository(mockedPhotosRepository)
            viewModel.setPhotosByApiCall(
                "F.R.I.E.N.D.S",
                1,
                true
            )
            viewModel.flickerPhotoResponse.observeForever(observer)
            assertThat(viewModel.flickerPhotoResponse.value?.stat).isNotEqualTo(
                FLICKER_SUCCESS_RESPONSE_CODE
            )
        } finally {
            viewModel.flickerPhotoResponse.removeObserver(observer)
        }
    }

    @Test
    fun `check with valid request if response is accurate as mocked`() {

        `when`(
            mockedPhotosRepository.getPhotos(
                anyString(),
                anyInt()
            )
        ).thenReturn(
            MutableLiveData(fakeFlickerPhotoSuccessResponse)
        )

        val observer = Observer<FlickerPhotoResponse> {}

        try {
            viewModel.setMockPhotosRepository(mockedPhotosRepository)
            viewModel.setPhotosByApiCall(
                "F.R.I.E.N.D.S",
                1,
                true
            )
            viewModel.flickerPhotoResponse.observeForever(observer)
            assertThat(viewModel.flickerPhotoResponse.value).isEqualTo(
                fakeFlickerPhotoSuccessResponse
            )
        } finally {
            viewModel.flickerPhotoResponse.removeObserver(observer)
        }
    }

    @Test
    fun `check if movie is set by setMovie(movie)`() {
        val observer = Observer<Movie> {}
        try {
            val movie = Movie(
                listOf(
                    "Rachel Green",
                    "Monica Geller",
                    "Chandler Bing",
                    "Ross Geller",
                    "Joey Tribbiani",
                    "Phoebe Buffay"
                ),
                listOf(
                    "Comedy",
                    "Sitcom"
                ),
                5.0,
                "F.R.I.E.N.D.S",
                1994
            )

            viewModel.setMovie(movie)
            viewModel.movieMutableLiveData.observeForever(observer)

            assertThat(viewModel.movieMutableLiveData.value).isEqualTo(movie)
        } finally {
            viewModel.movieMutableLiveData.removeObserver(observer)
        }
    }

    @Test
    fun `check cast list when movie cast size is greater than 3`() {
        val observer = Observer<MutableList<String>> {}
        try {
            val movie = Movie(
                listOf(
                    "Rachel Green",
                    "Monica Geller",
                    "Chandler Bing",
                    "Ross Geller",
                    "Joey Tribbiani",
                    "Phoebe Buffay"
                ),
                listOf(
                    "Comedy",
                    "Sitcom"
                ),
                5.0,
                "F.R.I.E.N.D.S",
                1994
            )

            viewModel.setMovie(movie)
            viewModel.movieCastList.observeForever(observer)

            assertThat(viewModel.movieCastList.value?.size).isEqualTo(3)
        } finally {
            viewModel.movieCastList.removeObserver(observer)
        }
    }

    @Test
    fun `check cast list size when movie cast size is equal to 3`() {
        val observer = Observer<MutableList<String>> {}
        try {
            val movie = Movie(
                listOf(
                    "Rachel Green",
                    "Monica Geller",
                    "Chandler Bing"
                ),
                listOf(
                    "Comedy",
                    "Sitcom"
                ),
                5.0,
                "F.R.I.E.N.D.S",
                1994
            )

            viewModel.setMovie(movie)
            viewModel.movieCastList.observeForever(observer)

            assertThat(viewModel.movieCastList.value?.size).isEqualTo(3)
        } finally {
            viewModel.movieCastList.removeObserver(observer)
        }
    }

    @Test
    fun `check cast list size when movie cast size is less than 3`() {
        val observer = Observer<MutableList<String>> {}
        try {
            val movie = Movie(
                listOf(
                    "Rachel Green",
                    "Monica Geller"
                ),
                listOf(
                    "Comedy",
                    "Sitcom"
                ),
                5.0,
                "F.R.I.E.N.D.S",
                1994
            )

            viewModel.setMovie(movie)
            viewModel.movieCastList.observeForever(observer)

            assertThat(viewModel.movieCastList.value?.size).isLessThan(3)
        } finally {
            viewModel.movieCastList.removeObserver(observer)
        }
    }

    @Test
    fun `check showAllCast text when click text is View All Cast`() {
        val observer = Observer<String> {}
        try {
            val movie = Movie(
                listOf(
                    "Rachel Green",
                    "Monica Geller"
                ),
                listOf(
                    "Comedy",
                    "Sitcom"
                ),
                5.0,
                "F.R.I.E.N.D.S",
                1994
            )

            viewModel.setMovie(movie)
            viewModel.castClick(VIEW_ALL_CAST)
            viewModel.showAllCastText.observeForever(observer)

            assertThat(viewModel.showAllCastText.value).isEqualTo(SHOW_LESS)
        } finally {
            viewModel.showAllCastText.removeObserver(observer)
        }
    }

    @Test
    fun `check showAllCast text when click text is Show less`() {
        val observer = Observer<String> {}
        try {
            val movie = Movie(
                listOf(
                    "Rachel Green",
                    "Monica Geller",
                    "Chandler Bing",
                    "Ross Geller",
                    "Joey Tribbiani",
                    "Phoebe Buffay"
                ),
                listOf(
                    "Comedy",
                    "Sitcom"
                ),
                5.0,
                "F.R.I.E.N.D.S",
                1994
            )

            viewModel.setMovie(movie)
            viewModel.castClick(SHOW_LESS)
            viewModel.showAllCastText.observeForever(observer)

            assertThat(viewModel.showAllCastText.value).isEqualTo(VIEW_ALL_CAST)
        } finally {
            viewModel.showAllCastText.removeObserver(observer)
        }
    }

    @Test
    fun `check noNetworkConnection value if network connection is set to false`() {
        val observer = Observer<Boolean> {}

        try {
            viewModel.setPhotosByApiCall(
                "F.R.I.E.N.D.S",
                1,
                false
            )
            viewModel.noNetworkConnection.observeForever(observer)

            assertThat(viewModel.noNetworkConnection.value).isFalse()
        } finally {
            viewModel.noNetworkConnection.removeObserver(observer)
        }
    }

    @Test
    fun `check noNetworkConnection value if network connection is set to true`() {
        val observer = Observer<Boolean> {}

        try {
            viewModel.setPhotosByApiCall(
                "F.R.I.E.N.D.S",
                1,
                true
            )
            viewModel.noNetworkConnection.observeForever(observer)

            assertThat(viewModel.noNetworkConnection.value).isTrue()
        } finally {
            viewModel.noNetworkConnection.removeObserver(observer)
        }
    }
}