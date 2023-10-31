package com.example.flightsearchapp.data

import kotlinx.coroutines.flow.Flow

interface FlightRepository {

    fun getAirportsByCode(iataCode: String): Flow<List<Airport>>

    fun getAirportsByName(name: String): Flow<List<Airport>>

    fun getAllAirports(): Flow<List<Airport>>

    fun getMatchingAirports(searchQuery: String): Flow<List<Airport>>

    fun getFlights(iataCode: String): Flow<List<Airport>>
}