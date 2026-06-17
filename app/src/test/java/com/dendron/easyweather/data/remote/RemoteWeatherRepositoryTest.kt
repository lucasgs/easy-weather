package com.dendron.easyweather.data.remote

import app.cash.turbine.test
import com.dendron.easyweather.MainDispatcherRule
import com.dendron.easyweather.data.local.WeatherCacheDao
import com.dendron.easyweather.data.local.model.toCachedEntity
import com.dendron.easyweather.data.remote.model.CurrentWeather
import com.dendron.easyweather.data.remote.model.Daily
import com.dendron.easyweather.data.remote.model.DailyUnits
import com.dendron.easyweather.data.remote.model.Hourly
import com.dendron.easyweather.data.remote.model.HourlyUnits
import com.dendron.easyweather.data.remote.model.WeatherDto
import com.dendron.easyweather.data.remote.model.toDomain
import com.dendron.easyweather.domain.WeatherFailure
import com.dendron.easyweather.domain.WeatherResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.check
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
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

    @Mock
    private lateinit var weatherCacheDao: WeatherCacheDao

    private lateinit var weatherRepository: RemoteWeatherRepository

    private val lat = 1.0
    private val long = 2.0

    @Before
    fun setUp() {
        weatherRepository = RemoteWeatherRepository(api, weatherCacheDao)
    }

    @Test
    fun `getCurrentWeather should emit loading and success when API return success`() = runTest {
        val fakeWeatherDto = getFakeWeatherDto()
        val expectedWeather = fakeWeatherDto.toDomain()
        whenever(weatherCacheDao.getByCoordinates(lat, long)).thenReturn(null)

        whenever(
            api.getCurrentWeather(
                latitude = lat,
                longitude = long,
            ),
        ).thenReturn(fakeWeatherDto)

        weatherRepository.getCurrentWeather(lat, long).test {
            assertEquals(WeatherResult.Loading, awaitItem())
            val success = awaitItem() as WeatherResult.Success
            assertEquals(expectedWeather, success.weather)
            assertEquals(false, success.isFromCache)
            assertEquals(false, success.isStale)
            assert(success.lastUpdatedAtMillis > 0L)
            awaitComplete()
        }

        verify(weatherCacheDao).upsert(
            check { cachedWeather ->
                assertEquals(lat, cachedWeather.latitude, 0.0)
                assertEquals(long, cachedWeather.longitude, 0.0)
                assertEquals(expectedWeather.locationName, cachedWeather.locationName)
                assertEquals(expectedWeather.currentTemperature, cachedWeather.currentTemperature, 0.0)
            },
        )
    }

    @Test
    fun `getCurrentWeather should return network failure when API returns IOException`() = runTest {
        whenever(weatherCacheDao.getByCoordinates(lat, long)).thenReturn(null)
        whenever(
            api.getCurrentWeather(
                latitude = lat,
                longitude = long,
            ),
        ).thenAnswer {
            throw IOException()
        }

        weatherRepository.getCurrentWeather(lat, long).test {
            assertEquals(WeatherResult.Loading, awaitItem())
            assertEquals(WeatherResult.Failure(WeatherFailure.Network), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `getCurrentWeather should return network failure when API returns httpException`() = runTest {
        whenever(weatherCacheDao.getByCoordinates(lat, long)).thenReturn(null)
        whenever(
            api.getCurrentWeather(
                latitude = lat,
                longitude = long,
            ),
        ).thenThrow(HttpException::class.java)

        weatherRepository.getCurrentWeather(lat, long).test {
            assertEquals(WeatherResult.Loading, awaitItem())
            assertEquals(WeatherResult.Failure(WeatherFailure.Network), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `getCurrentWeather should return unknown failure when API returns any type of exception`() = runTest {
        whenever(weatherCacheDao.getByCoordinates(lat, long)).thenReturn(null)
        whenever(
            api.getCurrentWeather(
                latitude = lat,
                longitude = long,
            ),
        ).thenAnswer { throw Exception("Error") }

        weatherRepository.getCurrentWeather(lat, long).test {
            assertEquals(WeatherResult.Loading, awaitItem())
            assertEquals(WeatherResult.Failure(WeatherFailure.Unknown), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `getCurrentWeather should emit cached weather before refreshed weather when cache exists`() = runTest {
        val fakeWeatherDto = getFakeWeatherDto()
        val expectedWeather = fakeWeatherDto.toDomain()
        val cachedAtMillis = 1234L
        whenever(weatherCacheDao.getByCoordinates(lat, long)).thenReturn(
            expectedWeather.toCachedEntity(lat, long, cachedAtMillis),
        )
        whenever(api.getCurrentWeather(latitude = lat, longitude = long)).thenReturn(fakeWeatherDto)

        weatherRepository.getCurrentWeather(lat, long).test {
            assertEquals(WeatherResult.Loading, awaitItem())

            val cached = awaitItem() as WeatherResult.Success
            assertEquals(expectedWeather, cached.weather)
            assertEquals(true, cached.isFromCache)
            assertEquals(false, cached.isStale)
            assertEquals(cachedAtMillis, cached.lastUpdatedAtMillis)

            val refreshed = awaitItem() as WeatherResult.Success
            assertEquals(expectedWeather, refreshed.weather)
            assertEquals(false, refreshed.isFromCache)
            assertEquals(false, refreshed.isStale)
            assert(refreshed.lastUpdatedAtMillis >= cachedAtMillis)
            awaitComplete()
        }
    }

    @Test
    fun `getCurrentWeather should emit stale cached weather when refresh fails`() = runTest {
        val fakeWeatherDto = getFakeWeatherDto()
        val expectedWeather = fakeWeatherDto.toDomain()
        val cachedAtMillis = 1234L
        whenever(weatherCacheDao.getByCoordinates(lat, long)).thenReturn(
            expectedWeather.toCachedEntity(lat, long, cachedAtMillis),
        )
        whenever(api.getCurrentWeather(latitude = lat, longitude = long)).thenAnswer { throw IOException() }

        weatherRepository.getCurrentWeather(lat, long).test {
            assertEquals(WeatherResult.Loading, awaitItem())

            val cached = awaitItem() as WeatherResult.Success
            assertEquals(true, cached.isFromCache)
            assertEquals(false, cached.isStale)

            val stale = awaitItem() as WeatherResult.Success
            assertEquals(expectedWeather, stale.weather)
            assertEquals(true, stale.isFromCache)
            assertEquals(true, stale.isStale)
            assertEquals(cachedAtMillis, stale.lastUpdatedAtMillis)
            awaitComplete()
        }

        verify(weatherCacheDao, never()).upsert(any())
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
