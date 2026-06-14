package com.dendron.easyweather.presentation.home

import javax.inject.Inject

class WindDirectionMapper @Inject constructor() {
    fun map(angle: Int): String = when (angle) {
        in 0..22 -> "N"
        in 22..67 -> "NE"
        in 67..112 -> "E"
        in 112..157 -> "SE"
        in 157..202 -> "S"
        in 202..247 -> "SW"
        in 247..292 -> "W"
        in 292..337 -> "NW"
        else -> "No direction"
    }
}
