package io.github.farhad.popcorn.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.github.farhad.popcorn.R
import io.github.farhad.popcorn.di.Injectable
import io.github.farhad.popcorn.utils.ImageLoader
import kotlinx.android.synthetic.main.fragment_trending_movies.*
import kotlinx.android.synthetic.main.layout_loading.*
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
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(TrendingMoviesViewModel::class.java)

        initViews()

        if (savedInstanceState == null) {
            viewModel.getTrendingMovies()
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.viewState.observe(this, Observer { state ->
            handleViewState(state)
            if (state != null) state.movies?.let {
                adapter.addItems(it)
            }
        })

        viewModel.errorState.observe(this, Observer { error ->
            error?.let {
                button_try_again.visibility = View.VISIBLE
                Snackbar.make(recyclerview_trending_movies, it.message.toString(), Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun initViews() {
        button_try_again.visibility = View.GONE
        progressbar.visibility = View.VISIBLE

        button_try_again.setOnClickListener {
            viewModel.getTrendingMovies()
        }

        recyclerview_trending_movies.isNestedScrollingEnabled = false
        recyclerview_trending_movies.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        adapter = TrendingMoviesAdapter(imageLoader) { movie ->
            viewModel.setSelectedMovieId(movie.id)
        }

        recyclerview_trending_movies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                    if (visibleItemCount + firstVisibleItem >= totalItemCount
                        && visibleItemCount != totalItemCount
                        && visibleItemCount > 0
                    ) {
                        viewModel.getTrendingMovies()
                    }
                }
            }
        })

        recyclerview_trending_movies.adapter = adapter
    }

    private fun handleViewState(state: TrendingMoviesState) {
        if (!state.showLoading) {
            button_try_again.visibility = View.GONE
            progressbar.visibility = View.GONE
        }
    }
}