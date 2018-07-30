package com.papyrus.fanoos.bshairwebapp.Adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.comment_item.view.*

class CommentsHolder (itemView: View): RecyclerView.ViewHolder(itemView){
    val des_comment = itemView.comment_content
    val author_comment = itemView.comment_author_name
    val date_comment = itemView.comment_time_posting
}