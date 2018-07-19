package com.papyrus.fanoos.bshairwebapp

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

class NewsDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        setSupportActionBar(toolbar_detail)

        val bundle = intent.extras
        val title = bundle.getString("title")
        val content = bundle.getString("content")
        val fullImage = bundle.getString("full_image")

        des_news_detail.text = Html.fromHtml(Html.fromHtml(content).toString())
        title_news_detail.text = title
        Glide.with(this).load(fullImage).into(image_news_detial)

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
                Snackbar.make(news_detail, "Share is good", Snackbar.LENGTH_LONG).show()
                return true
            }
            R.id.action_adding_comment -> {
                Snackbar.make(news_detail, "comment is good", Snackbar.LENGTH_LONG).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
