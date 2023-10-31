package com.example.flightsearchapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearchapp.data.Airport
import com.example.flightsearchapp.data.Favorite
import com.example.flightsearchapp.data.Flight

@Composable
fun FlightSearchApp(
    viewModel: HomeScreenViewModel = viewModel(factory = HomeScreenViewModel.Factory)
) {
    val airportList = viewModel.airportList.collectAsState()
    val flightList = viewModel.flightList.collectAsState()
    val favoriteList = viewModel.favoriteList.collectAsState()

    Scaffold { innerPadding ->
        HomeScreen(
            airportList = airportList.value,
            flightList = flightList.value,
            favoriteList = favoriteList.value,
            onSearchQueryChange = {
                viewModel.updateUiState(it)
                viewModel.getResult()
            },
            onSuggestionSelected = { airport ->  viewModel.getFlightsList(airport) },
            onFlightSelected = { flight ->  viewModel.addToFavorite(flight) },
            searchQuery = viewModel.searchQueryState,
            showSuggestions = viewModel.showSuggestions,
            showAllFlights = viewModel.showAllFlights,
            contentPadding = innerPadding
        )

    }
}

@Composable
fun HomeScreen(
    airportList: List<Airport>,
    modifier: Modifier = Modifier,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSuggestionSelected: (Airport) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    showSuggestions: Boolean,
    flightList: List<Flight>,
    favoriteList: List<Favorite>,
    onFlightSelected: (Flight) -> Unit,
    showAllFlights: Boolean
) {
    Column {
        TextField(
            value = searchQuery,
            onValueChange = { onSearchQueryChange(it) }
        )

        if(showSuggestions) {
            SuggestionsList(
                airportList = airportList,
                onSuggestionSelected = onSuggestionSelected
            )
        }

        if(showAllFlights) {
            // show all flights
            FlightsList(
                flightList = flightList,
                onFlightSelected = onFlightSelected
            )
        } else {
            // show favorite flights
            FavoritesList(
                favoriteList = favoriteList
            )
        }
    }
}

@Composable
fun FlightsList(
    flightList: List<Flight>,
    onFlightSelected: (Flight) -> Unit,
    modifier: Modifier = Modifier
) {
    var departureCode: String = ""

    if(flightList.isNotEmpty()) {
        departureCode = flightList[0].departureAirport.iataCode
    }

    Column {
        Text("Flights from $departureCode")
        LazyColumn(
            modifier = modifier.padding(top = 12.dp)
        ) {
            items(flightList) {flight ->
                FlightCard(
                    flight = flight,
                    modifier = Modifier.clickable { onFlightSelected(flight) }
                )
            }
        }
    }
}

@Composable
fun FlightCard(
    flight: Flight,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text("Depart")
        Text("${flight.departureAirport.iataCode}  ${flight.departureAirport.name}")
        Text("Arrive")
        Text("${flight.destinationAirport.iataCode}  ${flight.destinationAirport.name}")
        Divider()
    }
}

@Composable
fun FavoritesList(
    favoriteList: List<Favorite>,
    modifier: Modifier = Modifier
) {
    Column {
        Text("Favorite routes")
        LazyColumn(
            modifier = modifier.padding(top = 12.dp)
        ) {
            items(favoriteList) {favorite ->
                FavoriteCard(
                    favorite = favorite,
                    //modifier = Modifier.clickable {  }
                )
            }
        }
    }
}

@Composable
fun FavoriteCard(
    favorite: Favorite,
    modifier: Modifier = Modifier
) {
    Column {
        Text("Depart")
        Text(favorite.departureCode)
        Text("Arrive")
        Text(favorite.destinationCode)
        Divider()
    }
}

@Composable
fun SuggestionsList(
    airportList: List<Airport>,
    onSuggestionSelected: (Airport) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(airportList) {airport ->
            SuggestionCard(
                airport = airport,
                modifier = Modifier.clickable { onSuggestionSelected(airport) }
            )
        }
    }
}

@Composable
fun SuggestionCard(
    airport: Airport,
    modifier: Modifier = Modifier
) {
    Column {
        Row(modifier = modifier) {
            Text(airport.iataCode)
            Spacer(modifier = Modifier.width(4.dp))
            Text(airport.name)
        }
        Divider()
    }
}

