package com.dendron.easyweather.data.remote

import com.dendron.easyweather.data.remote.model.toDomain
import com.dendron.easyweather.domain.location.LocationSearchFailure
import com.dendron.easyweather.domain.location.LocationSearchRepository
import com.dendron.easyweather.domain.location.LocationSearchResult
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RemoteLocationSearchRepository @Inject constructor(
    private val api: LocationSearchApi,
) : LocationSearchRepository {
    override suspend fun searchLocations(query: String): LocationSearchResult =
        try {
            LocationSearchResult.Success(api.searchLocations(query).toDomain())
        } catch (e: HttpException) {
            LocationSearchResult.Failure(LocationSearchFailure.Network)
        } catch (e: IOException) {
            LocationSearchResult.Failure(LocationSearchFailure.Network)
        } catch (e: Exception) {
            LocationSearchResult.Failure(LocationSearchFailure.Unknown)
        }
}
