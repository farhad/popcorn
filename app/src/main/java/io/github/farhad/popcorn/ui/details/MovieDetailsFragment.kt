package io.github.farhad.popcorn.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import io.github.farhad.popcorn.R
import io.github.farhad.popcorn.di.Injectable
import io.github.farhad.popcorn.utils.ImageLoader
import javax.inject.Inject

class MovieDetailsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var viewModel : MovieDetailsViewModel

    private lateinit var performerAdapter : MoviePerformersAdapter
    private lateinit var rolesAdapter : MovieRolesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie_details,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this,viewModelFactory).get(MovieDetailsViewModel::class.java)

        initView()

        if(savedInstanceState == null){
            viewModel.getMoviePerformers()
            viewModel.getMovieRoles()
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.viewState.observe(this, Observer {state ->
            handleViewState(state)
            if(state!=null) state.performers?.let {
                performerAdapter.addItems(it)
            }

            if(state != null) state.roles?.let {
                rolesAdapter.addItems(it)
            }


        })
    }

    private fun initView() {

    }

    private fun handleViewState(state: MovieDetailsState){

    }

}