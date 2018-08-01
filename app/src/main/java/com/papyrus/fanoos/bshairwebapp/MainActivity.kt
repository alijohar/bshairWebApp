package com.papyrus.fanoos.bshairwebapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
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
import kotlinx.android.synthetic.main.nav_header_main.*
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


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


//        CustomFont
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("droidkufi_bold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )


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
                var newCount = page + 1
                progressbar.visibility = View.VISIBLE

                fetchData(newCount)


            }
        })


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun fetchDataCatList() {
        compositeDisposable.add(myNewsApi.getCatList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { catsData -> displayCatData(catsData) })

    }

    private fun displayCatData(catsData: CatList?) {

        try {
            addMenuItemInNavMenuDrawer(catsData)
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

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
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
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    //    ForCustomFont
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }


    //    For add item to submenu in drawerlayout
    private fun addMenuItemInNavMenuDrawer(catsData: CatList?) {
        val navView = findViewById<View>(R.id.nav_view) as NavigationView

        val menu = navView.menu

        for (item in 0 until catsData!!.categories.size) {
            menu.add(catsData.categories[item].title).setIcon(R.drawable.ic_menu_bshair_v).setOnMenuItemClickListener {
                val newId = catsData.categories[item].id
                val newCatTitle = catsData.categories[item].title
                val intent = Intent(this, CatActivity::class.java)
                intent.putExtra("cat_id", newId)
                intent.putExtra("cat_title", newCatTitle)
                startActivity(intent)


                true
            }
        }


        val subMenu = menu.addSubMenu("")

        subMenu.add(getString(R.string.text_setting)).setIcon(R.drawable.ic_menu_add_comment_v).setOnMenuItemClickListener {
            Toast.makeText(this, "test", Toast.LENGTH_LONG).show()
            true
        }
        subMenu.add(getString(R.string.about_us)).setIcon(R.drawable.ic_menu_share_v)
        subMenu.add(getString(R.string.contact_us)).setIcon(R.drawable.ic_menu_person_v)
        subMenu.add(getString(R.string.share_app)).setIcon(R.drawable.ic_menu_clock_v)
        subMenu.add(getString(R.string.send_bugs)).setIcon(R.drawable.ic_menu_clock_v)

        navView.invalidate()

    }


}
