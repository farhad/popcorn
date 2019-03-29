package io.github.farhad.popcorn.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.farhad.popcorn.R
import io.github.farhad.popcorn.di.Injectable
import io.github.farhad.popcorn.domain.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_movie_list.*
import javax.inject.Inject

class MovieListFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MovieViewModel

    private lateinit var disposable: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()

        disposable = viewModel.getUpcomingMovies().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { addItemsToRecyclerView(it) },
                { Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG).show() })
    }

    private fun addItemsToRecyclerView(list: List<Movie>) {
        recyclerview_movie_list.isNestedScrollingEnabled = false
        recyclerview_movie_list.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerview_movie_list.adapter = MovieAdapter(list)
    }

    class MovieAdapter(private val list: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

        override fun getItemCount(): Int = list.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_upcoming, parent, false)
            return MovieViewHolder(view)
        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

            holder.tvMovieTitle.text = list[holder.adapterPosition].title
        }

        class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val tvMovieTitle = itemView.findViewById<AppCompatTextView>(R.id.textview_title_movie)
        }
    }
}