package com.papyrus.bshairwebapp.Api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NewsClinet {
    private var ourInstance: Retrofit? = null
    val instance: Retrofit
        get() {
            if (NewsClinet.ourInstance == null){
                NewsClinet.ourInstance = Retrofit.Builder().baseUrl("http://bshaer.net/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
            }

            return NewsClinet.ourInstance!!

        }
}