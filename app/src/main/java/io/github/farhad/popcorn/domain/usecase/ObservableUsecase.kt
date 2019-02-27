package io.github.farhad.popcorn.domain.usecase

import io.github.farhad.popcorn.domain.transformer.Transformer
import io.reactivex.Observable

/**
 * todo : add documentation about what is an ObservableUsecase, also add a CompletableUsecase
 */
abstract class ObservableUsecase<T, in Params>(private val transformer: Transformer<T>) {

    abstract fun buildUseCase(params: Params? = null): Observable<T>

    fun execute(params: Params? = null): Observable<T> = buildUseCase(params).compose(transformer)
}