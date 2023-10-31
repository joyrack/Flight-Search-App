package com.example.flightsearchapp.data

import android.content.Context

interface AppContainer {
    val flightRepository: FlightRepository
    val favoriteRepository: FavoriteRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val flightRepository: FlightRepository by lazy {
        OfflineFlightRepository(FlightDatabase.getDatabase(context).airportDao())
    }

    override val favoriteRepository: FavoriteRepository by lazy {
        OfflineFavoriteRepository(FlightDatabase.getDatabase(context).favoriteDao())
    }
}