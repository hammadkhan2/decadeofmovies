package com.swvl.adecadeofmovies.data.repositories

import androidx.lifecycle.LiveData
import com.swvl.adecadeofmovies.BuildConfig
import com.swvl.adecadeofmovies.data.network.MyRetrofitBuilder
import com.swvl.adecadeofmovies.models.flicker_photo.FlickerPhotoResponse
import com.swvl.adecadeofmovies.utils.Constants
import kotlinx.coroutines.*

object RepositorySingleton {

    var job: CompletableJob? = null

    fun getPhotosFromApi(movieName: String, pageNumber: Int): LiveData<FlickerPhotoResponse> {
        job = Job()
        return object : LiveData<FlickerPhotoResponse>() {
            override fun onActive() {
                super.onActive()
                job?.let { apiCallJob ->
                    CoroutineScope(Dispatchers.IO + apiCallJob).launch {

                        val flickerPhotoResponse = MyRetrofitBuilder.apiService.getPhotos(
                            Constants.FLICKER_PHOTOS_METHOD_NAME,
                            BuildConfig.FLICKER_KEY,
                            Constants.FLICKER_RESPONSE_TYPE,
                            Constants.FLICKER_NO_CALLBACK,
                            movieName,
                            pageNumber,
                            Constants.FLICKER_PER_PAGE_COUNT
                        )

                        withContext(Dispatchers.Main) {
                            value = flickerPhotoResponse
                            apiCallJob.complete()
                        }
                    }
                }
            }
        }
    }

    fun cancelJob() {
        job?.cancel()
    }
}