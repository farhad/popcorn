package io.github.farhad.popcorn.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.farhad.popcorn.R
import io.github.farhad.popcorn.di.Injectable
import io.github.farhad.popcorn.domain.model.Movie
import io.github.farhad.popcorn.utils.ImageLoader
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_movie_list.*
import javax.inject.Inject

class TrendingMoviesFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var viewModel: TrendingMoviesViewModel

    private lateinit var adapter: TrendingMoviesAdapter

    private lateinit var disposable: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TrendingMoviesViewModel::class.java)
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
        recyclerview_movie_list.layoutManager = GridLayoutManager(activity, 2, RecyclerView.VERTICAL, false)
        adapter = TrendingMoviesAdapter(imageLoader) { movie, view ->
            // todo : implement! }
        }
        recyclerview_movie_list.adapter = adapter

        adapter.addItems(list)
    }
}