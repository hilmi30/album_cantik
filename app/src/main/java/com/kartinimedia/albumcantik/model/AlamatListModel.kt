package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class AlamatListModel(

	@SerializedName("data")
	var data: List<DataAlamat> = listOf(),

	@SerializedName("totalPage")
	var totalPage: Int? = null,

	@SerializedName("count")
	var count: Int? = null,

	@SerializedName("message")
	var message: String? = null,

	@SerializedName("totalCount")
	var totalCount: Int? = null,

	@SerializedName("isSuccess")
	var isSuccess: Int? = null
)