package com.swvl.adecadeofmovies.data.network

import com.swvl.adecadeofmovies.BuildConfig
import com.swvl.adecadeofmovies.models.flicker_photo.FlickerPhotoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(BuildConfig.URL_GET_PHOTOS)
    suspend fun getPhotos(
        @Query("method") method: String,
        @Query("api_key") api_key: String,
        @Query("format") format: String,
        @Query("nojsoncallback") nojsoncallback: Int,
        @Query("text") text: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): FlickerPhotoResponse
}