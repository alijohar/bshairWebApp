package com.papyrus.fanoos.bshairwebapp.Api

import com.papyrus.fanoos.bshairwebapp.Models.CatList
import com.papyrus.fanoos.bshairwebapp.Models.News
import com.papyrus.fanoos.bshairwebapp.Models.NewsComments
import com.papyrus.fanoos.bshairwebapp.Models.PostFromCatIndex
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


//    For get all cats list
    @GET("get_category_index")
    fun getCatList():io.reactivex.Observable<CatList>


//    For get all posts in special cat
    @GET("get_category_posts")
    fun getPostFromCat(@Query("category_id") catId:Int,
                       @Query("page") pageNumber:Int):io.reactivex.Observable<PostFromCatIndex>


//    For get all posts when searching
    @GET("get_posts")
    fun getPostFromSearching(@Query("s") searchingItem:String,
                             @Query("page") pageNumber: Int):io.reactivex.Observable<News>

}