package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class DataProduct (
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("kode_produk")
    var kodeProduk: String = "",

    @SerializedName("nama_produk")
    var namaProduk: String = "",

    @SerializedName("deskripsi_produk")
    var deskripsiProduk: String = "",

    @SerializedName("kategori_produk")
    var kategoriProduk: String = "",

    @SerializedName("foto")
    var foto: String = "",

    @SerializedName("harga")
    var harga: Int = 0,

    @SerializedName("harga_reseller")
    var hargaReseller: Int = 0,

    @SerializedName("specification")
    var specification: Specification = Specification(),

    @SerializedName("slider")
    var slider: List<Slider> = listOf()
)