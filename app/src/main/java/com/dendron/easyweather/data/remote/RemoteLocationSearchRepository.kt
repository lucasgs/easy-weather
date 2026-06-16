package com.dendron.easyweather.data.remote

import com.dendron.easyweather.data.remote.model.toDomain
import com.dendron.easyweather.domain.location.LocationSearchRepository
import com.dendron.easyweather.domain.location.LocationSearchResult
import javax.inject.Inject

class RemoteLocationSearchRepository @Inject constructor(
    private val api: LocationSearchApi,
) : LocationSearchRepository {
    override suspend fun searchLocations(query: String): LocationSearchResult =
        try {
            LocationSearchResult.Success(api.searchLocations(query).toDomain())
        } catch (e: Exception) {
            LocationSearchResult.Failure(e.toLocationSearchFailure())
        }
}
