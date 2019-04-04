package io.github.farhad.popcorn.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.farhad.popcorn.R
import io.github.farhad.popcorn.di.Injectable
import io.github.farhad.popcorn.domain.model.Movie
import io.github.farhad.popcorn.utils.ImageLoader
import kotlinx.android.synthetic.main.fragment_trending_movies.*
import javax.inject.Inject

class TrendingMoviesFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var viewModel: TrendingMoviesViewModel

    private lateinit var adapter: TrendingMoviesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_trending_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TrendingMoviesViewModel::class.java)

        recyclerview_trending_movies.isNestedScrollingEnabled = false
        recyclerview_trending_movies.layoutManager = GridLayoutManager(activity, 2, RecyclerView.VERTICAL, false)
        adapter = TrendingMoviesAdapter(imageLoader) { movie, view ->
            // todo : implement! }
        }
        recyclerview_trending_movies.adapter = adapter

        if (savedInstanceState == null) {
            viewModel.getTrendingMovies()
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.viewState.observe(this, Observer { state ->
            handleViewState(state)
            if (state != null) state.movies?.let {
                addItemsToRecyclerView(it)
            }
        })

        viewModel.errorState.observe(this, Observer { error ->
            error?.let { Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show() }
        })
    }

    private fun handleViewState(state: TrendingMoviesState) {
        if (!state.showLoading) progress_trending_movies.visibility = View.GONE
    }


    private fun addItemsToRecyclerView(list: List<Movie>) {
        adapter.addItems(list)
    }
}