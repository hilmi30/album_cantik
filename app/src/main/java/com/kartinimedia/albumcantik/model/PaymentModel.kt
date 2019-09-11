package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class PaymentModel(
    @SerializedName("isSuccess")
    var isSuccess: Int = 0,

    @SerializedName("message")
    var message: String = "",

    @SerializedName("data")
    var dataPayment: List<DataPayment> = listOf()
)