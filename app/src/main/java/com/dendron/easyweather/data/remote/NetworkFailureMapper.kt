package com.dendron.easyweather.data.remote

import com.dendron.easyweather.domain.WeatherFailure
import com.dendron.easyweather.domain.location.LocationSearchFailure
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

internal fun Throwable.toWeatherFailure(): WeatherFailure = when (this) {
    is SocketTimeoutException -> WeatherFailure.Timeout
    is HttpException, is IOException -> WeatherFailure.Network
    else -> WeatherFailure.Unknown
}

internal fun Throwable.toLocationSearchFailure(): LocationSearchFailure = when (this) {
    is SocketTimeoutException -> LocationSearchFailure.Timeout
    is HttpException, is IOException -> LocationSearchFailure.Network
    else -> LocationSearchFailure.Unknown
}
