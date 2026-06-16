package com.dendron.easyweather.data.remote.model

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
