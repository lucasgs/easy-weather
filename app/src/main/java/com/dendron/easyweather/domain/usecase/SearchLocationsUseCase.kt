package com.dendron.easyweather.domain.usecase

import com.dendron.easyweather.common.Resource
import com.dendron.easyweather.domain.location.LocationSearchRepository
import com.dendron.easyweather.domain.location.SearchedLocation
import javax.inject.Inject

class SearchLocationsUseCase @Inject constructor(
    private val locationSearchRepository: LocationSearchRepository,
) {
    suspend operator fun invoke(query: String): Resource<List<SearchedLocation>> =
        locationSearchRepository.searchLocations(query)
}
