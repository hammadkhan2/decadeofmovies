package com.swvl.adecadeofmovies.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swvl.adecadeofmovies.databinding.ItemCastListBinding

class CastListAdapter(private val castList: List<String>) :
    RecyclerView.Adapter<CastListAdapter.GenresViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder {
        val binding = ItemCastListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return GenresViewHolder(binding)
    }

    override fun getItemCount() = castList.size

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {
        holder.binding.tvActorName.text = castList[position]
    }

    inner class GenresViewHolder(val binding: ItemCastListBinding) :
        RecyclerView.ViewHolder(binding.root)

}