package io.github.farhad.popcorn.ui.details

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.farhad.popcorn.R
import io.github.farhad.popcorn.domain.model.Performer
import io.github.farhad.popcorn.utils.ImageLoader
import kotlinx.android.synthetic.main.item_performer_role.view.*

class MoviePerformersAdapter(private val imageLoader: ImageLoader, private val resources: Resources) :
    RecyclerView.Adapter<MoviePerformersAdapter.ViewHolder>() {

    val performers: MutableList<Performer> = mutableListOf()

    fun addItems(list: List<Performer>) {
        list.map {
            if (!performers.contains(it)) {
                val pos = performers.size
                performers.add(it)
                notifyItemRangeInserted(pos, 1)
            }
            return@map
        }
    }

    override fun getItemCount(): Int = performers.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_performer_role, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val performer = performers[holder.adapterPosition]
        holder.bind(performer, imageLoader, resources)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(performer: Performer, imageLoader: ImageLoader, resources: Resources) = with(itemView) {
            performer.imageUrl?.let { imageLoader.loadCircular(it, imageview, R.drawable.ic_person) }
            textview_name.text = performer.name
            textview_character.text = resources.getString(R.string.cast_formatted, performer.character)
        }
    }

}