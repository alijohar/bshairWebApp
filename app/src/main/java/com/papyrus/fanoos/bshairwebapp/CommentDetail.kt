package com.papyrus.fanoos.bshairwebapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.papyrus.fanoos.bshairwebapp.Adapters.CommentsAdapter
import kotlinx.android.synthetic.main.activity_comment_detail.*

class CommentDetail : AppCompatActivity() {
    internal lateinit var myCommentAdapter:CommentsAdapter
    var commentsArray = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_detail)

        recycler_comments.layoutManager = LinearLayoutManager(this)
        addOnArray()

        myCommentAdapter = CommentsAdapter(this, commentsArray)
        recycler_comments.adapter = myCommentAdapter
    }

    private fun addOnArray() {
        commentsArray.add("test1")
        commentsArray.add("test2")
        commentsArray.add("test3")
        commentsArray.add("test4")
        commentsArray.add("test5")
        commentsArray.add("test6")
        commentsArray.add("test7")
        commentsArray.add("test8")
    }

}
