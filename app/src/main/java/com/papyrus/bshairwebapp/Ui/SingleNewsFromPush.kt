package com.papyrus.bshairwebapp.Ui

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.papyrus.bshairwebapp.bshairwebapp.R
import kotlinx.android.synthetic.main.app_bar_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class SingleNewsFromPush : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        des_news_detail.loadDataWithBaseURL("file:///android_asset/","hi, its worked","text/html","utf-8",null)


        setSupportActionBar(toolbar_detail)
        title_news_detail.text = "hello"

    }
}