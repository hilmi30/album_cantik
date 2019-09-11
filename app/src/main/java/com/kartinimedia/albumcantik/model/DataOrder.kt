package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class DataOrder (
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("kode_order")
    var kodeOrder: String = "",

    @SerializedName("tanggal_order")
    var tanggalOrder: String = "",

    @SerializedName("status_order")
    var statusOrder: String = "",

    @SerializedName("customer_id")
    var customerId: String = "",

    @SerializedName("status_lunas")
    var statusLunas: Int = 0,

    @SerializedName("lunas")
    var lunas: String = "",

    @SerializedName("order_name")
    var orderName: String = "",

    @SerializedName("order_address")
    var orderAddress: String = "",

    @SerializedName("order_phone")
    var orderPhone: String = "",

    @SerializedName("order_email")
    var orderEmail: String = "",

    @SerializedName("order_note")
    var orderNote: String = "",

    @SerializedName("order_prov")
    var orderProv: String = "",

    @SerializedName("order_kab")
    var orderKab: String = "",

    @SerializedName("kode_pos")
    var kodePos: Int = 0,

    @SerializedName("order_kec")
    var orderKec: String = "",

    @SerializedName("payment_id")
    var paymentId: Int = 0,

    @SerializedName("metode_pembayaran")
    var metodePembayaran: String = "",

    @SerializedName("tanggal_jadi")
    var tanggalJadi: String = "",

    @SerializedName("total_harga")
    var totalHarga: Int = 0,

    @SerializedName("jumlah_order")
    var jumlahOrder: Int = 0,

    @SerializedName("detail_order")
    var detailOrder: DetailOrder = DetailOrder()
)