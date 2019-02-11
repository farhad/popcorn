package io.github.farhad.popcorn.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.farhad.popcorn.data.entity.RoleEntity

@Dao
interface RoleEntityDao : BaseDao<RoleEntity> {

    @Query("SELECT * FROM Roles WHERE movieId = :movieId")
    fun getMovieRolesList(movieId: Int): List<RoleEntity>

    @Query("SELECT COUNT(*) FROM Roles")
    fun count(): Int
}