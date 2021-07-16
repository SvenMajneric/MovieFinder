package com.example.moviefinder.presentation.moviedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.moviefinder.R
import com.example.moviefinder.databinding.FragmentMovieDetailsBinding


class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val  binding: FragmentMovieDetailsBinding get() = _binding!!

    private val viewModel : MovieDetailsViewModel by activityViewModels()

    private val args: MovieDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieDetailsBinding.bind(view)

        bind()
        viewModel.fetchMovieDetailsById(args.imdbId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun bind (){
        viewModel.movieDetails.observe(viewLifecycleOwner, {
            binding.apply {
                progressBar.visibility = View.GONE
                tvMovieName.text = it.title
                tvImdbRating.text = it.rating
                tvDatePublished.text = it.releaseDate
                tvMovieDuration.text = it.duration
                tvMovieGenres.text = it.genres
                tvMovieDescription.text = it.description
                Glide.with(this@MovieDetailsFragment)
                    .load(it.posterUrl)
                    .into(ivMoviePoster)
            }
        })

        viewModel.notificationLiveData.observe(viewLifecycleOwner, { notificationString ->
            binding.progressBar.visibility = View.GONE
            if (notificationString != null){
                val shouldShowNotification = notificationString.hasBeenHandled()
                if (!shouldShowNotification) {
                    Toast.makeText(activity, notificationString.contentIfNotHandled.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}