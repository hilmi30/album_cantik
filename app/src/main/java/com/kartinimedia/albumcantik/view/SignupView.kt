package com.kartinimedia.albumcantik.view

import com.kartinimedia.albumcantik.contract.BaseView

interface SignupView: BaseView {
    fun error(msg: String)
    fun suksesRegister()
    fun showLoading()
    fun hideLoading()
}