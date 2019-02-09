package com.yossisegev.domain.common

import io.github.farhad.popcorn.domain.usecase.Transformer
import io.reactivex.Observable
import io.reactivex.ObservableSource

class RelayTransformer<T> : Transformer<T>() {
    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream
    }

}