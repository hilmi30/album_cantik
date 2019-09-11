package com.kartinimedia.albumcantik.presenter

import android.content.Context
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.contract.BasePresenter
import com.kartinimedia.albumcantik.view.LoginView
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

class LoginPresenter: BasePresenter<LoginView> {

    private var view: LoginView? = null
    private var dispo: Disposable? = null

    override fun onAttach(view: LoginView) {
        this.view = view
    }

    override fun onDetach() {
        this.view = null
    }

    fun dispo() {
        dispo?.dispose()
    }

    fun login(username: String, password: String, context: Context) {
        this.view?.showLoading()

        dispo = Const.services.login(
            username = username,
            password = password
        )
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) this.view?.suksesLogin(it)
                    else this.view?.error(context.getString(R.string.terjadi_kesalahan))

                    this.view?.hideLoading()
                },
                onError = {
                    it.printStackTrace()
                    if (it is HttpException)
                        this.view?.error(context.getString(R.string.user_login_tidak_benar))
                    else if (it is UnknownHostException || it is TimeoutException || it is SocketTimeoutException || it is ConnectException)
                        this.view?.error(context.getString(R.string.tidak_ada_koneksi))

                    this.view?.hideLoading()
                },
                onComplete = {
                    this.view?.hideLoading()
                }
            )
    }

    fun resendEmail(email: String, context: Context) {
        this.view?.showLoading()

        dispo = Const.services.resendEmail(
            email = email
        )
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) this.view?.suksesResend()
                    else this.view?.error(context.getString(R.string.terjadi_kesalahan))

                    this.view?.hideLoading()
                },
                onError = {
                    if (it is HttpException)
                        this.view?.error(context.getString(R.string.email_tidak_terdaftar))
                    else if (it is UnknownHostException || it is TimeoutException || it is SocketTimeoutException || it is ConnectException)
                        this.view?.error(context.getString(R.string.tidak_ada_koneksi))

                    this.view?.hideLoading()
                },
                onComplete = {
                    this.view?.hideLoading()
                }
            )
    }

    fun lupaPass(context: Context, email: String) {
        this.view?.showLoading()

        dispo = Const.services.lupaPass(
            email = email
        )
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) this.view?.suksesReqLupaPass()
                    else this.view?.error(context.getString(R.string.terjadi_kesalahan))

                    this.view?.hideLoading()
                },
                onError = {
                    if (it is HttpException)
                        this.view?.error(context.getString(R.string.email_tidak_terdaftar))
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