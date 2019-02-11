package io.github.farhad.popcorn.data.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {
    /**
     * Insert an object in the database.
     *
     * @param row the object to be inserted.
     */
    @Insert
    fun insert(row: T)

    /**
     * Insert an array of objects in the database.
     *
     * @param row the objects to be inserted.
     */
    @Insert
    fun insert(rows: List<T>)

    /**
     * Insert or update the object in the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(row: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(rows: List<T>)

    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    @Update
    fun update(row: T)

    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete
    fun delete(row: T)

    @Delete
    fun delete(rows: List<T>)
}