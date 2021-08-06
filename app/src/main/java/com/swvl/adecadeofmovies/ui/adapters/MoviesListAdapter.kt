package com.swvl.adecadeofmovies.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.swvl.adecadeofmovies.databinding.ItemMoviesListBinding
import com.swvl.adecadeofmovies.models.Movie

class MoviesListAdapter(
    private val moviesList: List<Movie>,
    private var itemClickListener: ItemClickListener,
    private var isShowYearSectionTitle: Boolean
) :
    RecyclerView.Adapter<MoviesListAdapter.MoviesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding = ItemMoviesListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviesViewHolder(binding)
    }

    override fun getItemCount() = moviesList.size

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        with(holder) {
            with(moviesList[position]) {
                binding.tvRating.text = rating.toString()
                binding.tvMovieTitle.text = title
                binding.tvMovieYear.text = year.toString()

                if (isShowYearSectionTitle) {
                    when {
                        position == 0 -> binding.tvMoviesSectionYear.visibility = View.VISIBLE
                        year != moviesList[position - 1].year -> binding.tvMoviesSectionYear.visibility =
                            View.VISIBLE
                        else -> binding.tvMoviesSectionYear.visibility = View.GONE
                    }

                    binding.tvMoviesSectionYear.text = year.toString()
                } else {
                    binding.tvMoviesSectionYear.visibility = View.GONE
                }

                val transitionName = "$title$position"

                binding.clItemMoviesList.transitionName = transitionName

                itemView.setOnClickListener {
                    itemClickListener.itemClick(this, transitionName, binding.clItemMoviesList)
                }
            }
        }
    }

    inner class MoviesViewHolder(val binding: ItemMoviesListBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface ItemClickListener {
        fun itemClick(movie: Movie, transitionName: String, clItemMoviesList: ConstraintLayout)
    }
}