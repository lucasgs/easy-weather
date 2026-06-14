package com.dendron.easyweather.presentation.home

import app.cash.turbine.test
import com.dendron.easyweather.MainDispatcherRule
import com.dendron.easyweather.R
import com.dendron.easyweather.common.Resource
import com.dendron.easyweather.domain.Weather
import com.dendron.easyweather.domain.WeatherRepository
import com.dendron.easyweather.domain.WeatherUnits
import com.dendron.easyweather.domain.location.LocationData
import com.dendron.easyweather.domain.location.LocationProvider
import com.dendron.easyweather.domain.location.LocationResult
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
    private lateinit var weatherRepository: WeatherRepository

    @Mock
    private lateinit var locationProvider: LocationProvider

    private lateinit var viewModel: WeatherListViewModel
    private lateinit var weatherUiModelMapper: WeatherUiModelMapper

    @Before
    fun setUp() {
        weatherUiModelMapper = WeatherUiModelMapper(
            weatherDescriptionMapper = WeatherDescriptionMapper(),
            weatherIconMapper = WeatherIconMapper(),
            windDirectionMapper = WindDirectionMapper(),
        )
        viewModel = WeatherListViewModel(
            weatherRepository = weatherRepository,
            locationProvider = locationProvider,
            weatherUiModelMapper = weatherUiModelMapper,
        )
    }

    @Test
    fun `fetchData should emit loading then content when location and weather are available`() = runTest {
        whenever(locationProvider.getCurrentLocation()).thenReturn(
            LocationResult.Success(LocationData(LAT, LONG)),
        )

        whenever(weatherRepository.getCurrentWeather(LAT, LONG)).thenReturn(
            flow {
                emit(Resource.Loading())
                emit(Resource.Success(data = fakeWeather))
            },
        )

        viewModel.state.test {
            assertEquals(WeatherScreenState.Empty, awaitItem())

            viewModel.fetchData()

            assertEquals(WeatherScreenState.Loading, awaitItem())
            val content = awaitItem() as WeatherScreenState.Content
            assertEquals(fakeWeatherUiModel, content.model)
        }
    }

    @Test
    fun `fetchData should emit loading then error when repository fails`() = runTest {
        whenever(locationProvider.getCurrentLocation()).thenReturn(
            LocationResult.Success(LocationData(LAT, LONG)),
        )

        whenever(weatherRepository.getCurrentWeather(LAT, LONG)).thenReturn(
            flow {
                emit(Resource.Loading())
                emit(Resource.Error(message = "Error"))
            },
        )

        viewModel.state.test {
            assertEquals(WeatherScreenState.Empty, awaitItem())

            viewModel.fetchData()

            assertEquals(WeatherScreenState.Loading, awaitItem())
            assertEquals(
                WeatherScreenState.Error(ErrorReason.Network),
                awaitItem(),
            )
        }
    }

    @Test
    fun `fetchData should emit permission required when location permission is missing`() = runTest {
        whenever(locationProvider.getCurrentLocation()).thenReturn(LocationResult.PermissionDenied)

        viewModel.state.test {
            assertEquals(WeatherScreenState.Empty, awaitItem())

            viewModel.fetchData()

            assertEquals(WeatherScreenState.Loading, awaitItem())
            assertEquals(WeatherScreenState.PermissionRequired, awaitItem())
        }
    }

    @Test
    fun `fetchData should emit location disabled when providers are off`() = runTest {
        whenever(locationProvider.getCurrentLocation()).thenReturn(LocationResult.LocationDisabled)

        viewModel.state.test {
            assertEquals(WeatherScreenState.Empty, awaitItem())

            viewModel.fetchData()

            assertEquals(WeatherScreenState.Loading, awaitItem())
            assertEquals(WeatherScreenState.LocationDisabled, awaitItem())
        }
    }

    @Test
    fun `fetchData should emit location unavailable error when no location is returned`() = runTest {
        whenever(locationProvider.getCurrentLocation()).thenReturn(LocationResult.Unavailable)

        viewModel.state.test {
            assertEquals(WeatherScreenState.Empty, awaitItem())

            viewModel.fetchData()

            assertEquals(WeatherScreenState.Loading, awaitItem())
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
            descriptionText = "Mainly clear, partly cloudy, and overcast",
            windText = "50.0 km/h SE",
            temperatureText = "2.0° / 10.0° / 1.0°",
            weatherIcon = R.drawable.cloudy,
        )

        val fakeWeather = Weather(
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
        )
    }
}
