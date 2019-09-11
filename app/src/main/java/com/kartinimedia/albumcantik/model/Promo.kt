package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class Promo(
    @SerializedName("caption")
    val caption: String,
    @SerializedName("gambar")
    val gambar: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("deskripsi")
    val deskripsi: String
)