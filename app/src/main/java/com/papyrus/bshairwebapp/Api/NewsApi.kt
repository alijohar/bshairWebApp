package com.papyrus.bshairwebapp.Api

import com.papyrus.bshairwebapp.Models.*
import retrofit2.http.*
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

//    For submit comment
    @FormUrlEncoded
    @POST("submit_comment")
    fun submitComment(@Field("post_id") post_id:Int,
                      @Field("name") commenterName:String,
                      @Field("email") commenterMail:String,
                      @Field("content") commentContent:String):io.reactivex.Observable<commentStatus>

}