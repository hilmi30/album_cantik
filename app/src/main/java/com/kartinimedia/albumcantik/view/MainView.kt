package com.kartinimedia.albumcantik.view

import com.kartinimedia.albumcantik.contract.BaseView
import com.kartinimedia.albumcantik.model.DataKategori
import com.kartinimedia.albumcantik.model.DataProduct
import com.kartinimedia.albumcantik.model.Promo

interface MainView: BaseView {
    fun showLoading()
    fun hideLoading()
    fun error(msg: String)
    fun showData(it: List<DataKategori>)
    fun showDraft(drafts: List<DataProduct>)
    fun draftKosong()
    fun setTotalPage(totalPage: Int)
    fun showCurrentData(data: List<DataKategori>)
    fun showPromo(data: List<Promo>)
    fun promoKosong()
    fun promoError()
}