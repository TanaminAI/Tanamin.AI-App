package com.dicoding.tanaminai.data.remote.soil

import com.google.gson.annotations.SerializedName

data class SoilResponse(

	@field:SerializedName("P")
	val p: Double,

	@field:SerializedName("hum")
	val hum: Double,

	@field:SerializedName("temp")
	val temp: Double? = null,

	@field:SerializedName("ph")
	val ph: Double? = null,

	@field:SerializedName("K")
	val k: Double? = null,

	@field:SerializedName("N")
	val n: Double? = null,

	@field:SerializedName("timestamp")
	val timestamp: Long
)
