package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class ForgotPassModel(

	@SerializedName("message")
	var message: String = "",

	@SerializedName("isSuccess")
	var isSuccess: Int = 0
)