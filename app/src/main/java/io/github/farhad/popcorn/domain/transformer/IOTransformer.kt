package io.github.farhad.popcorn.domain.transformer

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

class IOTransformer<T> : Transformer<T>() {
    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream.observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
    }
}