package com.papyrus.fanoos.bshairwebapp.Api

import com.papyrus.fanoos.bshairwebapp.Models.News
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("get_posts")
    fun getNews(@Query("page") page: Int): io.reactivex.Observable<News>
}