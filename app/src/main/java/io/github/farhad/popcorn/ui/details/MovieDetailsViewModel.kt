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
    var movieId: Int = 0

    fun onNewMovieId(movieId: Int) {
        this.movieId = movieId
        addDisposable(
            repository.getMovieInfo(movieId).subscribe(
                {
                    if (it != null) {
                        loadingState.postValue(false)
                        movieState.postValue(it)

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
        loadingState.postValue(true)

        addDisposable(
            getMovieCast.execute(GetMovieCast.Params(movieId = movieId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (!it.isNullOrEmpty()) {
                        loadingState.value = false
                        performersState.postValue(it)
                    }

                }, {
                    loadingState.value = false
                    errorState.value = it
                })
        )
    }

    fun getMovieRoles() {
        loadingState.postValue(true)

        addDisposable(
            getMovieCrew.execute(GetMovieCrew.Params(movieId = movieId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (!it.isNullOrEmpty()) {
                        loadingState.value = false
                        rolesState.value = it
                    } else {
                        this.errorState.postValue(IllegalArgumentException("movie not found"))
                    }

                }, {
                    loadingState.value = false
                    errorState.value = it
                })
        )
    }
}