package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class Gender(

	@SerializedName("gender")
	var gender: String = "",

	@SerializedName("id")
	var id: Int = 0
)