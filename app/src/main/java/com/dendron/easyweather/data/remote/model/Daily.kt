package com.dendron.easyweather.data.remote.model

import com.google.gson.annotations.SerializedName

data class Daily(
    @SerializedName("temperature_2m_max")
    val temperature2mMax: List<Double>,
    @SerializedName("temperature_2m_min")
    val temperature2mMin: List<Double>,
    @SerializedName("sunrise")
    val sunrise: List<String>,
    @SerializedName("sunset")
    val sunset: List<String>,
    @SerializedName("time")
    val time: List<String>,
    @SerializedName("windspeed_10m_max")
    val windspeed10mMax: List<Double>,
)
