package io.github.farhad.popcorn.data.repository

import io.github.farhad.popcorn.domain.transformer.Transformer
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ExecutionThreadTransformer<T> : Transformer<T>() {

    override fun apply(upstream: Observable<T>): ObservableSource<T> =
        upstream.observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
}