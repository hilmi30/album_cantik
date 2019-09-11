package com.kartinimedia.albumcantik.view

import com.kartinimedia.albumcantik.contract.BaseView
import com.kartinimedia.albumcantik.model.DataAlamat
import com.kartinimedia.albumcantik.model.DataLokasi

interface FormAlamatView: BaseView {
    fun showProvinsi(data: List<DataLokasi>)
    fun error(msg: String)
    fun showKabupaten(data: List<DataLokasi>)
    fun showKecamatan(data: List<DataLokasi>)
    fun sukseSimpanAlamat(data: DataAlamat)
    fun showLoading()
    fun hideLoading()
    fun suksesAlamatUtama()
    fun suksesUbahAlamat(data: DataAlamat)
    fun suksesHapusAlamat()
}