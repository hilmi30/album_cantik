package com.kartinimedia.albumcantik.utils

import com.kartinimedia.albumcantik.BuildConfig
import com.kartinimedia.albumcantik.network.ApiRepo
import com.kartinimedia.albumcantik.network.RetrofitBuilder

class Const {
    companion object {

        val services: ApiRepo = RetrofitBuilder.getInstance(BuildConfig.BASE_URL_API).create(ApiRepo::class.java)

        const val position = "pos"
        const val title = "title"
        const val timeOutDebounce = 300 // Milisecond
        const val timeOut = 60 // Second
        const val timeOutDebounceUpload = 300 // Milisecond
        const val timeOutUpload = 1000 // Second
        const val userPref = "userPref"
        const val status = "status"
        const val id = "id"
        const val username = "username"
        const val email = "email"
        const val token = "accessToken"
        const val fullName = "fullName"
        const val noHp = "noHp"
        const val alamat = "alamat"
        const val genderId = "genderId"
        const val namaGender = "namaGender"
        const val image = "image"
        const val genderData = "genderData"
        const val genderId1 = "genderId1"
        const val genderId2 = "genderId2"
        const val genderName1 = "genderName1"
        const val genderName2 = "genderName2"
        const val kategoriId = "kategoriId"
        const val kategoriNama = "kategoriNama"
        const val totalFoto = "totalFoto"
        const val slider = "slider"
        const val upload = "upload"
        const val dbName = "draft"
        const val draftTable = "draftTable"
        const val isDraft = "isDraft"
        const val draft = "draft"
        const val provinsiTable = "provinsiTable"
        const val kodePos = "kodePos"
        const val prov = "prov"
        const val kab = "kab"
        const val kec = "kec"
        const val atasNama = "atasNama"
        const val productId = "productId"
        const val harga = "harga"
        const val namaProduct = "namaProduct"
        const val isDetailOrder = "isDetailOrder"
        const val jumlahOrder = "jumlahOrder"
        const val kodeOrder = "kodeOrder"
        const val statusOrder = "statusOrder"
        const val tglOrder = "tglOrder"
        const val statusPembayaran = "statusPembayaran"
        const val metodePembayaran = "metodePembayaran"
        const val dataBank = "dataBank"
        const val dataRekening = "dataRekening"
        const val hargaUnik = "hargaUnik"
        const val statusLunas = "statusLunas"
        const val orderNote = "orderNote"
        const val dataSize = "dataSize"
        const val roles = "roles"
        const val customer = "customer"
        const val reseller = "reseller"
        const val hargaReseller = "hargaReseller"
        const val orderName = "orderName"
        const val idProv = "idProv"
        const val idKab = "idKab"
        const val idKec = "idKec"
        const val desc = "desc"
    }
}
