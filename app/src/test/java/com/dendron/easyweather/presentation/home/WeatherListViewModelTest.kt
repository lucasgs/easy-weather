package com.dendron.easyweather.presentation.home

import app.cash.turbine.test
import com.dendron.easyweather.MainDispatcherRule
import com.dendron.easyweather.R
import com.dendron.easyweather.domain.DailyForecast
import com.dendron.easyweather.domain.HourlyForecast
import com.dendron.easyweather.domain.Weather
import com.dendron.easyweather.domain.WeatherFailure
import com.dendron.easyweather.domain.WeatherRepository
import com.dendron.easyweather.domain.WeatherResult
import com.dendron.easyweather.domain.WeatherUnits
import com.dendron.easyweather.domain.location.CurrentLocationFailure
import com.dendron.easyweather.domain.location.CurrentLocationResult
import com.dendron.easyweather.domain.location.LocationData
import com.dendron.easyweather.domain.location.LocationProvider
import com.dendron.easyweather.domain.location.LocationSearchRepository
import com.dendron.easyweather.domain.usecase.GetCurrentLocationUseCase
import com.dendron.easyweather.domain.usecase.GetCurrentWeatherUseCase
import com.dendron.easyweather.domain.usecase.LoadCurrentLocationWeatherUseCase
import com.dendron.easyweather.domain.usecase.LoadWeatherForCoordinatesUseCase
import com.dendron.easyweather.domain.usecase.RefreshWeatherUseCase
import com.dendron.easyweather.domain.usecase.SearchLocationsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class WeatherListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var locationProvider: LocationProvider

    @Mock
    private lateinit var weatherRepository: WeatherRepository

    @Mock
    private lateinit var locationSearchRepository: LocationSearchRepository

    private lateinit var viewModel: WeatherListViewModel
    private lateinit var weatherUiModelMapper: WeatherUiModelMapper

    @Before
    fun setUp() {
        weatherUiModelMapper = WeatherUiModelMapper(
            weatherDescriptionMapper = WeatherDescriptionMapper(),
            weatherIconMapper = WeatherIconMapper(),
            windDirectionMapper = WindDirectionMapper(),
        )
        val loadWeatherForCoordinatesUseCase = LoadWeatherForCoordinatesUseCase(
            getCurrentWeatherUseCase = GetCurrentWeatherUseCase(weatherRepository),
            refreshWeatherUseCase = RefreshWeatherUseCase(weatherRepository),
        )
        viewModel = WeatherListViewModel(
            loadCurrentLocationWeatherUseCase = LoadCurrentLocationWeatherUseCase(
                getCurrentLocationUseCase = GetCurrentLocationUseCase(locationProvider),
                loadWeatherForCoordinatesUseCase = loadWeatherForCoordinatesUseCase,
            ),
            loadWeatherForCoordinatesUseCase = loadWeatherForCoordinatesUseCase,
            searchLocationsUseCase = SearchLocationsUseCase(locationSearchRepository),
            weatherUiModelMapper = weatherUiModelMapper,
        )
    }

    @Test
    fun `fetchData should emit loading then content when location and weather are available`() = runTest {
        whenever(locationProvider.getCurrentLocation()).thenReturn(
            CurrentLocationResult.Success(LocationData(LAT, LONG)),
        )

        whenever(weatherRepository.getCurrentWeather(LAT, LONG)).thenReturn(
            flow {
                emit(WeatherResult.Loading)
                emit(WeatherResult.Success(fakeWeather))
            },
        )

        viewModel.state.test {
            assertEquals(WeatherScreenState.Empty, awaitItem())

            viewModel.fetchData()

            assertEquals(WeatherScreenState.Loading(R.string.weather_loading_message), awaitItem())
            val content = awaitItem() as WeatherScreenState.Content
            assertEquals(fakeWeatherUiModel, content.model)
        }
    }

    @Test
    fun `fetchData should emit loading then error when repository fails`() = runTest {
        whenever(locationProvider.getCurrentLocation()).thenReturn(
            CurrentLocationResult.Success(LocationData(LAT, LONG)),
        )

        whenever(weatherRepository.getCurrentWeather(LAT, LONG)).thenReturn(
            flow {
                emit(WeatherResult.Loading)
                emit(WeatherResult.Failure(WeatherFailure.Network))
            },
        )

        viewModel.state.test {
            assertEquals(WeatherScreenState.Empty, awaitItem())

            viewModel.fetchData()

            assertEquals(WeatherScreenState.Loading(R.string.weather_loading_message), awaitItem())
            assertEquals(
                WeatherScreenState.Error(ErrorReason.Network),
                awaitItem(),
            )
        }
    }

    @Test
    fun `fetchData should emit permission required when location permission is missing`() = runTest {
        whenever(locationProvider.getCurrentLocation()).thenReturn(
            CurrentLocationResult.Failure(CurrentLocationFailure.PermissionDenied),
        )

        viewModel.state.test {
            assertEquals(WeatherScreenState.Empty, awaitItem())

            viewModel.fetchData()

            assertEquals(WeatherScreenState.Loading(R.string.weather_loading_message), awaitItem())
            assertEquals(WeatherScreenState.PermissionRequired, awaitItem())
        }
    }

    @Test
    fun `fetchData should emit location disabled when providers are off`() = runTest {
        whenever(locationProvider.getCurrentLocation()).thenReturn(
            CurrentLocationResult.Failure(CurrentLocationFailure.LocationDisabled),
        )

        viewModel.state.test {
            assertEquals(WeatherScreenState.Empty, awaitItem())

            viewModel.fetchData()

            assertEquals(WeatherScreenState.Loading(R.string.weather_loading_message), awaitItem())
            assertEquals(WeatherScreenState.LocationDisabled, awaitItem())
        }
    }

    @Test
    fun `fetchData should emit location unavailable error when no location is returned`() = runTest {
        whenever(locationProvider.getCurrentLocation()).thenReturn(
            CurrentLocationResult.Failure(CurrentLocationFailure.Unavailable),
        )

        viewModel.state.test {
            assertEquals(WeatherScreenState.Empty, awaitItem())

            viewModel.fetchData()

            assertEquals(WeatherScreenState.Loading(R.string.weather_loading_message), awaitItem())
            assertEquals(
                WeatherScreenState.Error(ErrorReason.LocationUnavailable),
                awaitItem(),
            )
        }
    }

    companion object {
        const val LAT = 1.0
        const val LONG = 1.0

        val fakeWeatherUiModel = WeatherUiModel(
            locationName = "New York",
            descriptionText = "Mainly clear, partly cloudy, and overcast",
            currentTemperatureText = "10.0°",
            highLowText = "H:1.0°  L:2.0°",
            windText = "50.0 km/h SE",
            temperatureText = "2.0° / 10.0° / 1.0°",
            humidityText = "65%",
            precipitationText = "40%",
            sunriseText = "6:30 am",
            sunsetText = "8:15 pm",
            hourlyForecast = listOf(
                HourlyForecastUiModel(timeText = "1pm", temperatureText = "11.0°"),
            ),
            dailyForecast = listOf(
                DailyForecastUiModel(dayLabel = "Sun", rangeText = "2.0° / 8.0°"),
            ),
            weatherIcon = R.drawable.cloudy,
            palette = weatherPaletteForCode(1),
        )

        val fakeWeather = Weather(
            locationName = "New York",
            weatherUnit = WeatherUnits(
                temperature = "",
                wind = "km/h",
            ),
            currentTemperature = 10.0,
            maxTemperature = 1.0,
            minTemperature = 2.0,
            windSpeed = 50.0,
            windDirection = 120,
            weatherCode = 1,
            humidityPercent = 65,
            precipitationChancePercent = 40,
            sunrise = "2026-06-14T06:30",
            sunset = "2026-06-14T20:15",
            hourlyForecast = listOf(
                HourlyForecast(time = "2026-06-14T13:00", temperature = 11.0),
            ),
            dailyForecast = listOf(
                DailyForecast(date = "2026-06-14", minTemperature = 2.0, maxTemperature = 8.0),
            ),
        )
    }
}
