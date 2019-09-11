package com.kartinimedia.albumcantik.utils

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.kartinimedia.albumcantik.BuildConfig
import com.kartinimedia.albumcantik.network.ApiRepo
import com.kartinimedia.albumcantik.network.RetrofitBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class GetDataServices: Service() {

    private val services = RetrofitBuilder.getInstance(BuildConfig.BASE_URL_API).create(ApiRepo::class.java)
    private var dispo: Disposable? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        getGender()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        dispo?.dispose()
    }

    private fun getGender() {
        dispo = services.getGender()
            .debounce(Const.timeOutDebounce.toLong(), TimeUnit.MILLISECONDS)
            .timeout(Const.timeOut.toLong(), TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isSuccess == 200) {
                        getSharedPreferences(Const.genderData, Context.MODE_PRIVATE).edit().apply {
                            putInt(Const.genderId1, it.data[0].id)
                            putInt(Const.genderId2, it.data[1].id)
                            putString(Const.genderName1, it.data[0].gender)
                            putString(Const.genderName2, it.data[1].gender)
                            apply()
                        }
                    }

                    stopSelf()
                },
                onError = {
                    stopSelf()

                },
                onComplete = {
                    stopSelf()
                }
            )
    }
}