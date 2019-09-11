package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class DataDraftByProductId(

	@SerializedName("number")
	var number: String = "",

	@SerializedName("image")
    var image: String = ""
)