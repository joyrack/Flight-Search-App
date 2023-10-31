package com.example.flightsearchapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {

    @Query("SELECT * FROM airport WHERE iata_code LIKE '%' || :iataCode || '%'")
    fun getAirportsByCode(iataCode: String): Flow<List<Airport>>

    @Query("SELECT * FROM airport WHERE name LIKE '%' || :name || '%'")
    fun getAirportsByName(name: String): Flow<List<Airport>>

    @Query("SELECT * FROM airport")
    fun getAllAirports(): Flow<List<Airport>>

    @Query("SELECT * FROM airport WHERE " +
            "(iata_code LIKE '%' || :searchQuery || '%') OR " +
            "(name LIKE '%' || :searchQuery || '%')")
    fun getMatchingAirports(searchQuery: String): Flow<List<Airport>>

    @Query("SELECT * FROM airport WHERE iata_code != :iataCode")
    fun getFlights(iataCode: String): Flow<List<Airport>>

}