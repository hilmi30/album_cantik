package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class Profile(

	@SerializedName("image")
	var image: String = "",

	@SerializedName("full_name")
	var fullName: String = "",

	@SerializedName("gender")
	var gender: Gender = Gender(),

	@SerializedName("nomer_hp")
	var nomerHp: String? = "",

	@SerializedName("alamat")
	var alamat: DataAlamat = DataAlamat()
)