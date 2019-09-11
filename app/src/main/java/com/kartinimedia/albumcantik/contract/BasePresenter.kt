package com.kartinimedia.albumcantik.contract

interface BasePresenter<T: BaseView> {
    fun onAttach(view: T)
    fun onDetach()
}