package com.papyrus.bshairwebapp.Ui

import android.content.Context
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
import com.papyrus.bshairwebapp.Adapters.CommentsAdapter
import com.papyrus.bshairwebapp.Api.NewsApi
import com.papyrus.bshairwebapp.Api.NewsClinet
import com.papyrus.bshairwebapp.Models.NewsComments
import com.papyrus.bshairwebapp.Models.commentStatus
import com.papyrus.bshairwebapp.bshairwebapp.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_comment_detail.*
import kotlinx.android.synthetic.main.dialog_add_comment.view.*
import kotlinx.android.synthetic.main.no_internet.*
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.regex.Pattern

class CommentDetail : AppCompatActivity() {
    internal lateinit var myCommentAdapter: CommentsAdapter
    internal var compositeDisposable = CompositeDisposable()
    var id: Int = 0
    //      intit Api
    val myNewsClinet = NewsClinet.instance
    var myNewsApi = myNewsClinet.create(NewsApi::class.java)
    val objectMainActivity = MainActivity()
    lateinit var commentDialog:AlertDialog.Builder
    lateinit var view:View
    lateinit var dialog:AlertDialog
    var isShowErrorPageBefor:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_detail)

        if (!objectMainActivity.internet_connection(this)) {
            objectMainActivity.showToastNotInternet(this)

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


            val mLayoutManager = LinearLayoutManager(this)
//To reverse recyclerView 
            mLayoutManager.reverseLayout = true
            mLayoutManager.stackFromEnd = true

            recycler_comments.layoutManager = mLayoutManager


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
                if (isEmailValid(mail.text.toString())) {
                    sendData(localId, name.text.toString(), mail.text.toString(), desComment.text.toString(), context)
                }else  {
                    mail.error = context.getString(R.string.mail_not_valid)
                }

            } else {
                if (name.text.isEmpty()) name.error = context.getString(R.string.not_be_empty)
                if (mail.text.isEmpty()) mail.error = context.getString(R.string.not_be_empty)
                if (desComment.text.isEmpty()) desComment.error = context.getString(R.string.not_be_empty)
            }
        }





        cancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun sendData(postId:Int, name:String, email:String, content:String, newContext: Context) {
        try {
            compositeDisposable.add(myNewsApi.submitComment(postId, name, email, content).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe ({ statusOfComment -> displayCommentStatus(statusOfComment, newContext)}, {error -> displayErrorToast(error, this)}))

        }catch (e:Exception){
            Toast.makeText(newContext, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun displayErrorToast(error: Throwable?, context: Context) {
        dialog.dismiss()
        Toast.makeText(context, getText(R.string.commenet_error_send), Toast.LENGTH_LONG).show()
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
            compositeDisposable.add(myNewsApi.getPostComments(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe ({ newsCommentData -> displayCommentData(newsCommentData) }, { error -> displayError(error, this)}))
        }catch (e:Exception){
            objectMainActivity.showToastNotInternet(this)
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

    fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }


    //    ForCustomFont
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    fun displayError(error: Throwable?, context: Context) {
        if (!isShowErrorPageBefor){
            isShowErrorPageBefor = true
            showGifNotInternet(context)
        }else{
            Log.i("ErrorConnecting", "this log is show because error page already showed now + $error")
        }
    }

    fun showGifNotInternet(context: Context) {
        setContentView(R.layout.no_internet)
        val noInternetImage = findViewById<ImageView>(R.id.no_internet)
        Glide.with(context).load(R.drawable.tenor).into(noInternetImage)
        try_again_to_restart_activity.setOnClickListener {
            var intent = intent
            finish()
            startActivity(intent)
        }
    }



}
