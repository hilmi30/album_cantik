package com.kartinimedia.albumcantik.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder {
    companion object {
        private lateinit var retrofit: Retrofit
        private lateinit var httpClient: OkHttpClient.Builder
        private lateinit var loggingInterceptor: HttpLoggingInterceptor

        fun getInstance(baseUrl: String): Retrofit {
            loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(loggingInterceptor)
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()

            return retrofit
        }
    }
}