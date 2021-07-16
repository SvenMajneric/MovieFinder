package com.example.moviefinder.presentation.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.data.MovieBaseInfo
import com.example.moviefinder.R
import com.example.moviefinder.databinding.SearchMovieItemBinding

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private val items: MutableMap<String, List<MovieBaseInfo>> = mutableMapOf()

    private val favoriteItems: MutableList<MovieBaseInfo> = mutableListOf()



    var itemNavigationListener: ItemInteractionListener? = null
    var itemInteractionListener: ItemInteractionListener? = null

    interface ItemInteractionListener{
        fun onItemClicked(movieBaseInfo: MovieBaseInfo)
    }

    inner class SearchViewHolder(private val itemBinding: SearchMovieItemBinding): RecyclerView.ViewHolder(itemBinding.root){
        init {
            itemBinding.root.setOnClickListener {
                itemNavigationListener?.onItemClicked(items.values.flatten()[layoutPosition])
            }
            itemBinding.btnFavorite.setOnClickListener {
                itemInteractionListener?.onItemClicked(items.values.flatten()[layoutPosition])
            }
        }

        fun bind(movieBaseInfo: MovieBaseInfo){
            if(adapterPosition in -1 until 1 || items.values.flatten()[adapterPosition].year != items.values.flatten()[adapterPosition-1].year){
                itemBinding.tvReleaseYear.visibility = View.VISIBLE
            }
            if (movieBaseInfo in favoriteItems){
                itemBinding.btnFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_24)
            } else {
                itemBinding.btnFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)
            }
            Glide.with(itemView)
                .load(movieBaseInfo.poster)
                .fitCenter()
                .into(itemBinding.ivPoster)
            itemBinding.tvMovieTitle.text = movieBaseInfo.title
            itemBinding.tvReleaseYear.text = movieBaseInfo.year
        }
    }



    fun setData(movieMapGroupedByYear: Map<String, List<MovieBaseInfo>>){
        items.clear()
        items.putAll(movieMapGroupedByYear)
        notifyDataSetChanged()
    }

    fun setFavorites(favoriteMovieList: List<MovieBaseInfo>){
        favoriteItems.clear()
        favoriteItems.addAll(favoriteMovieList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemMovieBinding = SearchMovieItemBinding.inflate(layoutInflater, parent, false )
        return SearchViewHolder(itemMovieBinding)

    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
            holder.bind(items.values.flatten()[position])
    }

    override fun getItemCount(): Int {
        return items.values.size
    }

}