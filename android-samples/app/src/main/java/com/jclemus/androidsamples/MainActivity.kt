package com.jclemus.androidsamples

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jclemus.androidsamples.ui.theme.UiappTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


// DATA

data class WeatherResponse(
    val id: Long,
    val name: String,
    val main: Main
)

data class Main(
    val temp: Float,
    val humidity: Int
)


// UI

data class UiState(
    val isLoading: Boolean = false,
    val weather: WeatherResponse? = null,
    val error: String? = null,
    val searchText: String = ""
)

// API

const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

interface WeatherApiService {

    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") key: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}


class WeatherRepository(
    private val apiService: WeatherApiService
) {

    suspend fun getWeather(city: String): Result<WeatherResponse> {
        return try {
            val response = apiService.getWeather(city, "cffbee21813403a32b91d60ab4e083ee")

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun loadWeather() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, error = null)
            }

            repository.getWeather(uiState.value.searchText).fold(
                onSuccess = { weather ->
                    _uiState.update {
                        it.copy(isLoading = false, weather = weather, error = null, searchText = "")
                    }
                },
                onFailure = {
                    _uiState.update {
                        it.copy(error = "City not found", isLoading = false)
                    }
                }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun updateQuery(query: String) {
        _uiState.update {
            it.copy(searchText = query.trim())
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(WeatherApiService::class.java)
        val repository = WeatherRepository(apiService)
        setContent {
            UiappTheme {
                val viewModel: WeatherViewModel = viewModel {
                    WeatherViewModel(repository)
                }
                CitiesListScreen(
                    viewModel,
                    onCitySelected = { city ->
                        println(city)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesListScreen(
    viewModel: WeatherViewModel,
    onCitySelected: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Weather")
            })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    value = uiState.searchText,
                    onValueChange = {
                        viewModel.updateQuery(it)
                    },
                    label = { Text("Enter a city") },
                    singleLine = true,
                    enabled = uiState.isLoading.not()
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        viewModel.loadWeather()
                    },
                    enabled = uiState.isLoading.not() && uiState.searchText.isNotEmpty()
                ) {
                    Text("Search")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val error = uiState.error
            if (error != null) {
                Text(error, modifier = Modifier.fillMaxWidth())
            }

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.weather != null -> {
                    val weather = uiState.weather
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(weather?.name.orEmpty())

                        Spacer(Modifier.height(16.dp))
                        Text("${weather?.main?.temp} ªC")
                        Spacer(Modifier.height(16.dp))

                        Text("Humidity ${weather?.main?.humidity}")
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Search for a city to see the weather")
                    }
                }
            }


        }
    }
}