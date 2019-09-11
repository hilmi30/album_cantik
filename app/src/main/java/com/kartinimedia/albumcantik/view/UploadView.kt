package com.kartinimedia.albumcantik.view

import com.kartinimedia.albumcantik.contract.BaseView
import com.kartinimedia.albumcantik.model.DataDraftByProductId
import com.kartinimedia.albumcantik.model.DataProduct

interface UploadView: BaseView {
    fun error(msg: String, id: String)
    fun showData(it: DataProduct)
    fun prosesUpload(index: Int)
    fun showLoadingSlider()
    fun hideLoadingSlider()
    fun suksesUploadSemua()
    fun showDraft(files: List<DataDraftByProductId>?)
}