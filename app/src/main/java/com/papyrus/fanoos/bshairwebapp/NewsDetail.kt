package com.papyrus.fanoos.bshairwebapp

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_news_detail.*
import kotlinx.android.synthetic.main.app_bar_detail.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_detail.*
import android.content.Intent
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.telecom.Call
import android.util.Log
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.dialog_add_comment.*
import kotlinx.android.synthetic.main.dialog_add_comment.view.*
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import android.widget.TextView




class NewsDetail : AppCompatActivity() {
    var urlPost: String? = null
    var urlPostWithTitle: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)



        news_detail_swipe.setOnTouchListener(object:OnSwipeTouchListener(this) {

            override fun onSwipeRight() {
                finish()
                this@NewsDetail.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right)            }
            override fun onSwipeLeft() {

            }

        })
        setSupportActionBar(toolbar_detail)

        //        CustomFont
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("droidkufi_bold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )

        val bundle = intent.extras
        val title = bundle.getString("title")
        val content = bundle.getString("content")
        val fullImage = bundle.getString("full_image")
        val commentNumber: Int = bundle.getInt("number_comment")
        val catName = bundle.getString("cat_name")
        val authorName = bundle.getString("author_name")
        val time = bundle.getString("time_post")
        val postIdNew = bundle.getInt("post_id")
        val timeS = time.split(" ")
        urlPost = bundle.getString("post_url")
        urlPostWithTitle = title + "\n" + urlPost

        des_news_detail.text = Html.fromHtml(Html.fromHtml(content).toString())
        title_news_detail.text = title
        Glide.with(this).load(fullImage).into(image_news_detial)

        detail_author_name.text = authorName
        detail_time_posting.text = timeS[0]
        detail_cat_name.text = catName
        detail_comment_number.text = commentNumber.toString()

//Back arrow icon
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        comment_display.setOnClickListener {
            var intent = Intent(this, CommentDetail::class.java)
                intent.putExtra("post_id", postIdNew)
                intent.putExtra("post_title", title)
            startActivity(intent)
        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_sharing -> {
                sharePostLink()
                return true
            }
            R.id.action_adding_comment -> {
                addComment()
                return true


            }
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun addComment() {
        val commentDialog = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_add_comment, null)

        val submit = view.send_dialog_send_comment
        val cancel = view.cancel_dialog_send_comment
        val name = view.name_dialog_send_comment
        val mail = view.email_dialog_send_comment
        val desComment = view.des_dialog_send_comment

        submit.setOnClickListener {
            if (!name.text.isEmpty() && !mail.text.isEmpty() && !desComment.text.isEmpty()){
                Toast.makeText(this, "message is sent", Toast.LENGTH_LONG).show()
            }else{
                if (name.text.isEmpty()) name.error = getString(R.string.not_be_empty)
                if (mail.text.isEmpty()) mail.error = getString(R.string.not_be_empty)
                if (desComment.text.isEmpty()) desComment.error = getString(R.string.not_be_empty)
            }
        }

        val dialog = commentDialog.setView(view).create()
        dialog.show()
        dialog.setCancelable(false)



        cancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun sharePostLink() {

        try {
            val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.share)
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, urlPostWithTitle!!)
            startActivity(Intent.createChooser(sharingIntent, resources.getString(R.string.share)))
        } catch (e: Exception) {
            Snackbar.make(news_detail, e.message.toString(), Snackbar.LENGTH_LONG).show()

        }
    }

    //    ForCustomFont
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }


}

