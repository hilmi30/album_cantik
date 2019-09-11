package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class DataPayment (
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("payment_method")
    var paymentMethod: String = "",

    @SerializedName("list_payment")
    var listPayment: List<DataListPayment> = listOf()
)