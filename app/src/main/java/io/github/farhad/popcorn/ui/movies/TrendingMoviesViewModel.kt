package io.github.farhad.popcorn.ui.movies

import androidx.lifecycle.MutableLiveData
import io.github.farhad.popcorn.domain.usecase.GetTrendingMovies
import io.github.farhad.popcorn.ui.common.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TrendingMoviesViewModel @Inject constructor(val getTrendingMovies: GetTrendingMovies) : BaseViewModel() {

    var viewState: MutableLiveData<TrendingMoviesState> = MutableLiveData()
    var errorState: MutableLiveData<Throwable> = MutableLiveData()

    init {
        viewState.value = TrendingMoviesState()
    }

    fun getTrendingMovies() {
        addDisposable(getTrendingMovies.execute(GetTrendingMovies.Params(pageSize = 20, paginationId = 1))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { movies ->
                    viewState.value?.let {
                        val newState = this.viewState.value?.copy(showLoading = false, movies = movies)
                        this.viewState.value = newState
                        this.errorState.value = null
                    }
                }, {
                    val newState = this.viewState.value?.copy(showLoading = false)
                    this.viewState.value = newState
                    this.errorState.value = it
                })
        )
    }
}