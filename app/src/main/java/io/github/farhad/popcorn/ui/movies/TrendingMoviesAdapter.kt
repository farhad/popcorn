package io.github.farhad.popcorn.ui.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.farhad.popcorn.R
import io.github.farhad.popcorn.domain.model.Movie
import io.github.farhad.popcorn.utils.ImageLoader
import kotlinx.android.synthetic.main.item_movie_trending.view.*

class TrendingMoviesAdapter constructor(
    private val imageLoader: ImageLoader,
    private val onMovieSelected: (Movie) -> Unit
) :
    RecyclerView.Adapter<TrendingMoviesAdapter.ViewHolder>() {

    val movies: MutableList<Movie> = mutableListOf()

    fun addItems(list: List<Movie>) {

        if (!list.any { it in movies }) {
            val oldSize = movies.size
            movies.addAll(list)
            notifyItemRangeInserted(oldSize, list.size) // using oldSize-1 makes the last item in the list animates
        }
    }

    override fun getItemCount(): Int = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_trending, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[holder.adapterPosition]
        holder.bind(movie, imageLoader, onMovieSelected)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(movie: Movie, imageLoader: ImageLoader, listener: (Movie) -> Unit) = with(itemView) {
            textview_title_movie.text = movie.title
            textview_overview_movie.text = movie.overview
            textview_voteaverage_movie.text = movie.voteAverage.toString()

            movie.posterUrl?.let { imageLoader.load(it, imageview_movie_trending,R.drawable.ic_film_roll) }
            setOnClickListener { listener(movie) }
        }
    }
}