package com.dendron.easyweather.domain.location

import com.dendron.easyweather.common.Resource

interface LocationSearchRepository {
    suspend fun searchLocations(query: String): Resource<List<SearchedLocation>>
}
