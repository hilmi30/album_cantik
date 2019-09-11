package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class DraftByProductIdModel(

	@SerializedName("files")
	var files: List<DataDraftByProductId> = listOf(),

	@SerializedName("message")
	var message: String = "",

	@SerializedName("isSuccess")
	var isSuccess: Int = 0
)