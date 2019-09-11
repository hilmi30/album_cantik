package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class DraftModel(

	@SerializedName("drafts")
	var drafts: List<DataProduct> = listOf(),

	@SerializedName("message")
	var message: String? = "",

	@SerializedName("isSuccess")
	var isSuccess: Int? = 0
)