package com.kartinimedia.albumcantik.presenter

import android.content.Context
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.contract.BasePresenter
import com.kartinimedia.albumcantik.view.UploadView
import com.kartinimedia.albumcantik.utils.Const
import com.kartinimedia.albumcantik.utils.getToken
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

class UploadPresenter: BasePresenter<UploadView> {

    private var view: UploadView? = null
    private var dispo: Disposable? = null

    override fun onAttach(view: UploadView) {
        this.view = view
    }

    override fun onDetach() {
        this.view = null
    }

    fun dispose() {
        dispo?.dispose()
    }

    fun getSliderProduct(context: Context, productId: Int) {
        this.view?.showLoadingSlider()
        dispo = Const.services.getProductById(productId)
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) this.view?.showData(it.data[0])
                    else this.view?.error(context.getString(R.string.terjadi_kesalahan), Const.slider)

                    this.view?.hideLoadingSlider()
                },
                onError = {
                    if (it is HttpException)
                        this.view?.error(context.getString(R.string.terjadi_kesalahan), Const.slider)
                    else if (it is UnknownHostException || it is TimeoutException || it is SocketTimeoutException || it is ConnectException)
                        this.view?.error(context.getString(R.string.tidak_ada_koneksi), Const.slider)

                    this.view?.hideLoadingSlider()
                },
                onComplete = {
                    this.view?.hideLoadingSlider()
                }
            )
    }

    fun upload(
        context: Context,
        images: MultipartBody.Part,
        productId: Int,
        accessToken: String,
        number: Int,
        index: Int,
        size: Int
    ) {
        dispo = Const.services.uploadPhoto(images, accessToken, productId, number)
            .timeout(500, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) {
                        if (index.plus(1) < size)
                            view?.prosesUpload(index)
                        else
                            view?.suksesUploadSemua()
                    } else view?.error(context.getString(R.string.upload_gagal), Const.upload)
                },
                onError = {
                    if (it is HttpException)
                        view?.error(context.getString(R.string.upload_gagal), Const.upload)
                    else if (it is UnknownHostException || it is TimeoutException || it is SocketTimeoutException || it is ConnectException)
                        view?.error(context.getString(R.string.upload_gagal), Const.upload)
                },
                onComplete = {
                    //
                }
            )
    }

    fun getDraftByProductId(context: Context, productId: Int) {
        dispo = Const.services.getDraftByProductId(getToken(context), productId)
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) this.view?.showDraft(it.files)
                    else view?.error(context.getString(R.string.terjadi_kesalahan), Const.draft)
                },
                onError = {
                    if (it is UnknownHostException || it is TimeoutException
                        || it is SocketTimeoutException || it is ConnectException || it is HttpException)
                        this.view?.error(context.getString(R.string.tidak_ada_koneksi), Const.draft)
                },
                onComplete = {
                    //
                }
            )
    }
}