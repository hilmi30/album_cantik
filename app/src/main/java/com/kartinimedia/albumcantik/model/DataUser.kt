package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class DataUser(

	@SerializedName("access_token")
	var accessToken: String = "",

	@SerializedName("id")
	var id: Int = 0,

	@SerializedName("email")
	var email: String = "",

	@SerializedName("username")
	var username: String = ""
)