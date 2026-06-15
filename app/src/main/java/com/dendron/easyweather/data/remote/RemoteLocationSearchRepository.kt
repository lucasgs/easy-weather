package com.dendron.easyweather.data.remote

import com.dendron.easyweather.common.Resource
import com.dendron.easyweather.data.remote.model.toDomain
import com.dendron.easyweather.domain.location.LocationSearchRepository
import com.dendron.easyweather.domain.location.SearchedLocation
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RemoteLocationSearchRepository @Inject constructor(
    private val api: LocationSearchApi,
) : LocationSearchRepository {
    override suspend fun searchLocations(query: String): Resource<List<SearchedLocation>> =
        try {
            Resource.Success(api.searchLocations(query).toDomain())
        } catch (e: HttpException) {
            Resource.Error(e.localizedMessage)
        } catch (e: IOException) {
            Resource.Error(e.localizedMessage)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage)
        }
}
