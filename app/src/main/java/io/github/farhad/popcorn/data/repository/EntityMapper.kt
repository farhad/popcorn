package io.github.farhad.popcorn.data.repository

import io.github.farhad.popcorn.data.entity.MovieEntity
import io.github.farhad.popcorn.data.entity.PerformerEntity
import io.github.farhad.popcorn.data.entity.RoleEntity
import io.github.farhad.popcorn.domain.model.Movie
import io.github.farhad.popcorn.domain.model.Performer
import io.github.farhad.popcorn.domain.model.Role

object EntityMapper {

    fun toMovie(movieEntity: MovieEntity) = Movie(
        movieEntity.id,
        movieEntity.title,
        movieEntity.overview,
        movieEntity.releaseDate?.time,
        movieEntity.posterUrl,
        movieEntity.voteAverage,
        movieEntity.voteCount,
        movieEntity.isAdult
    )

    fun toRole(roleEntity: RoleEntity) = Role(
        roleEntity.id,
        roleEntity.movieId,
        roleEntity.job,
        roleEntity.name,
        roleEntity.imageUrl,
        roleEntity.department
    )

    fun toPerformer(performerEntity: PerformerEntity) = Performer(
        performerEntity.id,
        performerEntity.movieId,
        performerEntity.name,
        performerEntity.order,
        performerEntity.character,
        performerEntity.imageUrl
    )
}