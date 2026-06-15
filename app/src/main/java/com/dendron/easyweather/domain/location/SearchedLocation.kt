package com.dendron.easyweather.domain.location

data class SearchedLocation(
    val name: String,
    val admin1: String?,
    val country: String,
    val latitude: Double,
    val longitude: Double,
) {
    val displayName: String
        get() = listOfNotNull(name, admin1, country).joinToString(", ")
}
