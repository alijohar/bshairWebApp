package com.papyrus.fanoos.bshairwebapp.Adapters

import android.content.Context
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
import com.papyrus.fanoos.bshairwebapp.R.id.banner_image
import kotlinx.android.synthetic.main.slide_image_item.view.*


class BannerAdapter(val context:Context, val localBannerArray:ArrayList<String>): PagerAdapter() {
    var layoutInflater: LayoutInflater? = null

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as FrameLayout
    }

    override fun getCount(): Int {
        return localBannerArray.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = layoutInflater!!.inflate(R.layout.slide_image_item, container, false)
        val Banner:ImageView = itemView.banner_image
        val imageBanner:String = localBannerArray[position]
        Glide.with(context).load(imageBanner).into(Banner)


        container.addView(itemView)

        //listening to image click
        Banner.setOnClickListener(View.OnClickListener {
            Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show() })

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as FrameLayout)
    }

}