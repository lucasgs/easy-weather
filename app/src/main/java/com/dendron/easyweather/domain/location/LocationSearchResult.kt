package com.dendron.easyweather.domain.location

sealed interface LocationSearchResult {
    data class Success(val locations: List<SearchedLocation>) : LocationSearchResult
    data class Failure(val error: LocationSearchFailure) : LocationSearchResult
}

sealed interface LocationSearchFailure {
    data object Network : LocationSearchFailure
    data object Timeout : LocationSearchFailure
    data object Unknown : LocationSearchFailure
}
