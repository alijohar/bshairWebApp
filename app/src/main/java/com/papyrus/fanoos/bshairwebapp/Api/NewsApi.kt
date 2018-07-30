package com.papyrus.fanoos.bshairwebapp.Api

import com.papyrus.fanoos.bshairwebapp.Models.News
import com.papyrus.fanoos.bshairwebapp.Models.NewsComments
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

//    For last 10 posts
    @GET("get_posts")
    fun getNews(@Query("page") page: Int): io.reactivex.Observable<News>


//    For last 10 Banner Posts
    @GET("get_tag_posts")
    fun getBannerPosts(@Query("tag_slug") bannerTage:String,
                       @Query("page") pageBanner:Int):io.reactivex.Observable<News>


//    For get post comments
    @GET("get_post")
    fun getPostComments(@Query("id") id:Int):io.reactivex.Observable<NewsComments>
}