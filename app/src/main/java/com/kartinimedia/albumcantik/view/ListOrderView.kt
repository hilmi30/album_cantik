package com.kartinimedia.albumcantik.view

import com.kartinimedia.albumcantik.contract.BaseView
import com.kartinimedia.albumcantik.model.DataOrder

interface ListOrderView: BaseView {
    fun showLoading()
    fun error(msg: String)
    fun hideLoading()
    fun showData(dataOrder: List<DataOrder>)
    fun setTotalPage(totalPage: Int)
    fun showCurrentData(dataOrder: List<DataOrder>)
    fun orderKosong()
}