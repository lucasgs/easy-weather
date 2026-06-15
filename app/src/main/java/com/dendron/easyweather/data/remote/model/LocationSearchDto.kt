package com.dendron.easyweather.data.remote.model

import com.dendron.easyweather.domain.location.SearchedLocation
import com.google.gson.annotations.SerializedName

data class LocationSearchDto(
    @SerializedName("results")
    val results: List<LocationResultDto>?,
)

data class LocationResultDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("admin1")
    val admin1: String?,
    @SerializedName("country")
    val country: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
)

fun LocationSearchDto.toDomain(): List<SearchedLocation> =
    results.orEmpty().map { result ->
        SearchedLocation(
            name = result.name,
            admin1 = result.admin1,
            country = result.country,
            latitude = result.latitude,
            longitude = result.longitude,
        )
    }
