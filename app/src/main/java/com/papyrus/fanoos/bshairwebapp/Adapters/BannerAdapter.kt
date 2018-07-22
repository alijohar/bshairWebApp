package com.papyrus.fanoos.bshairwebapp.Adapters

import android.content.Context
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import android.view.ViewGroup
import com.papyrus.fanoos.bshairwebapp.R
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.papyrus.fanoos.bshairwebapp.Models.News
import com.papyrus.fanoos.bshairwebapp.Models.Post
import com.papyrus.fanoos.bshairwebapp.NewsDetail
import com.papyrus.fanoos.bshairwebapp.R.id.banner_image
import kotlinx.android.synthetic.main.slide_image_item.view.*


class BannerAdapter(val context:Context, val localBannerArray:News): PagerAdapter() {
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
        val imageBanner:String = localBannerArray.posts[position].thumbnail_images.medium_large.url
        val bannerContent = localBannerArray.posts[position].content
        val urlPost:String = localBannerArray.posts[position].url

        itemView.banner_title.text = bannerTitle
        Glide.with(context).load(imageBanner).into(banner)



        container.addView(itemView)

        //listening to image click
        banner.setOnClickListener(View.OnClickListener {
            val bannerIntent = Intent(context, NewsDetail::class.java)
            bannerIntent.putExtra("title", bannerTitle)
            bannerIntent.putExtra("content", bannerContent)
            bannerIntent.putExtra("full_image", imageBanner)
            bannerIntent.putExtra("post_url", urlPost)

            context.startActivity(bannerIntent)

             })

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as FrameLayout)
    }

}