package com.dendron.easyweather.data.remote

import com.dendron.easyweather.data.remote.model.LocationSearchDto
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationSearchApi {
    @GET("search")
    suspend fun searchLocations(
        @Query("name") name: String,
        @Query("count") count: Int = 10,
        @Query("language") language: String = "en",
        @Query("format") format: String = "json",
    ): LocationSearchDto
}
