package io.github.farhad.popcorn.ui.details

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
import io.github.farhad.popcorn.R
import io.github.farhad.popcorn.di.Injectable
import io.github.farhad.popcorn.domain.model.Movie
import io.github.farhad.popcorn.utils.ImageLoader
import io.github.farhad.popcorn.utils.formatDate
import io.github.farhad.popcorn.utils.formatDigits
import io.github.farhad.popcorn.utils.show
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.layout_loading.*
import kotlinx.android.synthetic.main.layout_movie_overview.*
import kotlinx.android.synthetic.main.layout_movie_performers.*
import kotlinx.android.synthetic.main.layout_movie_roles.*
import kotlinx.android.synthetic.main.layout_movie_title_and_votes.*
import javax.inject.Inject

class MovieDetailsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var viewModel: MovieDetailsViewModel

    private lateinit var adapterPerformers: MoviePerformersAdapter
    private lateinit var adapterRoles: MovieRolesAdapter

    private var movieId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            movieId = it.getInt("movieId")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(MovieDetailsViewModel::class.java)

        initView()

        viewModel.setMovieId(checkNotNull(movieId))
    }

    override fun onResume() {
        super.onResume()

        viewModel.viewState.observe(this, Observer { state ->
            if (state != null) {
                handleViewState(state)

                state.movie?.let {
                    showMovie(it)
                }

                state.performers?.let {
                    adapterPerformers.addItems(it)

                }

                state.roles?.let {
                    adapterRoles.addItems(it)
                }
            }
        })

        viewModel.errorState.observe(this, Observer {
            if (it != null)
                show(it.localizedMessage, constraint_details)
        })
    }

    private fun initView() {

        group_movie_details.visibility = View.GONE
        imageview_movie_backdrop.visibility = View.GONE
        collapsing_toolbar_movie_details.visibility = View.GONE
        button_try_again.visibility = View.GONE

        recyclerview_performers.isNestedScrollingEnabled = false
        recyclerview_performers.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        adapterPerformers = MoviePerformersAdapter(imageLoader, resources)

        recyclerview_performers.adapter = adapterPerformers

        recyclerview_roles.isNestedScrollingEnabled = false
        recyclerview_roles.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        adapterRoles = MovieRolesAdapter(imageLoader, resources)

        recyclerview_roles.adapter = adapterRoles
    }

    private fun showMovie(movie: Movie) {
        collapsing_toolbar_movie_details.visibility = View.VISIBLE
        movie.backdropUrl?.let { imageLoader.load(it, imageview_movie_backdrop) }
        textview_movie_title.text = movie.title
        movie.voteCount?.let { formatDigits(it) }
        textview_movie_voteaverage.text = movie.voteAverage.toString()
        movie.releaseDate?.let { textview_movie_releasedate.text = formatDate(it) }

        textview_movie_overview.text = movie.overview
    }

    private fun handleViewState(state: MovieDetailsState) {
        if (!state.showLoading) {
            progressbar.visibility = View.GONE
            button_try_again.visibility = View.GONE
            group_movie_details.visibility = View.VISIBLE

            if (state.performers.isNullOrEmpty() || state.roles.isNullOrEmpty()) {
//                button_try_again.visibility = View.VISIBLE
            }
        }
    }

}