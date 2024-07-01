package com.dicoding.tanaminai.data.remote.weather

import com.google.gson.annotations.SerializedName

data class WeatherResponse(

    @field:SerializedName("main")
	val main: Main? = null,

    @field:SerializedName("sys")
	val sys: Sys? = null,

    @field:SerializedName("weather")
	val weather: List<WeatherItem?>? = null,

    @field:SerializedName("name")
	val name: String? = null,

    )

data class Sys(
	
	@field:SerializedName("country")
	val country: String? = null,
)

data class WeatherItem(

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("main")
	val main: String? = null,
	
)

data class Main(
	@field:SerializedName("temp")
	val temp: Double? = null,
)

