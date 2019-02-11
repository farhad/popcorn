package io.github.farhad.popcorn.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.farhad.popcorn.data.db.converter.Converters
import io.github.farhad.popcorn.data.db.dao.MovieEntityDao
import io.github.farhad.popcorn.data.db.dao.PerformerEntityDao
import io.github.farhad.popcorn.data.db.dao.RoleEntityDao
import io.github.farhad.popcorn.data.entity.MovieEntity
import io.github.farhad.popcorn.data.entity.PerformerEntity
import io.github.farhad.popcorn.data.entity.RoleEntity

@TypeConverters(Converters::class)
@Database(entities = [MovieEntity::class, PerformerEntity::class, RoleEntity::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun getMovieEntityDao(): MovieEntityDao
    abstract fun getPerformerEntityDao(): PerformerEntityDao
    abstract fun getRoleEntityDao(): RoleEntityDao

    companion object Factory {
        fun create(context: Context): MovieDatabase {
            return Room.databaseBuilder(context, MovieDatabase::class.java, "app_database.db").build()
        }
    }
}