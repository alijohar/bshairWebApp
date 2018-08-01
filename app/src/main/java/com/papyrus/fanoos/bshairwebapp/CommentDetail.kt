package com.papyrus.fanoos.bshairwebapp

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.papyrus.fanoos.bshairwebapp.Adapters.CommentsAdapter
import com.papyrus.fanoos.bshairwebapp.Api.NewsApi
import com.papyrus.fanoos.bshairwebapp.Api.NewsClinet
import com.papyrus.fanoos.bshairwebapp.Models.NewsComments
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_comment_detail.*
import kotlinx.android.synthetic.main.app_bar_detail.*
import kotlinx.android.synthetic.main.dialog_add_comment.view.*
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class CommentDetail : AppCompatActivity() {
    internal lateinit var myCommentAdapter:CommentsAdapter
    internal var compositeDisposable = CompositeDisposable()
    internal lateinit var myNewsApi: NewsApi
    var id:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_detail)
        val toolbar = findViewById<Toolbar>(R.id.toolbar_detail)
        setSupportActionBar(toolbar)

        //Back arrow icon
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        //        CustomFont
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("droidkufi_regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
        val bundel = intent.extras
        val newId = bundel.getInt("post_id")
        val newTitle = bundel.getString("post_title")

        title_comment_detail.text = newTitle

        id = newId

        recycler_comments.layoutManager = LinearLayoutManager(this)

//      intit Api
        val myNewsClinet = NewsClinet.instance
        myNewsApi = myNewsClinet.create(NewsApi::class.java)

        //        Show Data
        fetchData(id)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.comment_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {

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

    private fun fetchData(id: Int) {
        compositeDisposable.add(myNewsApi.getPostComments(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{ newsCommentData -> displayCommentData(newsCommentData)})


    }

    private fun displayCommentData(newsCommentData: NewsComments?) {
        comment_progressbar.visibility = View.GONE
        myCommentAdapter = CommentsAdapter(this, newsCommentData!!.post.comments)
        recycler_comments.adapter = myCommentAdapter
    }
    //    ForCustomFont
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

}