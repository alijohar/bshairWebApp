package com.papyrus.fanoos.bshairwebapp

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.papyrus.fanoos.bshairwebapp.Adapters.BannerAdapter
import com.papyrus.fanoos.bshairwebapp.Adapters.NewsAdapter
import com.papyrus.fanoos.bshairwebapp.Api.NewsApi
import com.papyrus.fanoos.bshairwebapp.Api.NewsClinet
import com.papyrus.fanoos.bshairwebapp.Models.News
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    internal lateinit var myNewsApi: NewsApi
    internal var compositeDisposable = CompositeDisposable()
    internal lateinit var myNewsAdapter: NewsAdapter
    internal lateinit var myBannerAdapter:BannerAdapter
    internal var myImagesBanner = ArrayList<String>()
    var pageCount:Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        progressbar.visibility = View.VISIBLE

//        Init adapter of news
        myNewsAdapter = NewsAdapter(this, ArrayList())
        recycler_news.adapter = myNewsAdapter

//        Init adapter of banner
        myImagesBanner.add("https://cdn.pixabay.com/photo/2014/09/19/12/30/pencils-452238_960_720.jpg")
        myImagesBanner.add("https://cdn.pixabay.com/photo/2015/03/26/09/40/pencil-690050_960_720.jpg")
        myImagesBanner.add("https://cdn.pixabay.com/photo/2015/09/02/12/28/pencil-918449_960_720.jpg")
        myImagesBanner.add("https://cdn.pixabay.com/photo/2014/05/02/21/50/home-office-336378_960_720.jpg")

        myBannerAdapter = BannerAdapter(this, myImagesBanner)
        viewPager.adapter = myBannerAdapter

//        Init Api
        val myNewsClinet = NewsClinet.instance
        myNewsApi = myNewsClinet.create(NewsApi::class.java)


//        Show Data
        fetchData(pageCount)



//        Init RecyclerView
        recycler_news.setHasFixedSize(true)
        var newLayoutManger =  LinearLayoutManager(this)
        recycler_news.layoutManager = newLayoutManger
        recycler_news.addOnScrollListener(object: EndlessRecyclerViewScrollListener(newLayoutManger){
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

    private fun fetchData(localPageCount:Int) {
        compositeDisposable.add(myNewsApi.getNews(localPageCount).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{newsData -> displayData(newsData)})

    }

    private fun displayData(newsData: News?) {
        progressbar.visibility = View.GONE
        myNewsAdapter.addMoreItem(newsData!!.posts)
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
            R.id.action_searching -> return true
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
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
