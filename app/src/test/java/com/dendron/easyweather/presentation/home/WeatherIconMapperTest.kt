package com.dendron.easyweather.presentation.home

import com.dendron.easyweather.R
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherIconMapperTest {

    private val mapper = WeatherIconMapper()

    @Test
    fun `map should return matching icon`() {
        assertEquals(R.drawable.day_sunny, mapper.map(0))
        assertEquals(R.drawable.rain, mapper.map(63))
    }

    @Test
    fun `map should return fallback icon for unknown code`() {
        assertEquals(R.drawable.na, mapper.map(-1))
    }
}
