package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class DataLokasi (
    @SerializedName("id_prov")
    var idProv: String = "",

    @SerializedName("nama")
    var nama: String = "",

    @SerializedName("id_kab")
    var idKab: String = "",

    @SerializedName("id_kec")
    var idKec: String = "",

    @SerializedName("kabupaten")
    var kabupaten: String = "",

    @SerializedName("kecamatan")
    var kecamatan: String = "",

    @SerializedName("provinsi")
    var provinsi: String = "",

    @SerializedName("jenis")
    var jenis: String = ""
)