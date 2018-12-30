package com.papyrus.bshairwebapp.Adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.news_item.view.*

class NewsHolder (itemView: View): RecyclerView.ViewHolder(itemView){

    val news_title = itemView.title_news
    val news_cat = itemView.cat_news
    val news_image = itemView.image_news
    val news_card_view = itemView.mainCardView

}