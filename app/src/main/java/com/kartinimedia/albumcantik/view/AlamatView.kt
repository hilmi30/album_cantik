package com.kartinimedia.albumcantik.view

import com.kartinimedia.albumcantik.contract.BaseView
import com.kartinimedia.albumcantik.model.DataAlamat

interface AlamatView: BaseView {
    fun showLoading()
    fun hideLoading()
    fun error(msg: String)
    fun showData(data: List<DataAlamat>)
    fun alamatKosong()
}