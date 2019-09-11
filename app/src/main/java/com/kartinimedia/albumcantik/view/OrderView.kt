package com.kartinimedia.albumcantik.view

import com.kartinimedia.albumcantik.contract.BaseView
import com.kartinimedia.albumcantik.model.DataAlamat
import com.kartinimedia.albumcantik.model.DataOrder
import com.kartinimedia.albumcantik.model.DataPayment

interface OrderView: BaseView {
    fun showLoading()
    fun showAlamat(data: DataAlamat)
    fun alamatKosong()
    fun error(msg: String)
    fun hideLoading()
    fun showPayment(dataPayment: List<DataPayment>)
    fun suksesOrder(dataOrder: DataOrder)
    fun suksesHapusOrder()
}