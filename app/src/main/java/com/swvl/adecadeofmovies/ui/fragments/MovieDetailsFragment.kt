package com.swvl.adecadeofmovies.ui.fragments

import android.animation.LayoutTransition
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import com.swvl.adecadeofmovies.R
import com.swvl.adecadeofmovies.databinding.FragmentMovieDetialsBinding
import com.swvl.adecadeofmovies.ui.adapters.CastListAdapter
import com.swvl.adecadeofmovies.ui.adapters.GenreListAdapter
import com.swvl.adecadeofmovies.ui.adapters.PhotosListAdapter
import com.swvl.adecadeofmovies.utils.AppUtils.getFlickrPhotoUrl
import com.swvl.adecadeofmovies.utils.AppUtils.isNetworkAvailable
import com.swvl.adecadeofmovies.utils.AppUtils.showSnackBar
import com.swvl.adecadeofmovies.utils.Constants.FLICKER_SUCCESS_RESPONSE_CODE
import com.swvl.adecadeofmovies.utils.Constants.HIDE_SHOW_CAST_TEXT
import com.swvl.adecadeofmovies.utils.Constants.REPLY_MOTION_DURATION
import com.swvl.adecadeofmovies.viewmodels.MoviesDetailsFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {
    private var _binding: FragmentMovieDetialsBinding? = null
    private val binding get() = _binding!!
    private val args: MovieDetailsFragmentArgs by navArgs()

    private val moviesDetailsFragmentViewModel: MoviesDetailsFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition =
            MaterialContainerTransform().apply {
                drawingViewId = R.id.nav_host_fragment_container
                duration = REPLY_MOTION_DURATION
                scrimColor = Color.TRANSPARENT
                setAllContainerColors(Color.TRANSPARENT)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetialsBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.tvMovieTitle.transitionName = args.transitionName

        setHasOptionsMenu(false)
        setAutoAnimateLayoutChanges()

        moviesDetailsFragmentViewModel.setMovie(args.movie)
        setMovieUIElements()
        addObservers()

        return view
    }

    private fun addObservers() {
        with(moviesDetailsFragmentViewModel) {
            movieMutableLiveData.observe(viewLifecycleOwner, {
                it?.let { movieNonNull ->
                    with(binding) {
                        with(movieNonNull) {
                            tvMovieTitle.text = title
                            tvMovieRating.text = rating.toString()
                            tvMovieYear.text = year.toString()
                        }
                    }

                    binding.rvGenre.adapter = GenreListAdapter(movieNonNull.genres)
                }
            })

            movieCastList.observe(
                viewLifecycleOwner,
                { it?.let { binding.rvCast.adapter = CastListAdapter(it) } })

            flickerPhotoResponse.observe(
                viewLifecycleOwner,
                {
                    it?.let { flickerPhotoResponse ->
                        if (flickerPhotoResponse.stat == FLICKER_SUCCESS_RESPONSE_CODE) {
                            if (flickerPhotoResponse.photos.photo.isNotEmpty()
                            ) {
                                binding.rvMoviePhotos.adapter =
                                    PhotosListAdapter(flickerPhotoResponse.photos.photo)

                                getFlickrPhotoUrl(flickerPhotoResponse.photos.photo[0])?.let { url ->
                                    Glide
                                        .with(requireContext())
                                        .load(url)
                                        .centerCrop()
                                        .into(binding.ivMovieBanner)
                                }
                                binding.tvPhotosTitle.text = getString(R.string.photos)
                                binding.rvMoviePhotos.visibility = View.VISIBLE
                            } else {
                                binding.tvPhotosTitle.text = getString(R.string.no_photos_error)
                                binding.rvMoviePhotos.visibility = View.GONE
                            }
                        } else requireActivity().showSnackBar(getString(R.string.api_fetch_error))

                        binding.pbPhotos.visibility = View.GONE
                    }
                })

            noNetworkConnection.observe(viewLifecycleOwner, {
                it?.let { isConnected ->
                    if (!isConnected) {
                        requireActivity().showSnackBar(getString(R.string.network_error))
                        binding.pbPhotos.visibility = View.GONE
                    }
                }
            })

            showAllCastText.observe(viewLifecycleOwner, {
                it?.let { showCastText ->
                    when (showCastText) {
                        HIDE_SHOW_CAST_TEXT -> {
                            binding.tvShowAllCast.visibility = View.GONE
                        }
                        else -> {
                            binding.tvShowAllCast.text = showCastText
                        }
                    }
                }
            })
        }
    }

    private fun setMovieUIElements() {

        binding.tvShowAllCast.setOnClickListener {
            moviesDetailsFragmentViewModel.castClick(binding.tvShowAllCast.text.toString())
        }

        val genresLayoutManager = FlexboxLayoutManager(requireContext())
        with(genresLayoutManager) {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            alignItems = AlignItems.STRETCH
        }
        with(binding.rvGenre) {
            hasFixedSize()
            layoutManager = genresLayoutManager
        }

        val castLayoutManager = FlexboxLayoutManager(requireContext())
        with(castLayoutManager) {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            alignItems = AlignItems.STRETCH
        }

        with(binding.rvCast) {
            hasFixedSize()
            layoutManager = castLayoutManager
        }

        val photosLayoutManager = GridLayoutManager(requireContext(), 2)
        with(binding.rvMoviePhotos) {
            hasFixedSize()
            layoutManager = photosLayoutManager
        }

        moviesDetailsFragmentViewModel.setPhotosByApiCall(
            args.movieTitle,
            1,
            isNetworkAvailable()
        )
    }

    private fun setAutoAnimateLayoutChanges() {
        (binding.nsvDetailsFragmentRoot as ViewGroup).layoutTransition
            .enableTransitionType(LayoutTransition.CHANGING)

        (binding.clDetailsFragmentParent as ViewGroup).layoutTransition
            .enableTransitionType(LayoutTransition.CHANGING)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}