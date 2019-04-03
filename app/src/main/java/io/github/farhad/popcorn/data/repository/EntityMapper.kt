package io.github.farhad.popcorn.data.repository

import io.github.farhad.popcorn.data.entity.MovieEntity
import io.github.farhad.popcorn.data.entity.PerformerEntity
import io.github.farhad.popcorn.data.entity.RoleEntity
import io.github.farhad.popcorn.di.NamedString
import io.github.farhad.popcorn.di.StringType
import io.github.farhad.popcorn.domain.model.Movie
import io.github.farhad.popcorn.domain.model.Performer
import io.github.farhad.popcorn.domain.model.Role
import javax.inject.Inject

/**
 * todo : add backdrop image!
 */
class EntityMapper @Inject constructor(
    @NamedString(StringType.POSTER_BASE_URL) val posterBaseUrl: String,
    @NamedString(StringType.BACKDROP_BASE_URL) val backdropBaseUrl: String,
    @NamedString(StringType.PERFORMER_ROLE_BASE_URL) val performerRoleBaseUrl: String
) {

    fun toMovie(movieEntity: MovieEntity) = Movie(
        movieEntity.id,
        movieEntity.title,
        movieEntity.overview,
        movieEntity.releaseDate?.time,
        posterBaseUrl + movieEntity.posterUrl,
        backdropBaseUrl + movieEntity.backdropUrl,
        movieEntity.voteAverage,
        movieEntity.voteCount,
        movieEntity.isAdult
    )

    fun toRole(roleEntity: RoleEntity) = Role(
        roleEntity.id,
        roleEntity.movieId,
        roleEntity.job,
        roleEntity.name,
        performerRoleBaseUrl + roleEntity.imageUrl,
        roleEntity.department
    )

    fun toPerformer(performerEntity: PerformerEntity) = Performer(
        performerEntity.id,
        performerEntity.movieId,
        performerEntity.name,
        performerEntity.order,
        performerEntity.character,
        performerRoleBaseUrl + performerEntity.imageUrl
    )
}