package com.papyrus.bshairwebapp.Adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.papyrus.bshairwebapp.Models.News
import com.papyrus.bshairwebapp.Ui.NewsDetail
import com.papyrus.bshairwebapp.bshairwebapp.R
import kotlinx.android.synthetic.main.slide_image_item.view.*
import kotlinx.android.synthetic.main.slide_image_item_advertising.view.*

class BannerAdapterAdvertising(val context: Context, val localBannerArray: News): PagerAdapter() {
        var layoutInflater: LayoutInflater? = null

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object` as FrameLayout
        }

        override fun getCount(): Int {
            return localBannerArray.posts.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val itemView = layoutInflater!!.inflate(R.layout.slide_image_item_advertising, container, false)
            val banner: ImageView = itemView.banner_image_advertising
            val imageBanner:String = localBannerArray.posts[position].thumbnail_images.full.url
            val customLink:String = localBannerArray.posts[position].custom_fields.websiteAdvertising[0]
            Glide.with(context).load(imageBanner).into(banner)



            container.addView(itemView)

            //listening to image click
            banner.setOnClickListener(View.OnClickListener {
                try {
                    val openURL = Intent(android.content.Intent.ACTION_VIEW)
                    openURL.data = Uri.parse(customLink)
                    context.startActivity(openURL)
                }catch (ex:Exception){

                Toast.makeText(context, "للأسف لا يمكن فتح رابط الإعلان", Toast.LENGTH_LONG).show()
                }
            })

            return itemView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as FrameLayout)
        }

    }
