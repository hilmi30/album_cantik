package com.kartinimedia.albumcantik.presenter

import android.content.Context
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.view.AccountView
import com.kartinimedia.albumcantik.contract.BasePresenter
import com.kartinimedia.albumcantik.utils.Const
import com.kartinimedia.albumcantik.utils.checkError
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

class AccountPresenter: BasePresenter<AccountView> {

    private var view: AccountView? = null
    private var dispo: Disposable? = null

    override fun onAttach(view: AccountView) {
        this.view = view
    }

    override fun onDetach() {
        this.view = null
    }

    fun dispo() {
        dispo?.dispose()
    }

    fun updateProfile(context: Context, fullName: String, genderId: Int, passBaru: String?, passLama: String?, passRepeat: String?, alamatId: Int?) {
        this.view?.showLoading()

        dispo = Const.services.updateProfile(
            accessToken = getToken(context),
            fullName = fullName,
            genderId = genderId,
            passwordBaru = passBaru,
            passwordLama = passLama,
            passwordRepeat = passRepeat,
            alamatId = alamatId
        )
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) this.view?.suksesUpdate(it)
                    else this.view?.error(it.message)

                    this.view?.hideLoading()
                },
                onError = {
                    if (checkError(it))
                        view?.error(context.getString(R.string.tidak_ada_koneksi))
                    else
                        view?.error(context.getString(R.string.terjadi_kesalahan))

                    this.view?.hideLoading()
                },
                onComplete = {
                    this.view?.hideLoading()
                }
            )
    }
}