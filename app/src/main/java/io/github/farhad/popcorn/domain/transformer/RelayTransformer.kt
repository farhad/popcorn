package io.github.farhad.popcorn.domain.transformer

import io.reactivex.Observable
import io.reactivex.ObservableSource

class RelayTransformer<T> : Transformer<T>() {
    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream
    }

}