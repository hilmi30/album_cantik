package com.kartinimedia.albumcantik.presenter

import android.content.Context
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.contract.BasePresenter
import com.kartinimedia.albumcantik.view.ProductView
import com.kartinimedia.albumcantik.utils.Const
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class ProductPresenter: BasePresenter<ProductView> {

    private var view: ProductView? = null
    private var dispo: Disposable? = null

    override fun onAttach(view: ProductView) {
        this.view = view
    }

    override fun onDetach() {
        this.view = null
    }

    fun dispose() {
        dispo?.dispose()
    }

    fun getProduct(context: Context, kategoriId: Int, page: Int) {
        this.view?.showLoading()
        dispo = Const.services.getProduct(kategoriId, page)
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) {
                        this.view?.setPage(it.totalPage)
                        if (page == 1) {
                            this.view?.resetData(it.data)
                        } else {
                            this.view?.showData(it.data)
                        }
                    }
                    else this.view?.error(context.getString(R.string.terjadi_kesalahan))

                    this.view?.hideLoading()
                },
                onError = {
                    if (it is HttpException)
                        this.view?.error(context.getString(R.string.terjadi_kesalahan))
                    else if (it is UnknownHostException || it is TimeoutException || it is SocketTimeoutException || it is ConnectException)
                        this.view?.error(context.getString(R.string.tidak_ada_koneksi))

                    this.view?.hideLoading()
                },
                onComplete = {
                    this.view?.hideLoading()
                }
            )
    }
}