package com.kartinimedia.albumcantik.presenter

import android.content.Context
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.contract.BasePresenter
import com.kartinimedia.albumcantik.utils.Const
import com.kartinimedia.albumcantik.utils.checkError
import com.kartinimedia.albumcantik.utils.getToken
import com.kartinimedia.albumcantik.view.FormAlamatView
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

class FormAlamatPresenter: BasePresenter<FormAlamatView> {

    private var view: FormAlamatView? = null
    private var dispo: Disposable? = null

    override fun onAttach(view: FormAlamatView) {
        this.view = view
    }

    override fun onDetach() {
        view = null
    }

    fun dispose() {
        dispo?.dispose()
    }

    fun getProvinsi(context: Context) {
        dispo = Const.services.getProvinsi()
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) this.view?.showProvinsi(it.data)
                    else this.view?.error(context.getString(R.string.terjadi_kesalahan))
                },
                onError = {
                    //
                },
                onComplete = {
                    //
                }
            )
    }

    fun getKabupaten(context: Context, idProv: Int) {
        dispo = Const.services.getKabupaten(idProv)
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) this.view?.showKabupaten(it.data)
                    else this.view?.error(context.getString(R.string.terjadi_kesalahan))
                },
                onError = {
                    //
                },
                onComplete = {
                    //
                }
            )
    }

    fun getKecamatan(context: Context, idKab: Int) {
        dispo = Const.services.getKecamatan(idKab)
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) this.view?.showKecamatan(it.data)
                    else this.view?.error(context.getString(R.string.terjadi_kesalahan))
                },
                onError = {
                    //
                },
                onComplete = {
                    //
                }
            )
    }

    fun addAlamat(
        context: Context,
        token: String,
        alamat: String,
        atasNama: String,
        nomerHp: String,
        idProv: Int,
        idKab: Int,
        idKec: Int,
        kodePos: String
    ) {

        view?.showLoading()

        dispo = Const.services.addAlamat(
            accessToken = token,
            alamat = alamat,
            atasNama = atasNama,
            nomerHp = nomerHp,
            idProv = idProv,
            idKab = idKab,
            idKec = idKec,
            kodePos = kodePos
        )
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) this.view?.sukseSimpanAlamat(it.data)
                    else this.view?.error(context.getString(R.string.terjadi_kesalahan))

                    view?.hideLoading()
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

    fun updateAlamat(
        context: Context,
        token: String,
        alamat: String,
        atasNama: String,
        nomerHp: String,
        idProv: Int?,
        idKab: Int?,
        idKec: Int?,
        kodePos: String,
        alamatId: Int
    ) {

        view?.showLoading()

        dispo = Const.services.updateAlamat(
            accessToken = token,
            alamat = alamat,
            atasNama = atasNama,
            nomerHp = nomerHp,
            idProv = idProv,
            idKab = idKab,
            idKec = idKec,
            kodePos = kodePos,
            alamatId = alamatId
        )
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) this.view?.suksesUbahAlamat(it.data)
                    else this.view?.error(context.getString(R.string.terjadi_kesalahan))

                    view?.hideLoading()
                },
                onError = {
                    if (it is HttpException || it is SocketTimeoutException || it is ConnectException)
                        this.view?.error(context.getString(R.string.terjadi_kesalahan))
                    else if (it is UnknownHostException || it is TimeoutException)
                        this.view?.error(context.getString(R.string.tidak_ada_koneksi))

                    view?.hideLoading()
                },
                onComplete = {
                    view?.hideLoading()
                }
            )
    }

    fun deleteAlamat(
        context: Context,
        token: String,
        alamatId: Int
    ) {

        view?.showLoading()

        dispo = Const.services.deleteAlamat(
            accessToken = token,
            alamatId = alamatId
        )
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) this.view?.suksesHapusAlamat()
                    else this.view?.error(context.getString(R.string.terjadi_kesalahan))

                    view?.hideLoading()
                },
                onError = {
                    if (it is HttpException || it is SocketTimeoutException || it is ConnectException)
                        this.view?.error(context.getString(R.string.terjadi_kesalahan))
                    else if (it is UnknownHostException || it is TimeoutException)
                        this.view?.error(context.getString(R.string.tidak_ada_koneksi))

                    view?.hideLoading()
                },
                onComplete = {
                    view?.hideLoading()
                }
            )
    }

    fun updateProfile(context: Context, fullName: String?, genderId: Int?, passBaru: String?, passLama: String?, passRepeat: String?, alamatId: Int) {
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
                    if (it.isSuccess == 200) this.view?.suksesAlamatUtama()
                    else this.view?.error(it.message)

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