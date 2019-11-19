package com.kartinimedia.albumcantik.presenter

import android.content.Context
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.contract.BasePresenter
import com.kartinimedia.albumcantik.view.DraftView
import com.kartinimedia.albumcantik.utils.Const
import com.kartinimedia.albumcantik.utils.checkError
import com.kartinimedia.albumcantik.utils.getToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import okhttp3.internal.Util
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class DraftPresenter: BasePresenter<DraftView> {

    private var view: DraftView? = null
    private var dispo: Disposable? = null

    override fun onAttach(view: DraftView) {
        this.view = view
    }

    override fun onDetach() {
        view = null
    }

    fun dispose() {
        dispo?.dispose()
    }

    fun getAllDraft(context: Context) {

        view?.showLoading()

        dispo = Const.services.getAllDraft(getToken(context))
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) this.view?.showDraft(it.drafts)
                    else view?.dataKosong()
                },
                onError = {
                    if (checkError(it))
                        view?.error(context.getString(R.string.tidak_ada_koneksi))
                    else
                        view?.error(context.getString(R.string.terjadi_kesalahan))

                    view?.hideLoading()
                },
                onComplete = {
                    view?.hideLoading()
                }
            )
    }

    fun hapusDraft(context: Context, productId: Int) {
        view?.showLoading()

        dispo = Const.services.hapusDraft(getToken(context), productId)
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) this.view?.suksesHapusDraft()
                    else view?.error(context.getString(R.string.terjadi_kesalahan))
                },
                onError = {
                    if (checkError(it))
                        view?.error(context.getString(R.string.tidak_ada_koneksi))
                    else
                        view?.error(context.getString(R.string.terjadi_kesalahan))


                    view?.hideLoading()
                },
                onComplete = {
                    view?.hideLoading()
                }
            )
    }
}