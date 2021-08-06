package com.swvl.adecadeofmovies.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialElevationScale
import com.swvl.adecadeofmovies.R
import com.swvl.adecadeofmovies.databinding.FragmentMoviesListBinding
import com.swvl.adecadeofmovies.models.Movie
import com.swvl.adecadeofmovies.ui.adapters.MoviesListAdapter
import com.swvl.adecadeofmovies.utils.AppUtils.getMoviesSortedByYearWithCount
import com.swvl.adecadeofmovies.utils.Constants
import com.swvl.adecadeofmovies.utils.Constants.MOVIES_JSON_FILE_NAME
import com.swvl.adecadeofmovies.utils.Constants.REPLY_MOTION_DURATION
import com.swvl.adecadeofmovies.viewmodels.MoviesListFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesListFragment : Fragment(), SearchView.OnQueryTextListener {
    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!
    private lateinit var thisMoviesList: MutableList<Movie>
    private val staticMovies: MutableList<Movie> = mutableListOf()

    private val moviesListFragmentViewModel: MoviesListFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        val view = binding.root
        setHasOptionsMenu(true)

        observeDataFromViewModel()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun observeDataFromViewModel() {
        with(moviesListFragmentViewModel) {
            setMovies(MOVIES_JSON_FILE_NAME)
            getMovies().observe(viewLifecycleOwner, { moviesList ->
                thisMoviesList = moviesList.toMutableList()
                if (staticMovies.isNullOrEmpty())
                    staticMovies.addAll(thisMoviesList)
                with(binding.rvMoviesList) {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = getMoviesListAdapter(false)

                    val divider =
                        DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
                    divider.setDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.item_separator
                        )!!
                    )
                    addItemDecoration(divider)
                }
            })
        }
    }

    private fun getMoviesListAdapter(isShowYearSectionTitle: Boolean): MoviesListAdapter {
        return MoviesListAdapter(
            thisMoviesList,
            object : MoviesListAdapter.ItemClickListener {
                override fun itemClick(
                    movie: Movie,
                    transitionName: String,
                    clItemMoviesList: ConstraintLayout
                ) {

                    exitTransition = MaterialElevationScale(false).apply {
                        duration = REPLY_MOTION_DURATION
                    }
                    reenterTransition = MaterialElevationScale(true).apply {
                        duration = REPLY_MOTION_DURATION
                    }

                    val extras = FragmentNavigatorExtras(clItemMoviesList to transitionName)
                    val action =
                        MoviesListFragmentDirections.actionAddDetailsFragment(
                            movie.title,
                            movie,
                            transitionName
                        )
                    findNavController().navigate(action, extras)
                }
            },
            isShowYearSectionTitle
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_toolbar_menu, menu)
        val menuItemSearch = menu.findItem(R.id.menu_item_search)
        val searchView: SearchView = menuItemSearch.actionView as SearchView
        searchView.queryHint = getString(R.string.search_hint)

        searchView.setOnQueryTextListener(this)

        searchView.setOnCloseListener {
            binding.rvMoviesList.adapter = getMoviesListAdapter(false)
            false
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(query: String?): Boolean {

        thisMoviesList.clear()
        thisMoviesList.addAll(staticMovies)

        if (!query.isNullOrEmpty()) {
            val filteredList = thisMoviesList.filter { it.title.contains(query, true) }
            val sortedMovies = getMoviesSortedByYearWithCount(filteredList.toMutableList())
            thisMoviesList.clear()
            thisMoviesList.addAll(sortedMovies)
            binding.rvMoviesList.adapter = getMoviesListAdapter(true)
        } else {
            binding.rvMoviesList.adapter = getMoviesListAdapter(false)
        }
        return true
    }
}