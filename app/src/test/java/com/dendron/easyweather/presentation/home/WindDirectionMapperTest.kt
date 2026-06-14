package com.dendron.easyweather.presentation.home

import org.junit.Assert.assertEquals
import org.junit.Test

class WindDirectionMapperTest {

    private val mapper = WindDirectionMapper()

    @Test
    fun `map should return cardinal direction for matching angle`() {
        assertEquals("N", mapper.map(0))
        assertEquals("E", mapper.map(90))
        assertEquals("S", mapper.map(180))
        assertEquals("W", mapper.map(270))
    }

    @Test
    fun `map should return no direction when angle is outside supported range`() {
        assertEquals("No direction", mapper.map(360))
    }
}
