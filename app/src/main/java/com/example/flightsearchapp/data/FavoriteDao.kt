package com.example.flightsearchapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToFavorite(favorite: Favorite)

    @Query("SELECT * FROM favorite")
    fun getFavoriteFlights(): Flow<List<Favorite>>
}