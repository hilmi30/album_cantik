package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class DataKategori (
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("nama_kategori")
    var namaKategori: String = "",

    @SerializedName("image")
    var image: String = ""
)