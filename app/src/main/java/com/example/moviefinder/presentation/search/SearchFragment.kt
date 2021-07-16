package com.example.moviefinder.presentation.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.core.data.MovieBaseInfo
import com.example.moviefinder.R
import com.example.moviefinder.databinding.FragmentSearchBinding
import com.example.moviefinder.util.Resource
import dagger.hilt.android.AndroidEntryPoint

const val TAG = "SearchFragment"

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding get() = _binding!!

    private val viewModel: SearchViewModel by activityViewModels()

    private val searchAdapter = SearchAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)

        binding.rvSearchResults.adapter = searchAdapter
        setupListeners()
        bind()
        viewModel.getAllFavoriteMovies()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllFavoriteMovies()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerViewData(moviesGroupByYear: Map<String, List<MovieBaseInfo>>){
        searchAdapter.setData(moviesGroupByYear)
    }

    private fun checkRecyclerViewFavorites(favoriteList : List<MovieBaseInfo>){
        searchAdapter.setFavorites(favoriteList)
    }

    private fun setupListeners(){
        searchAdapter.itemNavigationListener = object : SearchAdapter.ItemInteractionListener{
            override fun onItemClicked(movieBaseInfo: MovieBaseInfo) {
                val action = SearchFragmentDirections.actionSearchFragmentToMovieDetailsFragment(movieBaseInfo.id)
                findNavController().navigate(action)
            }

        }

        searchAdapter.itemInteractionListener = object : SearchAdapter.ItemInteractionListener{
            override fun onItemClicked(movieBaseInfo: MovieBaseInfo) {
                if (viewModel.favorites.value?.data?.contains(movieBaseInfo) == true){
                    viewModel.removeMovieFromFavorites(movieBaseInfo)
                }else {
                    viewModel.addMovieToFavorites(movieBaseInfo)
                }

            }

        }

        binding.searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) viewModel.fetchMoviesFromApi(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }



    fun bind(){

        viewModel.movieListGroupedByYear.observe(viewLifecycleOwner, { moviesMapGroupedByYear ->
            when(moviesMapGroupedByYear){
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(activity, "Fetching movies failed! Try another search parameter.", Toast.LENGTH_LONG).show()
                    Log.e(TAG, moviesMapGroupedByYear.message.toString())
                }
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    moviesMapGroupedByYear.data?.let { setupRecyclerViewData(it) }
                }
            }
        })

        viewModel.favorites.observe(viewLifecycleOwner, { favoritesList ->
            when(favoritesList){
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Log.e(TAG, favoritesList.message.toString())
                }
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    favoritesList.data?.let { checkRecyclerViewFavorites(it) }
                }
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