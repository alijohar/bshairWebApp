package com.papyrus.bshairwebapp.Ui

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.analytics.FirebaseAnalytics
import com.papyrus.bshairwebapp.Api.NewsApi
import com.papyrus.bshairwebapp.Api.NewsClinet
import com.papyrus.bshairwebapp.Models.Post
import com.papyrus.bshairwebapp.Models.singlePost
import com.papyrus.bshairwebapp.Preferences.Preferences
import com.papyrus.bshairwebapp.bshairwebapp.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_news_detail.*
import kotlinx.android.synthetic.main.app_bar_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import kotlinx.android.synthetic.main.no_internet.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class SingleNewsFromPush : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    var compositeDisposable = CompositeDisposable()
    internal lateinit var myNewsApi: NewsApi
    val objectMainActivity = MainActivity()
    var newCommentDetail = CommentDetail()
    var locatDetailId:Int = 0
    var urlPostWithTitle = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val myNewsClinet = NewsClinet.instance
        myNewsApi = myNewsClinet.create(NewsApi::class.java)

        val bundel = intent.extras
        val newId = bundel.getString("customkey")

        setSupportActionBar(toolbar_detail)
        fetchData(newId)


    }

    override fun onSupportNavigateUp(): Boolean {
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        onBackPressed()
        return true
    }


    private fun fetchData(id: String) {
        val intId = id.toInt()
        try {
            compositeDisposable.add(myNewsApi.getSinglePost(intId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe ({ newsSingleData -> displaySingleData(newsSingleData) }, { error -> displayError(error, this)}))
        }catch (e:Exception){
            objectMainActivity.showToastNotInternet(this)
        }
    }

    private fun displayError(error: Throwable?, context: Context) {
        showGifNotInternet(context)

    }

    private fun displaySingleData(newsSingleData: singlePost?) {
        val title = newsSingleData!!.post.title
        val content = newsSingleData.post.content
        val fullImage = newsSingleData.post.thumbnail_images.medium_large.url
        val commentNumber:Int = newsSingleData.post.comment_count
        val catName = newsSingleData.post.categories[0].title
        val authorName = newsSingleData.post.author.name
        val time = newsSingleData.post.date
        val postIdNew = newsSingleData.post.id
        locatDetailId = postIdNew
        var myNewPreferences = Preferences(this)
        val newFontNameByUser = myNewPreferences.getFontName()
        val newFontSizeByUser = myNewPreferences.getFontSize()
        val urlPost = newsSingleData.post.url
        val link:String = "http://bshaer.net/?p=$postIdNew"
        urlPostWithTitle = title + "\n" + link

        var text = "<head><style type=\"text/css\">\n" +
                "@font-face {\n" +
                " font-family: 'MyCustomFont';\n" +
                " src: url('$newFontNameByUser') \n" +
                "}\n" +
                "\n" +
                "\n" +
                "body{\n" +
                "  font-size: $newFontSizeByUser;\n" +
                "  font-family:  'MyCustomFont';\n" +
                "  text-align: justify;\n" +
                "  direction: rtl;\n" +
                "  line-height: 2.5;\n" +
                "}\n" +
                ".link{\n" +
                "    display: inline-block;\n" +
                "    background-color: #7d5abd;\n" +
                "    color: #FFFFFF;\n" +
                "    padding: 6px 25px;\n" +
                "    text-align: center;\n" +
                "    text-decoration: none;\n" +
                "    font-size: 16px;\n" +
                "    opacity: 0.9;" +
                "}\n" +
                "\n" +
                "img{\n" +
                "  height: auto;\n" +
                "  width: 100%;\n" +
                "  display: block;\n" +
                "  margin-left: auto;\n" +
                "  margin-right: auto;\n" +
                "}\n" +
                ".center{\n" +
                "    text-align: center;\n" +
                "}\n" +
                "\n" +
                "h1, h2, h3, h4, h5, h5 {\n" +
                "  color: red;\n" +
                "  text-align: center;\n" +
                "}</style>\n\n</head><html><body dir=\"rtl\"; style=\"text-align:justify;\">";
        text += content
        text += "<p class =\"center\"><a href=\"$link\" class=\"link\">المطالعة في الموقع</a></p>"
        text += "</body></html>"

        des_news_detail.loadDataWithBaseURL("file:///android_asset/",text,"text/html","utf-8",null)


        title_news_detail.text = title

        Glide.with(this)
                .load(fullImage)
                .listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        imag_progress_bar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        imag_progress_bar.visibility = View.GONE
                        return false
                    }


                })
                .into(image_news_detial)
        detail_author_name.text = authorName
        detail_time_posting.text = time
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

        //        For Analytics Firebase

        val params = Bundle()
        params.putString("العنوان", title)
        firebaseAnalytics.logEvent("PostViewAndroid", params)

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
                newCommentDetail.addComment(this, locatDetailId)
                return true


            }
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
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

    //    ForCustomFont
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
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

}


