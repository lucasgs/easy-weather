package com.dendron.easyweather.data.remote.model

import com.dendron.easyweather.domain.location.SearchedLocation

fun LocationSearchDto.toDomain(): List<SearchedLocation> =
    results.orEmpty().map { result ->
        SearchedLocation(
            name = result.name,
            admin1 = result.admin1,
            country = result.country,
            latitude = result.latitude,
            longitude = result.longitude,
        )
    }
