package com.swvl.adecadeofmovies.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val cast: List<String>,
    val genres: List<String>,
    val rating: Double,
    val title: String,
    val year: Int
) : Parcelable