package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class PembayaranModel (
    @SerializedName("isSuccess")
    var isSuccess: Int = 0,

    @SerializedName("message")
    var message: String = ""
)