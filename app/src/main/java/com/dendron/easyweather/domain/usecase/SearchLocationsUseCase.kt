package com.dendron.easyweather.domain.usecase

import com.dendron.easyweather.domain.location.LocationSearchRepository
import com.dendron.easyweather.domain.location.LocationSearchResult
import javax.inject.Inject

class SearchLocationsUseCase @Inject constructor(
    private val locationSearchRepository: LocationSearchRepository,
) {
    suspend operator fun invoke(query: String): LocationSearchResult =
        locationSearchRepository.searchLocations(query)
}
