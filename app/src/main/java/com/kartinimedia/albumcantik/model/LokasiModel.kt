package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class LokasiModel (
    @SerializedName("isSuccess")
    var isSuccess: Int = 0,

    @SerializedName("message")
    var message: String = "",

    @SerializedName("data")
    var data: List<DataLokasi> = listOf()
)