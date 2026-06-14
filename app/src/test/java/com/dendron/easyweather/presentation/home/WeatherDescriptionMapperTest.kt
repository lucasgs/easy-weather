package com.dendron.easyweather.presentation.home

import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherDescriptionMapperTest {

    private val mapper = WeatherDescriptionMapper()

    @Test
    fun `map should return matching description`() {
        assertEquals("Clear sky", mapper.map(0))
        assertEquals("Rain: Slight, moderate and heavy intensity", mapper.map(63))
    }

    @Test
    fun `map should return fallback description for unknown code`() {
        assertEquals("Nothing to say :(", mapper.map(-1))
    }
}
