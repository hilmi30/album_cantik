package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class DataAlamat (
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("alamat")
    var alamat: String = "",

    @SerializedName("user_id")
    var dataUser: DataUser = DataUser(),

    @SerializedName("kode_pos")
    var kodePos: Int = 0,

    @SerializedName("atas_nama")
    var atasNama: String = "",

    @SerializedName("nomer_hp")
    var nomerHp: String = "",

    @SerializedName("provinsi")
    var provinsi: String = "",

    @SerializedName("kabupaten")
    var kabupaten: String = "",

    @SerializedName("kecamatan")
    var kecamatan: String = "",

    @SerializedName("id_prov")
    var idProv: Int = 0,

    @SerializedName("id_kab")
    var idKab: Int = 0,

    @SerializedName("id_kec")
    var idKec: Int = 0
)