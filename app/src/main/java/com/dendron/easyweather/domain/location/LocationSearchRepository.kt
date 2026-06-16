package com.dendron.easyweather.domain.location

interface LocationSearchRepository {
    suspend fun searchLocations(query: String): LocationSearchResult
}
