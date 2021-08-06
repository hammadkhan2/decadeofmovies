package com.swvl.adecadeofmovies.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.swvl.adecadeofmovies.databinding.ItemPhotosListBinding
import com.swvl.adecadeofmovies.models.flicker_photo.Photo
import com.swvl.adecadeofmovies.utils.AppUtils.getFlickrPhotoUrl

class PhotosListAdapter(
    private val photosList: List<Photo>
) : RecyclerView.Adapter<PhotosListAdapter.PhotosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val binding = ItemPhotosListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotosViewHolder(binding)
    }

    override fun getItemCount() = photosList.size

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        getFlickrPhotoUrl(photosList[position])?.let { url ->
            Glide
                .with(holder.itemView.context)
                .load(url)
                .centerCrop()
                .into(holder.binding.ivMoviePhoto)
        }
    }

    inner class PhotosViewHolder(val binding: ItemPhotosListBinding) :
        RecyclerView.ViewHolder(binding.root)

}