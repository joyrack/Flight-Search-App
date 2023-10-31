package com.example.flightsearchapp.data

import android.util.Log
import kotlinx.coroutines.flow.Flow

const val TAG = "OfflineFlightRepository"

class OfflineFlightRepository(private val airportDao: AirportDao) : FlightRepository {
    override fun getAirportsByCode(iataCode: String): Flow<List<Airport>>
    = airportDao.getAirportsByCode(iataCode)

    override fun getAirportsByName(name: String): Flow<List<Airport>>
    = airportDao.getAirportsByName(name)


    override fun getAllAirports(): Flow<List<Airport>> {
        Log.d(TAG, "repository getAllAirports() called")
        return airportDao.getAllAirports()
    }

    override fun getMatchingAirports(searchQuery: String): Flow<List<Airport>>
    = airportDao.getMatchingAirports(searchQuery)

    override fun getFlights(iataCode: String): Flow<List<Airport>>
    = airportDao.getFlights(iataCode)
}