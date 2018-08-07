package com.papyrus.fanoos.bshairwebapp

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.papyrus.fanoos.bshairwebapp.Adapters.CommentsAdapter
import com.papyrus.fanoos.bshairwebapp.Api.NewsApi
import com.papyrus.fanoos.bshairwebapp.Api.NewsClinet
import com.papyrus.fanoos.bshairwebapp.Models.NewsComments
import com.papyrus.fanoos.bshairwebapp.Models.commentStatus
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_comment_detail.*
import kotlinx.android.synthetic.main.app_bar_detail.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.dialog_add_comment.view.*
import kotlinx.android.synthetic.main.no_internet.*
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.security.AccessController.getContext

class CommentDetail : AppCompatActivity() {
    internal lateinit var myCommentAdapter: CommentsAdapter
    internal var compositeDisposable = CompositeDisposable()
    var id: Int = 0
    //      intit Api
    val myNewsClinet = NewsClinet.instance
    var myNewsApi = myNewsClinet.create(NewsApi::class.java)
    val checkConnection = MainActivity()
    lateinit var commentDialog:AlertDialog.Builder
    lateinit var view:View
    lateinit var dialog:AlertDialog





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_detail)

        if (!checkConnection.internet_connection(this)) {
            checkConnection.showToastNotInternet(this)

        } else {
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


            //        Show Data
            fetchData(id)

        }
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
                addComment(this, id)

                return true


            }
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun addComment(context:Context, localId: Int) {
        commentDialog = AlertDialog.Builder(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = inflater.inflate(R.layout.dialog_add_comment, null)
        dialog = commentDialog.setView(view).create()

        val submit = view.send_dialog_send_comment
        val cancel = view.cancel_dialog_send_comment
        val name = view.name_dialog_send_comment
        val mail = view.email_dialog_send_comment
        val desComment = view.des_dialog_send_comment

        dialog.show()
        dialog.setCancelable(false)


        submit.setOnClickListener {
            if (!name.text.isEmpty() && !mail.text.isEmpty() && !desComment.text.isEmpty()) {
                sendData(localId, name.text.toString(), mail.text.toString(), desComment.text.toString(), context)
            } else {
                if (name.text.isEmpty()) name.error = getString(R.string.not_be_empty)
                if (mail.text.isEmpty()) mail.error = getString(R.string.not_be_empty)
                if (desComment.text.isEmpty()) desComment.error = getString(R.string.not_be_empty)
            }
        }





        cancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun sendData(postId:Int, name:String, email:String, content:String, newContext: Context) {
        try {
            compositeDisposable.add(myNewsApi.submitComment(postId, name, email, content).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{ statusOfComment -> displayCommentStatus(statusOfComment, newContext)})

        }catch (e:Exception){
            Toast.makeText(newContext, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun displayCommentStatus(statusOfComment: commentStatus?, context: Context) {

        if (statusOfComment!!.status == "pending" || statusOfComment.status == "ok"){
            Toast.makeText(context, R.string.comment_sent_to_admin, Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }else{
            Toast.makeText(context, R.string.cannot_send_comment, Toast.LENGTH_LONG).show()
        }

    }

    private fun fetchData(id: Int) {
        try {
            compositeDisposable.add(myNewsApi.getPostComments(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { newsCommentData -> displayCommentData(newsCommentData) })
        }catch (e:Exception){
            checkConnection.showToastNotInternet(this)
        }


    }

    private fun displayCommentData(newsCommentData: NewsComments?) {
        comment_progressbar.visibility = View.GONE
        if (newsCommentData!!.post.comments.isEmpty()){
            no_item_text_view.text = getText(R.string.no_comment)
            no_item_text_view.visibility = View.VISIBLE
        }else {
            myCommentAdapter = CommentsAdapter(this, newsCommentData!!.post.comments)
            recycler_comments.adapter = myCommentAdapter
        }

    }

    //    ForCustomFont
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }



}
