package com.kartinimedia.albumcantik.presenter

import android.content.Context
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.view.AlamatView
import com.kartinimedia.albumcantik.contract.BasePresenter
import com.kartinimedia.albumcantik.utils.Const
import com.kartinimedia.albumcantik.utils.getToken
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

class AlamatPresenter: BasePresenter<AlamatView> {

    private var dispo: Disposable? = null
    private var view: AlamatView? = null

    override fun onAttach(view: AlamatView) {
        this.view = view
    }

    override fun onDetach() {
        view = null
    }

    fun dispose() {
        dispo?.dispose()
    }

    fun getAlamat(context: Context) {
        view?.showLoading()

        dispo = Const.services.getAlamat(getToken(context))
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) view?.showData(it.data)
                    else view?.alamatKosong()

                    view?.hideLoading()
                },
                onError = {
                    if (it is HttpException)
                        view?.error(context.getString(R.string.terjadi_kesalahan))
                    else if (it is UnknownHostException || it is TimeoutException || it is SocketTimeoutException || it is ConnectException)
                        view?.error(context.getString(R.string.tidak_ada_koneksi))

                    view?.hideLoading()
                },
                onComplete = {
                    view?.hideLoading()
                }
            )
    }

}