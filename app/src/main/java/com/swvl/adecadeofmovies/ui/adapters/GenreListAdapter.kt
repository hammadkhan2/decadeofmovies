package com.swvl.adecadeofmovies.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swvl.adecadeofmovies.databinding.ItemGenreListBinding

class GenreListAdapter(private val genreList: List<String>) :
    RecyclerView.Adapter<GenreListAdapter.GenresViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder {
        val binding = ItemGenreListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return GenresViewHolder(binding)
    }

    override fun getItemCount() = genreList.size

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {
        holder.binding.tvGenre.text = genreList[position]
    }

    inner class GenresViewHolder(val binding: ItemGenreListBinding) :
        RecyclerView.ViewHolder(binding.root)

}