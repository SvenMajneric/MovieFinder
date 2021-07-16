package com.example.moviefinder.presentation.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.data.MovieBaseInfo
import com.example.moviefinder.R
import com.example.moviefinder.databinding.FavoriteMovieItemBinding

class FavoritesAdapter: RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    private val items: MutableList<MovieBaseInfo> = mutableListOf()


    var favoriteStatusInteractionListener: ItemInteraction? = null
    var navigationInteractionListener: ItemInteraction? = null

    interface ItemInteraction{
        fun onFavoriteClicked(movieBaseInfo: MovieBaseInfo)
    }

    inner class FavoritesViewHolder(private val itemBinding: FavoriteMovieItemBinding): RecyclerView.ViewHolder(itemBinding.root){
        init {
            itemBinding.root.setOnClickListener {
                navigationInteractionListener?.onFavoriteClicked(items[layoutPosition])
            }
            itemBinding.btnFavorite.setOnClickListener {
                favoriteStatusInteractionListener?.onFavoriteClicked(items[layoutPosition])
            }
        }

        fun bind(movieBaseInfo: MovieBaseInfo){
            itemBinding.btnFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_24)
            Glide.with(itemView)
                .load(movieBaseInfo.poster)
                .fitCenter()
                .into(itemBinding.ivPoster)
            itemBinding.tvMovieTitle.text = movieBaseInfo.title
            itemBinding.tvReleaseYear.text = movieBaseInfo.year
        }
    }

    fun setData(favoritesList: List<MovieBaseInfo>){
        items.clear()
        items.addAll(favoritesList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val itemBinding = FavoriteMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return FavoritesViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

}