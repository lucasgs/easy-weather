package com.dendron.easyweather.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.dendron.easyweather.data.local.WeatherCacheDao
import com.dendron.easyweather.data.local.WeatherDatabase
import com.dendron.easyweather.data.location.DefaultLocationProvider
import com.dendron.easyweather.data.preferences.DataStoreWeatherPreferencesRepository
import com.dendron.easyweather.data.remote.LocationSearchApi
import com.dendron.easyweather.data.remote.NetworkConfig
import com.dendron.easyweather.data.remote.RemoteLocationSearchRepository
import com.dendron.easyweather.data.remote.RemoteWeatherRepository
import com.dendron.easyweather.data.remote.WeatherApi
import com.dendron.easyweather.domain.WeatherRepository
import com.dendron.easyweather.domain.location.LocationProvider
import com.dendron.easyweather.domain.location.LocationSearchRepository
import com.dendron.easyweather.domain.preferences.WeatherPreferencesRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (NetworkConfig.ENABLE_HTTP_LOGGING) {
            HttpLoggingInterceptor.Level.BASIC
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(NetworkConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(NetworkConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(NetworkConfig.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext appContext: Context): WeatherDatabase =
        Room.databaseBuilder(
            appContext,
            WeatherDatabase::class.java,
            "easyweather.db",
        ).build()

    @Provides
    @Singleton
    fun provideWeatherCacheDao(database: WeatherDatabase): WeatherCacheDao = database.weatherCacheDao()

    @Provides
    @Singleton
    fun provideWeatherApi(okHttpClient: OkHttpClient): WeatherApi =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(NetworkConfig.WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun provideWeatherPreferencesRepository(
        @ApplicationContext appContext: Context,
    ): WeatherPreferencesRepository = DataStoreWeatherPreferencesRepository(appContext)

    @Provides
    @Singleton
    fun provideWeatherRepository(
        api: WeatherApi,
        weatherCacheDao: WeatherCacheDao,
        weatherPreferencesRepository: WeatherPreferencesRepository,
    ): WeatherRepository {
        return RemoteWeatherRepository(api, weatherCacheDao, weatherPreferencesRepository)
    }

    @Provides
    @Singleton
    fun provideLocationSearchApi(okHttpClient: OkHttpClient): LocationSearchApi = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(NetworkConfig.GEOCODING_BASE_URL)
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