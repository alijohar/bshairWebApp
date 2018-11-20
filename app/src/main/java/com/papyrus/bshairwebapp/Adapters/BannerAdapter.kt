package com.papyrus.bshairwebapp.Adapters

import android.content.Context
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.papyrus.bshairwebapp.bshairwebapp.R
import android.view.LayoutInflater
import android.widget.*
import com.bumptech.glide.Glide
import com.papyrus.bshairwebapp.Models.News
import com.papyrus.bshairwebapp.Ui.NewsDetail
import kotlinx.android.synthetic.main.slide_image_item.view.*

class BannerAdapter(val context:Context, val localBannerArray:News): PagerAdapter() {
    var itsArticle: Boolean = false

    var layoutInflater: LayoutInflater? = null

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as FrameLayout
    }

    override fun getCount(): Int {
        return localBannerArray.posts.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = layoutInflater!!.inflate(R.layout.slide_image_item, container, false)
        val bannerTitle:String = localBannerArray.posts[position].title
        val banner:ImageView = itemView.banner_image
        val tags = localBannerArray.posts[position].tags
        val imageBanner:String = localBannerArray.posts[position].thumbnail_images.medium_large.url
        val bannerContent = localBannerArray.posts[position].content
        val urlPost:String = localBannerArray.posts[position].url
        val numberComment = localBannerArray.posts[position].comment_count
//        TODO: must get all cats if the customer needs it
        val catName = localBannerArray.posts[position].categories[0].title
        val authorName = localBannerArray.posts[position].author.nickname
        val timePosting = localBannerArray.posts[position].date
        itemView.banner_title.text = bannerTitle
        Glide.with(context).load(imageBanner).into(banner)



        container.addView(itemView)

        //listening to image click
        banner.setOnClickListener {
            itsArticle = false
            val bannerIntent = Intent(context, NewsDetail::class.java)
            for (item in tags) {
                if (item.title == "مقال") {
                    itsArticle = true
                    val authorArcticle = localBannerArray.posts[position].custom_fields.authorName[0]
                    bannerIntent.putExtra("authorArcticle", authorArcticle)
                }

            }
            bannerIntent.putExtra("itsArticle", itsArticle)
            bannerIntent.putExtra("title", bannerTitle)
            bannerIntent.putExtra("content", bannerContent)
            bannerIntent.putExtra("full_image", imageBanner)
            bannerIntent.putExtra("post_url", urlPost)
            bannerIntent.putExtra("number_comment", numberComment)
            bannerIntent.putExtra("cat_name", catName)
            bannerIntent.putExtra("author_name", authorName)
            bannerIntent.putExtra("time_post", timePosting)

            context.startActivity(bannerIntent)

             }

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as FrameLayout)
    }

}