package com.kartinimedia.albumcantik.view

import com.kartinimedia.albumcantik.contract.BaseView
import com.kartinimedia.albumcantik.model.DataProduct

interface ProductView: BaseView {
    fun showLoading()
    fun hideLoading()
    fun showData(data: List<DataProduct>)
    fun error(msg: String)
    fun resetData(data: List<DataProduct>)
    fun setPage(totalPage: Int?)
}