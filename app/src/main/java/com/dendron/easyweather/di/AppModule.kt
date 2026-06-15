package com.dendron.easyweather.di

import android.app.Application
import android.content.Context
import com.dendron.easyweather.data.location.DefaultLocationProvider
import com.dendron.easyweather.data.remote.LocationSearchApi
import com.dendron.easyweather.data.remote.RemoteLocationSearchRepository
import com.dendron.easyweather.data.remote.RemoteWeatherRepository
import com.dendron.easyweather.data.remote.WeatherApi
import com.dendron.easyweather.domain.WeatherRepository
import com.dendron.easyweather.domain.location.LocationProvider
import com.dendron.easyweather.domain.location.LocationSearchRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi {

        val moviesClient = OkHttpClient().newBuilder()
            .build()

        return Retrofit.Builder()
            .client(moviesClient)
            .baseUrl("https://api.open-meteo.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(api: WeatherApi): WeatherRepository {
        return RemoteWeatherRepository(api)
    }

    @Provides
    @Singleton
    fun provideLocationSearchApi(): LocationSearchApi = Retrofit.Builder()
        .baseUrl("https://geocoding-api.open-meteo.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(LocationSearchApi::class.java)

    @Provides
    @Singleton
    fun provideLocationSearchRepository(api: LocationSearchApi): LocationSearchRepository {
        return RemoteLocationSearchRepository(api)
    }

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(@ApplicationContext appContext: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(appContext)
    }

    @Provides
    @Singleton
    fun provideLocationProvider(
        appContext: Application,
        locationClient: FusedLocationProviderClient,
    ): LocationProvider {
        return DefaultLocationProvider(
            locationClient = locationClient,
            application = appContext,
        )
    }
}