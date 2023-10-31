package com.example.flightsearchapp.data

data class Flight(
    val id: Int = 0,
    val departureAirport: Airport,
    val destinationAirport: Airport
)

// extension function to convert Flight item to Favorite item
fun Flight.toFavorite(): Favorite = Favorite(
    id = id,
    departureCode = departureAirport.iataCode,
    destinationCode = destinationAirport.iataCode
)
