package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class ProductModel(

	@field:SerializedName("data")
	val data: List<DataProduct> = listOf(),

	@field:SerializedName("totalPage")
	val totalPage: Int? = null,

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("totalCount")
	val totalCount: Int? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Int? = null
)