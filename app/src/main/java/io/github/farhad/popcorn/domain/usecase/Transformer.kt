package io.github.farhad.popcorn.domain.usecase

import io.reactivex.ObservableTransformer

/**
 * todo : add documentation about why we need this transformer and how it's beneficial to our design
 */
abstract class Transformer<T> : ObservableTransformer<T, T>