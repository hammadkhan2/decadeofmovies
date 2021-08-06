package com.swvl.adecadeofmovies.data.repositories

import androidx.lifecycle.LiveData
import com.swvl.adecadeofmovies.models.flicker_photo.FlickerPhotoResponse

interface PhotosRepository {
    fun getPhotos(movieName: String, pageNumber: Int): LiveData<FlickerPhotoResponse>
}

class PhotosRepositoryImpl : PhotosRepository {
    override fun getPhotos(movieName: String, pageNumber: Int): LiveData<FlickerPhotoResponse> {
        return RepositorySingleton.getPhotosFromApi(
            movieName,
            pageNumber
        )
    }
}