package com.example.flightsearchapp.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearchapp.FlightSearchApplication
import com.example.flightsearchapp.data.Airport
import com.example.flightsearchapp.data.Favorite
import com.example.flightsearchapp.data.FavoriteRepository
import com.example.flightsearchapp.data.Flight
import com.example.flightsearchapp.data.FlightRepository
import com.example.flightsearchapp.data.toFavorite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val TAG = "HomeScreenViewModel"

class HomeScreenViewModel(
    private val flightRepository: FlightRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    // contains what the user searched for in the text field
    var searchQueryState by mutableStateOf("")
        private set

    var showAllFlights by mutableStateOf(false)
        private set

    var showSuggestions by mutableStateOf(false)
        private set

    // when the user types anything in the search field
    private val _airportList = MutableStateFlow<List<Airport>>(emptyList())
    val airportList = _airportList.asStateFlow()

    // when the user selects a suggestion
    private val _flightList = MutableStateFlow<List<Flight>>(emptyList())
    val flightList = _flightList.asStateFlow()

    // when the search field is empty
    private val _favoriteList = MutableStateFlow<List<Favorite>>(emptyList())
    val favoriteList = _favoriteList.asStateFlow()

    init {
        getFavorites()
    }

    fun updateUiState(searchQuery: String) {
        searchQueryState = searchQuery

        // the search query has changed
        if(searchQuery.isEmpty()) {
            getFavorites()
            showAllFlights = false    // show the list of favorite flights again
            showSuggestions = false
        } else {
            showSuggestions = true
        }
    }

    // get the list of airports that match with the search query entered by the user
    fun getResult() {
        Log.d(TAG, "homeUiState: $searchQueryState")

        viewModelScope.launch {
            flightRepository.getMatchingAirports(searchQueryState)
                .collect { result ->
                    _airportList.update { result }
                }
        }
    }

    // when the user selects a suggestion
    fun getFlightsList(airport: Airport) {
        showAllFlights = true

        // since the user has selected a suggestion, we don't want to show
        // the suggestions anymore
        showSuggestions = false

        viewModelScope.launch {
            flightRepository.getFlights(airport.iataCode)
                .collect { result ->
                    val newList = mutableListOf<Flight>()
                    result.forEach { destinationAirport ->
                        val newItem = Flight(
                            departureAirport = airport,
                            destinationAirport = destinationAirport
                        )
                        newList.add(newItem)
                    }
                    _flightList.update {
                        newList
                    }
                }
        }
    }

    // when the user adds a flight to favorite list
    fun addToFavorite(flight: Flight) {
        viewModelScope.launch {
            favoriteRepository.addToFavorite(flight.toFavorite())
        }
    }

    // favorites list to show when the user has not selected any suggestion
    private fun getFavorites() {
        viewModelScope.launch {
            favoriteRepository.getFavoriteFlights()
                .collect { result ->
                    _favoriteList.update { result }
                }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                HomeScreenViewModel(
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightSearchApplication)
                        .container
                        .flightRepository,
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightSearchApplication)
                        .container
                        .favoriteRepository
                )
            }
        }
    }

}