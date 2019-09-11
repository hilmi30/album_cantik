package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class ListOrderModel (
    @SerializedName("isSuccess")
    var isSuccess: Int = 0,

    @SerializedName("message")
    var message: String = "",

    @SerializedName("data")
    var dataOrder: List<DataOrder> = listOf(),

    @SerializedName("count")
    var count: Int = 0,

    @SerializedName("totalCount")
    var totalCount: Int = 0,

    @SerializedName("totalPage")
    var totalPage: Int = 0
)