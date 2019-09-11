package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class SignupModel(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Int? = null
)