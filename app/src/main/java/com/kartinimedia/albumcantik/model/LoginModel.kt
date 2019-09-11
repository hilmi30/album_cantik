package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class LoginModel(

	@SerializedName("access_token")
	var accessToken: String = "",

	@SerializedName("profile")
	var profile: Profile = Profile(),

	@SerializedName("id")
	var id: Int = 0,

	@SerializedName("message")
	var message: String = "",

	@SerializedName("email")
	var email: String = "",

	@SerializedName("isSuccess")
	var isSuccess: Int = 0,

	@SerializedName("username")
	var username: String = "",

	@SerializedName("roles")
	var roles: String = ""
)