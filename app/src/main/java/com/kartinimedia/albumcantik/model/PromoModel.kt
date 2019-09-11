package com.kartinimedia.albumcantik.model
import com.google.gson.annotations.SerializedName


data class PromoModel(
    @SerializedName("data")
    val data: List<Promo>,
    @SerializedName("count")
    val count: Int,
    @SerializedName("isSuccess")
    val isSuccess: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("totalCount")
    val totalCount: Int,
    @SerializedName("totalPage")
    val totalPage: Int
)
