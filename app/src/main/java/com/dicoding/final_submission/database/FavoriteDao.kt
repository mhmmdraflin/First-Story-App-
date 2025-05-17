package com.dicoding.final_submission.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: Favorite)

    @Delete
    fun delete(favorite: Favorite)

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): LiveData<List<Favorite>>

    @Query("SELECT * FROM favorites WHERE id = :id")
    fun getFavoriteEventById(id: String): LiveData<Favorite>
}