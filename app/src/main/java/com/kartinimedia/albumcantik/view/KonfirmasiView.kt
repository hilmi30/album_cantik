package com.kartinimedia.albumcantik.view

import com.kartinimedia.albumcantik.contract.BaseView

interface KonfirmasiView: BaseView {
    fun showLoading()
    fun suksesKonfirmasi()
    fun error(msg: String)
    fun hideLoading()
}