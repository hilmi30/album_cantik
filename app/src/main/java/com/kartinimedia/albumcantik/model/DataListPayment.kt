package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class DataListPayment (
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("payment_method")
    var paymentMethod: String = "",

    @SerializedName("nomer_rekening")
    var nomerRekening: String = ""
)