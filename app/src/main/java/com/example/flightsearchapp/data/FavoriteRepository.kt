package com.example.flightsearchapp.data

import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun addToFavorite(favorite: Favorite)

    fun getFavoriteFlights(): Flow<List<Favorite>>
}