package com.kartinimedia.albumcantik.view

import com.kartinimedia.albumcantik.contract.BaseView
import com.kartinimedia.albumcantik.model.DataProduct

interface DraftView: BaseView {
    fun showDraft(drafts: List<DataProduct>)
    fun showLoading()
    fun error(msg: String)
    fun hideLoading()
    fun dataKosong()
    fun suksesHapusDraft()
}