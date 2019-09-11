package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class Specification(

	@SerializedName("total_foto")
	var totalFoto: Int = 0,

	@SerializedName("total_halaman")
	var totalHalaman: String = ""
)