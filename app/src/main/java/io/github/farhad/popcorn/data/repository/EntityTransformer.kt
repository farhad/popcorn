package io.github.farhad.popcorn.data.repository

import io.github.farhad.popcorn.data.entity.MovieEntity
import io.github.farhad.popcorn.data.entity.PerformerEntity
import io.github.farhad.popcorn.data.entity.RoleEntity
import io.github.farhad.popcorn.domain.model.Movie
import io.github.farhad.popcorn.domain.model.Performer
import io.github.farhad.popcorn.domain.model.Role
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

sealed class EntityTransformer {

    inner class MovieTransformer : ObservableTransformer<List<MovieEntity>?, List<Movie>?> {

        override fun apply(upstream: Observable<List<MovieEntity>?>): ObservableSource<List<Movie>?> {
            return upstream.flatMapIterable { it }.map { EntityMapper.toMovie(it) }.buffer(20)
        }
    }

    inner class RoleTransformer : ObservableTransformer<List<RoleEntity>?, List<Role>?> {

        override fun apply(upstream: Observable<List<RoleEntity>?>): ObservableSource<List<Role>?> {
            return upstream.flatMapIterable { it }.map { EntityMapper.toRole(it) }.buffer(20)
        }
    }

    inner class PerformerTransformer : ObservableTransformer<List<PerformerEntity>?, List<Performer>?> {

        override fun apply(upstream: Observable<List<PerformerEntity>?>): ObservableSource<List<Performer>?> {
            return upstream.flatMapIterable { it }.map { EntityMapper.toPerformer(it) }.buffer(20)
        }
    }
}