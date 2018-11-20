package com.papyrus.bshairwebapp.Ui

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_news_detail.*
import kotlinx.android.synthetic.main.app_bar_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.app.AlertDialog
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import android.os.Build
import android.text.Spanned
import android.widget.TextView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.papyrus.bshairwebapp.Util.OnSwipeTouchListener
import com.papyrus.bshairwebapp.Preferences.Preferences
import com.papyrus.bshairwebapp.bshairwebapp.R


class NewsDetail : AppCompatActivity() {
    var urlPost: String? = null
    var urlPostWithTitle: String? = null
    lateinit var commentDialog:AlertDialog.Builder
    lateinit var view:View
    lateinit var dialog:AlertDialog
    var newCommentDetail = CommentDetail()
    var locatDetailId:Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        var itsArticle = bundle.getBoolean("itsArticle")

        if (itsArticle) {
            val authorArticle = bundle.getString("authorArcticle")
            setContentView(R.layout.activity_news_detail_article)
            val newTextView = findViewById<TextView>(R.id.author_name_article)
            newTextView.text = authorArticle
        }else{
            setContentView(R.layout.activity_news_detail)
        }




        val isRightToLeft = resources.getBoolean(R.bool.is_rtl)

        if (isRightToLeft) {
            des_news_detail.setOnTouchListener(object : OnSwipeTouchListener(this) {


                override fun onSwipeLeft() {
                    finish()
                    this@NewsDetail.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }



            })
        } else {
            des_news_detail.setOnTouchListener(object : OnSwipeTouchListener(this) {


                override fun onSwipeRight() {
                    finish()
                    this@NewsDetail.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                }

            })
        }

        setSupportActionBar(toolbar_detail)

        //        CustomFont
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("droidkufi_regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )

        val title = bundle.getString("title")
        val content = bundle.getString("content")
        val fullImage = bundle.getString("full_image")
        val commentNumber: Int = bundle.getInt("number_comment")
        val catName = bundle.getString("cat_name")
        val authorName = bundle.getString("author_name")
        val time = bundle.getString("time_post")
        val postIdNew = bundle.getInt("post_id")
        locatDetailId = postIdNew
        val timeS = time.split(" ")
        var myNewPreferences = Preferences(this)
        val newFontNameByUser = myNewPreferences.getFontName()
        val newFontSizeByUser = myNewPreferences.getFontSize()
        urlPost = bundle.getString("post_url")
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

    fun fromHtmlNew(html: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(html)
        }
    }

}

