package com.kartinimedia.albumcantik.presenter

import android.content.Context
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.contract.BasePresenter
import com.kartinimedia.albumcantik.view.MainView
import com.kartinimedia.albumcantik.utils.Const
import com.kartinimedia.albumcantik.utils.getStatus
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

class MainPresenter: BasePresenter<MainView> {

    private var view: MainView? = null
    private var dispo: Disposable? = null

    override fun onAttach(view: MainView) {
        this.view = view
    }

    override fun onDetach() {
        this.view = null
    }

    fun dispose() {
        dispo?.dispose()
    }

    fun getAllCategory(context: Context, page: Int) {
        this.view?.showLoading()
        dispo = Const.services.getAllCategory(page)
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) {
                        view?.setTotalPage(it.totalPage)
                        if (page == 1) {
                            view?.showData(it.data)
                        } else {
                            view?.showCurrentData(it.data)
                        }
                    }
                    else this.view?.error(context.getString(R.string.terjadi_kesalahan))
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

    fun getAllDraft(context: Context) {
        if (getStatus(context)) {
            dispo = Const.services.getAllDraft(getToken(context))
                .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
                .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        if (it.isSuccess == 200) this.view?.showDraft(it.drafts)
                        else view?.draftKosong()
                    },
                    onError = {
                        view?.draftKosong()
                    },
                    onComplete = {
                        //
                    }
                )
        } else {
            view?.draftKosong()
        }
    }

    fun getPromo() {
        dispo = Const.services.getPromo()
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) this.view?.showPromo(it.data)
                },
                onError = {
                    //
                },
                onComplete = {
                    //
                }
            )
    }
}