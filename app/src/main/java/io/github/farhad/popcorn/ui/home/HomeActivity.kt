package io.github.farhad.popcorn.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.github.farhad.popcorn.R
import io.github.farhad.popcorn.ui.movies.TrendingMoviesViewModel
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var movieViewModel: TrendingMoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    override fun onResume() {
        super.onResume()

        movieViewModel = ViewModelProviders.of(this, viewModelFactory).get(TrendingMoviesViewModel::class.java)

        movieViewModel.viewState.observe(this, Observer {

            if (it.selectedMovie != null) {
                // navigate to details fragment
            }
        })
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector
}
