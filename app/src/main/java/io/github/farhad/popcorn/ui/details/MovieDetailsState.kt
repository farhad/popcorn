package io.github.farhad.popcorn.ui.details

import io.github.farhad.popcorn.domain.model.Performer
import io.github.farhad.popcorn.domain.model.Role

data class MovieDetailsState(
    var showLoading: Boolean = false,
    var performers: List<Performer>? = null,
    var roles: List<Role>? = null,
    var movieId: Int
)