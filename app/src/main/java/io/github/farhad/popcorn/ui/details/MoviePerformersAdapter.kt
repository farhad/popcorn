package io.github.farhad.popcorn.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.farhad.popcorn.R
import io.github.farhad.popcorn.domain.model.Performer
import io.github.farhad.popcorn.utils.ImageLoader
import kotlinx.android.synthetic.main.item_movie_performer.*

class MoviePerformersAdapter constructor(private val imageLoader: ImageLoader) :
    RecyclerView.Adapter<MoviePerformersAdapter.ViewHolder>() {

    val performers: MutableList<Performer> = mutableListOf()

    fun addItems(list: List<Performer>) {
        performers.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    override fun getItemCount(): Int = performers.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_performer, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val performer = performers[holder.adapterPosition]
        holder.bind(performer, imageLoader)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(performer: Performer, imageLoader: ImageLoader) {
            performer.imageUrl?.let { imageLoader.loadCircular(it, imageview_performer) }
            textview_performer_name.text = performer.name
            textview_performer_character.text = "as ${performer.character}"
        }
    }

}