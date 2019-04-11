package io.github.farhad.popcorn.ui.details

import androidx.lifecycle.MutableLiveData
import io.github.farhad.popcorn.domain.model.Movie
import io.github.farhad.popcorn.domain.model.Performer
import io.github.farhad.popcorn.domain.model.Role
import io.github.farhad.popcorn.domain.repository.Repository
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
    private val getMovieCast: GetMovieCast,
    private val repository: Repository
) : BaseViewModel() {

    var viewState: MutableLiveData<MovieDetailsState> = MutableLiveData()
    var movieState: MutableLiveData<Movie> = MutableLiveData()
    var performersState: MutableLiveData<List<Performer>> = MutableLiveData()
    var rolesState: MutableLiveData<List<Role>> = MutableLiveData()
    var loadingState: MutableLiveData<Boolean> = MutableLiveData()
    var errorState: MutableLiveData<Throwable> = MutableLiveData()

    fun setMovieId(movieId: Int) {
        addDisposable(
            repository.getMovieInfo(movieId).subscribe(
                {
                    if (it != null) {
                        loadingState.postValue(false)
                        movieState.postValue(it)

                        getMoviePerformers()
                        getMovieRoles()

                    } else {
                        this.errorState.postValue(IllegalArgumentException("movie not found"))
                    }
                },
                {
                    this.errorState.postValue(it)
                })
        )
    }

    fun getMoviePerformers() {
        if (movieState.value != null) {
            loadingState.postValue(true)

            addDisposable(
                getMovieCast.execute(GetMovieCast.Params(movieId = movieState.value!!.id))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (!it.isNullOrEmpty()) {
                            loadingState.value = false
                            performersState.value = it
                        }

                    }, {
                        loadingState.value = false
                        errorState.value = it
                    })
            )
        }
    }

    fun getMovieRoles() {
        if (movieState.value != null && loadingState.value != true) {
            loadingState.postValue(true)

            addDisposable(
                getMovieCrew.execute(GetMovieCrew.Params(movieId = movieState.value!!.id))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (!it.isNullOrEmpty()) {
                            loadingState.value = false
                            rolesState.value = it
                        }

                    }, {
                        loadingState.value = false
                        errorState.value = it
                    })
            )
        }
    }
}