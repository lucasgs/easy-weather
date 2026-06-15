package com.dendron.easyweather.data.remote

import app.cash.turbine.test
import com.dendron.easyweather.MainDispatcherRule
import com.dendron.easyweather.common.Resource
import com.dendron.easyweather.data.remote.model.*
import com.dendron.easyweather.domain.Weather
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class RemoteWeatherRepositoryTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var api: WeatherApi

    private lateinit var weatherRepository: RemoteWeatherRepository

    private val lat = 1.0
    private val long = 2.0

    @Before
    fun setUp() {
        weatherRepository = RemoteWeatherRepository(api)
    }

    @Test
    fun `getCurrentWeather should emit loading and success when API return success`() = runTest {

        val fakeWeatherDto = getFakeWeatherDto()
        val expectedWeather = fakeWeatherDto.toDomain()
        val expectedLoading = Resource.Loading<Weather>()
        val expectedSuccess = Resource.Success(data = expectedWeather)

        whenever(
            api.getCurrentWeather(
                latitude = lat,
                longitude = long,
            )
        ).thenReturn(fakeWeatherDto)

        weatherRepository.getCurrentWeather(lat, long).test {
            assertEquals(expectedLoading, awaitItem())
            assertEquals(expectedSuccess, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `getCurrentWeather should return error when API returns IOException`() = runTest {

        val expectedErrorMessage = "Error"
        val expectedLoading = Resource.Loading<Weather>()
        val expectedError = Resource.Error<Weather>(expectedErrorMessage)

        whenever(
            api.getCurrentWeather(
                latitude = lat,
                longitude = long,
            )
        ).thenAnswer {
            throw IOException()
        }

        weatherRepository.getCurrentWeather(lat, long).test {
            assertEquals(expectedLoading, awaitItem())
            assertEquals(expectedError, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `getCurrentWeather should return error when API returns httpException`() = runTest {

        val expectedErrorMessage = "Error"
        val expectedLoading = Resource.Loading<Weather>()
        val expectedError = Resource.Error<Weather>(expectedErrorMessage)

        whenever(
            api.getCurrentWeather(
                latitude = lat,
                longitude = long,
            )
        ).thenThrow(HttpException::class.java)

        weatherRepository.getCurrentWeather(lat, long).test {
            assertEquals(expectedLoading, awaitItem())
            assertEquals(expectedError, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `getCurrentWeather should return error when API returns any type of exception`() = runTest {

        val expectedErrorMessage = "Error"
        val expectedLoading = Resource.Loading<Weather>()
        val expectedError = Resource.Error<Weather>(expectedErrorMessage)

        whenever(
            api.getCurrentWeather(
                latitude = lat,
                longitude = long,
            )
        ).thenAnswer { throw Exception(expectedErrorMessage) }

        weatherRepository.getCurrentWeather(lat, long).test {
            assertEquals(expectedLoading, awaitItem())
            assertEquals(expectedError, awaitItem())
            awaitComplete()
        }
    }

    private fun getFakeWeatherDto() = WeatherDto(
        currentWeather = CurrentWeather(
            temperature = 1.0,
            time = "2026-06-14T13:00",
            weathercode = 1,
            winddirection = 1,
            windspeed = 1.0,
        ),
        daily = Daily(
            temperature2mMax = listOf(1.0),
            temperature2mMin = listOf(2.0),
            sunrise = listOf("2026-06-14T06:30"),
            sunset = listOf("2026-06-14T20:15"),
            time = listOf("2026-06-14"),
            windspeed10mMax = emptyList(),
        ),
        dailyUnits = DailyUnits(
            temperature2mMax = "",
            temperature2mMin = "",
            sunrise = "",
            sunset = "",
            time = "",
            windspeed10mMax = "",
        ),
        elevation = 0,
        generationtimeMs = 1.0,
        hourly = Hourly(
            temperature2m = listOf(11.0),
            relativeHumidity2m = listOf(65),
            precipitationProbability = listOf(40),
            time = listOf("2026-06-14T13:00"),
        ),
        hourlyUnits = HourlyUnits(
            temperature2m = "",
            relativeHumidity2m = "",
            precipitationProbability = "",
            time = "",
        ),
        latitude = lat,
        longitude = long,
        timezone = "",
        timezoneAbbreviation = "",
        utcOffsetSeconds = 0,
    )
}