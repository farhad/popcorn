package io.github.farhad.popcorn.fixture

import io.github.farhad.popcorn.domain.transformer.Transformer
import io.reactivex.Observable
import io.reactivex.ObservableSource

class RelayTransformer<T> : Transformer<T>() {
    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream
    }

}