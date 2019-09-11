package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class AllCategoryModel(

	@SerializedName("data")
	var data: List<DataKategori> = listOf(),

	@SerializedName("totalPage")
	var totalPage: Int = 0,

	@SerializedName("count")
	var count: Int = 0,

	@SerializedName("message")
	var message: String = "",

	@SerializedName("totalCount")
	var totalCount: Int = 0,

	@SerializedName("isSuccess")
	var isSuccess: Int = 0
)