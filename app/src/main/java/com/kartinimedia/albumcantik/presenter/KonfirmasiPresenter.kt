package com.kartinimedia.albumcantik.presenter

import android.content.Context
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.contract.BasePresenter
import com.kartinimedia.albumcantik.utils.Const
import com.kartinimedia.albumcantik.utils.getToken
import com.kartinimedia.albumcantik.view.KonfirmasiView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class KonfirmasiPresenter: BasePresenter<KonfirmasiView> {

    private var view: KonfirmasiView? = null
    private var dispo: Disposable? = null

    override fun onAttach(view: KonfirmasiView) {
        this.view = view
    }

    override fun onDetach() {
        view = null
    }

    fun dispo() {
        dispo?.dispose()
    }

    fun konfirmasiPembayaran(context: Context, orderId: Int, image: MultipartBody.Part) {
        view?.showLoading()

        dispo = Const.services.konfirmasiPembayaran(getToken(context), orderId, image)
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) this.view?.suksesKonfirmasi()
                    else this.view?.error(context.getString(R.string.terjadi_kesalahan))

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