package io.github.farhad.popcorn.ui.details

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.farhad.popcorn.R
import io.github.farhad.popcorn.domain.model.Role
import io.github.farhad.popcorn.utils.ImageLoader
import kotlinx.android.synthetic.main.item_performer_role.view.*

class MovieRolesAdapter constructor(private val imageLoader: ImageLoader, val resources: Resources) :
    RecyclerView.Adapter<MovieRolesAdapter.ViewHolder>() {

    val roles: MutableList<Role> = mutableListOf()

    fun addItems(list: List<Role>) {
        if(list.any { it in roles }) {
            roles.addAll(list)
            notifyItemRangeInserted(0, list.size)
        }
    }

    override fun getItemCount(): Int = roles.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_performer_role, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val role = roles[holder.adapterPosition]
        holder.bind(role, imageLoader, resources)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(role: Role, imageLoader: ImageLoader, resources: Resources) = with(itemView) {
            role.imageUrl?.let { imageLoader.loadCircular(it, imageview,R.drawable.ic_person) }
            textview_name.text = role.name
            textview_character.text = resources.getString(R.string.crew_formatted, role.department, role.job)
        }
    }

}