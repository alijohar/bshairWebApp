package com.papyrus.fanoos.bshairwebapp.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.papyrus.fanoos.bshairwebapp.R

class CommentsAdapter(internal val context:Context, internal var commentsList:ArrayList<String>): RecyclerView.Adapter<CommentsHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return CommentsHolder(itemView)
    }

    override fun getItemCount(): Int {
        return commentsList.size
    }

    override fun onBindViewHolder(holder: CommentsHolder, position: Int) {
        holder.des_comment.text = commentsList[position]
    }

}