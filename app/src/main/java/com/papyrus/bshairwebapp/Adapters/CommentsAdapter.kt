package com.papyrus.bshairwebapp.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import com.papyrus.bshairwebapp.Models.Comment
import com.papyrus.bshairwebapp.bshairwebapp.R

class CommentsAdapter(internal val context:Context, internal var commentsList:List<Comment>): RecyclerView.Adapter<CommentsHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return CommentsHolder(itemView)
    }

    override fun getItemCount(): Int {
        return commentsList.size
    }

    override fun onBindViewHolder(holder: CommentsHolder, position: Int) {
        holder.des_comment.text = Html.fromHtml(Html.fromHtml(commentsList[position].content).toString())
        holder.author_comment.text = commentsList[position].name
        holder.date_comment.text = commentsList[position].date

    }

}