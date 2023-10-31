package com.example.flightsearchapp.data

import kotlinx.coroutines.flow.Flow

class OfflineFavoriteRepository(private val favoriteDao: FavoriteDao) : FavoriteRepository {
    override suspend fun addToFavorite(favorite: Favorite)
    = favoriteDao.addToFavorite(favorite)

    override fun getFavoriteFlights(): Flow<List<Favorite>>
    = favoriteDao.getFavoriteFlights()
}