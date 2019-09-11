package com.kartinimedia.albumcantik.view

import com.kartinimedia.albumcantik.contract.BaseView
import com.kartinimedia.albumcantik.model.UpdateProfileModel

interface AccountView: BaseView {
    fun showLoading()
    fun hideLoading()
    fun suksesUpdate(it: UpdateProfileModel?)
    fun error(msg: String)
}