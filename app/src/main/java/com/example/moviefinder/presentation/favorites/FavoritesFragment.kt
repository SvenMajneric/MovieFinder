package com.example.moviefinder.presentation.favorites

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.core.data.MovieBaseInfo
import com.example.moviefinder.R
import com.example.moviefinder.databinding.FragmentFavoritesBinding
import com.example.moviefinder.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding: FragmentFavoritesBinding get() = _binding!!

    private val viewModel: FavoritesViewModel by activityViewModels()

    private val favoritesAdapter = FavoritesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoritesBinding.bind(view)

        binding.rvFavorites.adapter = favoritesAdapter
        setupListeners()
        bind()
        viewModel.getAllFavoriteMovies()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupListeners(){
        binding.searchBar.setOnSearchClickListener {
            findNavController().navigate(R.id.action_favoritesFragment_to_searchFragment)
        }
        favoritesAdapter.favoriteStatusInteractionListener = object : FavoritesAdapter.ItemInteraction{
            override fun onFavoriteClicked(movieBaseInfo: MovieBaseInfo) {
                    viewModel.removeMovieFromFavorites(movieBaseInfo)

            }
        }
        favoritesAdapter.navigationInteractionListener = object : FavoritesAdapter.ItemInteraction{
            override fun onFavoriteClicked(movieBaseInfo: MovieBaseInfo) {
                val action = FavoritesFragmentDirections.actionFavoritesFragmentToMovieDetailsFragment(movieBaseInfo.id)
                findNavController().navigate(action)
            }

        }
        binding.btnClearFavorites.setOnClickListener {
            AlertDialog.Builder(activity)
                .setTitle("Clear favorites?")
                .setMessage("All of your favorites will be deleted.")
                .setPositiveButton("Yes") { dialog, which ->
                    viewModel.clearFavorites()
                }
                .setNegativeButton("No") { dialog, which ->

                }
                .show()
        }
    }

    private fun bind(){

        viewModel.notificationLiveData.observe(viewLifecycleOwner, { notificationString ->
            binding.progressBar.visibility = View.GONE
            if (notificationString != null){
                val shouldShowNotification = notificationString.hasBeenHandled()
                if (!shouldShowNotification) {
                    Toast.makeText(activity, notificationString.contentIfNotHandled.toString(), Toast.LENGTH_SHORT).show()
                }
            }

        })

        viewModel.favorites.observe(viewLifecycleOwner, {
            when(it){
                is Resource.Error -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(activity, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    setRecyclerViewData(it.data!!)
                }
            }
        })
    }

    private fun setRecyclerViewData(movieList : List<MovieBaseInfo>){
        favoritesAdapter.setData(movieList)
    }

}