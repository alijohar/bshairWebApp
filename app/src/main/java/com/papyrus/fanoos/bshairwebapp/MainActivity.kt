package com.papyrus.fanoos.bshairwebapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.papyrus.fanoos.bshairwebapp.Adapters.BannerAdapter
import com.papyrus.fanoos.bshairwebapp.Adapters.NewsAdapter
import com.papyrus.fanoos.bshairwebapp.Api.NewsApi
import com.papyrus.fanoos.bshairwebapp.Api.NewsClinet
import com.papyrus.fanoos.bshairwebapp.Models.CatList
import com.papyrus.fanoos.bshairwebapp.Models.News
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*
import android.net.ConnectivityManager
import android.net.Uri
import android.support.v4.view.accessibility.AccessibilityEventCompat.setAction
import android.support.v4.content.ContextCompat
import android.support.design.widget.Snackbar
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_news_detail.*
import kotlinx.android.synthetic.main.no_internet.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    internal lateinit var myNewsApi: NewsApi
    internal var compositeDisposable = CompositeDisposable()
    internal lateinit var myNewsAdapter: NewsAdapter
    internal lateinit var myBannerAdapter: BannerAdapter


    var pageCount: Int = 1

    //    TODO: Must change var below when website datas changed
    var bannerTagName: String = "test"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //        CustomFont
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("droidkufi_bold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )

        if (!internet_connection(this)) {
            showGifNotInternet(this)

        } else {

            setSupportActionBar(toolbar)


//        For add list of cat in the drawerLayout
            val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
            navigationView.setNavigationItemSelectedListener(this)


            progressbar.visibility = View.VISIBLE

//        Init adapter of news
            myNewsAdapter = NewsAdapter(this, ArrayList())
            recycler_news.adapter = myNewsAdapter


//        Init Api
            val myNewsClinet = NewsClinet.instance
            myNewsApi = myNewsClinet.create(NewsApi::class.java)


//        recycler_cats_drawer.layoutManager = LinearLayoutManager(this)


//        Show Banner Data
            fetchDataBanner(bannerTagName, pageCount)

//        Show Data
            fetchData(pageCount)


//        Show Cat List Data
            fetchDataCatList()


//        Init RecyclerView
            recycler_news.setHasFixedSize(true)
            var newLayoutManger = LinearLayoutManager(this)
            recycler_news.layoutManager = newLayoutManger
            recycler_news.addOnScrollListener(object : EndlessRecyclerViewScrollListener(newLayoutManger) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    if (!internet_connection(this@MainActivity)){
                        showToastNotInternet(this@MainActivity)
                    }
                    else {
                        var newCount = page + 1
                        progressbar.visibility = View.VISIBLE

                        fetchData(newCount)

                    }
                }
            })


            val toggle = ActionBarDrawerToggle(
                    this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            drawer_layout.addDrawerListener(toggle)
            toggle.syncState()

            nav_view.setNavigationItemSelectedListener(this)
        }
    }


    private fun fetchDataCatList() {
        compositeDisposable.add(myNewsApi.getCatList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { catsData -> displayCatData(catsData) })

    }

    private fun displayCatData(catsData: CatList?) {

        try {
            addMenuItemInNavMenuDrawer(catsData, this)
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }


    private fun fetchData(localPageCount: Int) {
        compositeDisposable.add(myNewsApi.getNews(localPageCount).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { newsData -> displayData(newsData) })

    }

    private fun fetchDataBanner(bannerTagName: String, pageBanner: Int) {
        compositeDisposable.add(myNewsApi.getBannerPosts(bannerTagName, pageBanner).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { BannersData -> displayBannerData(BannersData) })
    }


    private fun displayData(newsData: News?) {
        progressbar.visibility = View.GONE
        myNewsAdapter.addMoreItem(newsData!!.posts)
    }

    private fun displayBannerData(bannersData: News?) {
        myBannerAdapter = BannerAdapter(this, bannersData!!)
        viewPager.adapter = myBannerAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_searching -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)

                return true

            }


            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_share -> {
                shareTextUrl()
            }
            R.id.nav_about -> {
                openAboutActivity(this)

            }
            R.id.nav_contact -> {
                sendMail(this)
            }
            R.id.nav_error -> {
                sendBugMail(this)
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun sendMail(context: Context) {
        val intent = Intent(Intent.ACTION_SENDTO) // it's not ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_mail))
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.say_some_thing))
        intent.data = Uri.parse(getString(R.string.bshairMail)) // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // this will make such that when user returns to your app, your app is displayed, instead of the email app.

        try {
            startActivity(intent)
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(context, getText(R.string.no_clinet_for_mail), Toast.LENGTH_SHORT).show()
        }


    }

    fun sendBugMail(context: Context) {
        val intent = Intent(Intent.ACTION_SENDTO) // it's not ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_mail_error))
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.say_some_thing))
        intent.data = Uri.parse(getString(R.string.bshair_mail_erro)) // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // this will make such that when user returns to your app, your app is displayed, instead of the email app.

        try {
            startActivity(intent)
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(context, getText(R.string.no_clinet_for_mail), Toast.LENGTH_SHORT).show()
        }


    }
    fun openAboutActivity(context: Context) {
        val intent = Intent(context, AboutUS::class.java)
        startActivity(intent)
    }


    //    ForCustomFont
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }


    //    For add item to submenu in drawerlayout
    fun addMenuItemInNavMenuDrawer(catsData: CatList?, context: Context) {
        val navView = findViewById<View>(R.id.nav_view) as NavigationView

        val menu = navView.menu

        val subMenu = menu.addSubMenu(R.string.cats)

        for (item in 0 until catsData!!.categories.size) {
            subMenu.add(catsData.categories[item].title).setIcon(R.drawable.ic_menu_bshair_v).setOnMenuItemClickListener {
                val newId = catsData.categories[item].id
                val newCatTitle = catsData.categories[item].title
                val intent = Intent(context, CatActivity::class.java)
                intent.putExtra("cat_id", newId)
                intent.putExtra("cat_title", newCatTitle)
                startActivity(intent)


                true
            }
        }


        navView.invalidate()

        val mTimer = Timer()
        mTimer.scheduleAtFixedRate(MyTimerTask(), 2000, 4000)

    }

    inner class MyTimerTask : TimerTask() {
        override fun run() {
            this@MainActivity.runOnUiThread {
                if (viewPager.currentItem == 0) {
                    viewPager.currentItem = 1
                } else if (viewPager.currentItem == 1) {
                    viewPager.currentItem = 2
                } else if (viewPager.currentItem == 2) {
                    viewPager.currentItem = 3
                } else if (viewPager.currentItem == 3) {
                    viewPager.currentItem = 4
                } else if (viewPager.currentItem == 4) {
                    viewPager.currentItem = 5
                } else {
                    viewPager.currentItem = 0
                }


            }
        }
    }

    fun showGifNotInternet(context: Context) {
        setContentView(R.layout.no_internet)
        val noInternetImage = findViewById<ImageView>(R.id.no_internet)
        Glide.with(context).load(R.drawable.tenor).into(noInternetImage)

        try_again_to_restart_activity.setOnClickListener {
            val intent = intent
            finish()
            startActivity(intent)
        }
    }

    fun internet_connection(context: Context): Boolean {
        //Check if connected to internet, output accordingly
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    fun showToastNotInternet(context: Context){
        Toast.makeText(context, R.string.no_internet, Toast.LENGTH_LONG).show()

    }

    fun shareTextUrl() {
        try {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_title))
            var sAux = "\n" + getString(R.string.share_des) + "\n\n"
            sAux += "https://play.google.com/store/apps/details?id=com.papyrus.fanoos.bshairwebapp \n\n"
            i.putExtra(Intent.EXTRA_TEXT, sAux)
            startActivity(Intent.createChooser(i, getString(R.string.share_app)))
        } catch (e: Exception) {
            //e.toString();
        }


    }


}
