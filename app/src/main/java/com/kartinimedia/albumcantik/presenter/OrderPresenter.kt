package com.kartinimedia.albumcantik.presenter

import android.content.Context
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.contract.BasePresenter
import com.kartinimedia.albumcantik.utils.Const
import com.kartinimedia.albumcantik.utils.getToken
import com.kartinimedia.albumcantik.view.OrderView
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

class OrderPresenter: BasePresenter<OrderView> {

    private var view: OrderView? = null
    private var dispo: Disposable? = null

    override fun onAttach(view: OrderView) {
        this.view = view
    }

    override fun onDetach() {
        view = null
    }

    fun dispose() {
        dispo?.dispose()
    }

    fun detailAlamat(context: Context, alamatId: Int) {
        view?.showLoading()
        dispo = Const.services.detailAlamat(accessToken = getToken(context), alamatSearch = alamatId)
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) view?.showAlamat(it.data[0])
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

    fun getPayment(context: Context) {
        view?.showLoading()
        dispo = Const.services.getPayment(0)
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) view?.showPayment(it.dataPayment)
                    else view?.error(context.getString(R.string.terjadi_kesalahan))

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

    fun addOrder(
        context: Context,
        jumlahOrder: Int,
        paymentId: Int,
        orderName: String,
        orderEmail: String,
        orderPhone: String,
        orderAddress: String,
        provId: Int,
        kabId: Int,
        kecId: Int,
        productId: Int,
        kodePos: Int,
        orderNote: String,
        kodeUnik: Int
    ) {
        view?.showLoading()
        dispo = Const.services.addOrder(
            accessToken = getToken(context),
            jumlahOrder = jumlahOrder,
            paymentId = paymentId,
            orderName = orderName,
            orderEmail = orderEmail,
            orderPhone = orderPhone,
            orderAddress = orderAddress,
            provId = provId,
            kabId = kabId,
            kecId = kecId,
            productId = productId,
            kodePos = kodePos,
            orderNote = orderNote,
            kodeUnik = kodeUnik
        )
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) view?.suksesOrder(it.dataOrder)
                    else view?.error(context.getString(R.string.terjadi_kesalahan))

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

    fun deleteOrder(context: Context, orderId: Int) {
        view?.showLoading()
        dispo = Const.services.deleteOrder(getToken(context), orderId)
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) view?.suksesHapusOrder()
                    else view?.error(context.getString(R.string.terjadi_kesalahan))

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