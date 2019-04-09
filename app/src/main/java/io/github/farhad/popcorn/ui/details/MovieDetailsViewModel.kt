package io.github.farhad.popcorn.ui.details

import androidx.lifecycle.MutableLiveData
import io.github.farhad.popcorn.domain.model.Movie
import io.github.farhad.popcorn.domain.usecase.GetMovieCast
import io.github.farhad.popcorn.domain.usecase.GetMovieCrew
import io.github.farhad.popcorn.ui.common.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject


/**
 * Todo : cast and crew come from the same endpoint, so it's better to merge them in one usecase and stream
 *
 */
class MovieDetailsViewModel @Inject constructor(
    private val getMovieCrew: GetMovieCrew,
    private val getMovieCast: GetMovieCast
) : BaseViewModel() {

    var viewState: MutableLiveData<MovieDetailsState> = MutableLiveData()
    var errorState: MutableLiveData<Throwable> = MutableLiveData()

    /**
     * this is not the right approach,
     * the movieId should be provided with a custom ViewModelFactory!!
     */
    fun setMovie(movie: Movie) {
        val newState = viewState.value?.copy(movie = movie)
        viewState.value = newState
    }

    fun getMoviePerformers() {

        if (this.viewState.value?.showLoading == false) {

            val state = this.viewState.value?.copy(showLoading = true)
            this.viewState.value = state

            addDisposable(
                getMovieCast.execute(GetMovieCast.Params(movieId = viewState.value!!.movie.id)).observeOn(
                    AndroidSchedulers.mainThread()
                ).subscribe({

                    val newState = this.viewState.value?.copy(showLoading = false, performers = it)
                    this.viewState.value = newState
                    this.errorState.value = null

                }, {
                    val newState = this.viewState.value?.copy(showLoading = false)
                    this.viewState.value = newState
                    this.errorState.value = it
                })
            )
        }
    }

    fun getMovieRoles() {

        if (this.viewState.value?.showLoading == false) {

            val state = this.viewState.value?.copy(showLoading = true)
            this.viewState.value = state

            addDisposable(
                getMovieCrew.execute(GetMovieCrew.Params(movieId = viewState.value!!.movie.id)).observeOn(
                    AndroidSchedulers.mainThread()
                ).subscribe({
                    val newState = this.viewState.value?.copy(showLoading = false, roles = it)
                    this.viewState.value = newState
                    this.errorState.value = null
                }, {
                    val newState = this.viewState.value?.copy(showLoading = false)
                    this.viewState.value = newState
                    this.errorState.value = it
                })
            )
        }
    }
}