package io.github.farhad.popcorn.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.farhad.popcorn.domain.usecase.GetMovieCast
import io.github.farhad.popcorn.domain.usecase.GetMovieCrew
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(private val getMovieCrew: GetMovieCrew,
                                                private val getMovieCast: GetMovieCast) : ViewModel() {

    var viewState : MutableLiveData<MovieDetailsState> = MutableLiveData()
    var errorState : MutableLiveData<Throwable> = MutableLiveData()


}