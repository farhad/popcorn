package io.github.farhad.popcorn.data.remote.response

import com.google.gson.annotations.SerializedName
import io.github.farhad.popcorn.data.entity.PerformerEntity
import io.github.farhad.popcorn.data.entity.RoleEntity

data class MovieCreditList(
    @SerializedName("cast") val performers: List<PerformerEntity>,
    @SerializedName("crew") val roles: List<RoleEntity>
)