package com.kartinimedia.albumcantik.view

import com.kartinimedia.albumcantik.contract.BaseView
import com.kartinimedia.albumcantik.model.LoginModel

interface LoginView: BaseView {
    fun showLoading()
    fun suksesLogin(it: LoginModel)
    fun error(msg: String)
    fun hideLoading()
    fun suksesResend()
    fun suksesReqLupaPass()
}