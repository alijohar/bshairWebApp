package com.papyrus.fanoos.bshairwebapp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.papyrus.fanoos.bshairwebapp.Adapters.NewsAdapter
import com.papyrus.fanoos.bshairwebapp.Api.NewsApi
import com.papyrus.fanoos.bshairwebapp.Api.NewsClinet
import com.papyrus.fanoos.bshairwebapp.Models.CatList
import com.papyrus.fanoos.bshairwebapp.Models.News
import com.papyrus.fanoos.bshairwebapp.Models.PostFromCatIndex
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_cat.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.app_bar_main_cat.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main_cat.*
import kotlinx.android.synthetic.main.no_internet.*
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class CatActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    internal lateinit var myNewsApi: NewsApi
    internal var compositeDisposable = CompositeDisposable()
    internal lateinit var myNewsAdapter: NewsAdapter

    var pageCount: Int = 1
    val checkConnection = MainActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat)
        if (!checkConnection.internet_connection(this)) {
            checkConnection.showToastNotInternet(this)

        } else {
            toolbar_cat.title = ""
            setSupportActionBar(toolbar_cat)

            val bundle = intent.extras
            val idCat = bundle.getInt("cat_id")
            val titleCat = bundle.getString("cat_title")

            title_cat.text = titleCat

            //        For add list of cat in the drawerLayout
            val navigationView = findViewById<View>(R.id.nav_view_cat) as NavigationView
            navigationView.setNavigationItemSelectedListener(this)

            progressbar_cat.visibility = View.VISIBLE


            //        Init adapter of news
            myNewsAdapter = NewsAdapter(this, ArrayList())
            recycler_cats_index.adapter = myNewsAdapter


//        Init Api
            val myNewsClinetNew = NewsClinet.instance
            myNewsApi = myNewsClinetNew.create(NewsApi::class.java)


//        CustomFont
            CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                    .setDefaultFontPath("droidkufi_bold.ttf")
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            )

            //        Show Data
            fetchData(idCat, pageCount)


//        Show Cat List Data
            fetchDataCatList()


//        Init RecyclerView
            recycler_cats_index.setHasFixedSize(true)
            var newLayoutManger = LinearLayoutManager(this)
            recycler_cats_index.layoutManager = newLayoutManger
            recycler_cats_index.addOnScrollListener(object : EndlessRecyclerViewScrollListener(newLayoutManger) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    var newCount = page + 1
                    progressbar_cat.visibility = View.VISIBLE

                    fetchData(idCat, newCount)


                }
            })


            val toggle = ActionBarDrawerToggle(
                    this, drawer_layout_cat, toolbar_cat, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            drawer_layout_cat.addDrawerListener(toggle)
            toggle.syncState()

            nav_view_cat.setNavigationItemSelectedListener(this)
        }
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


    private fun fetchData(idCat: Int, localPageCount: Int) {
        compositeDisposable.add(myNewsApi.getPostFromCat(idCat, localPageCount).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { newsData -> displayData(newsData) })

    }

    private fun displayData(newsData: PostFromCatIndex?) {
        progressbar_cat.visibility = View.GONE
        myNewsAdapter.addMoreItem(newsData!!.posts)
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
        val navView = findViewById<View>(R.id.nav_view_cat) as NavigationView

        val menu = navView.menu

        for (item in 0 until catsData!!.categories.size) {
            menu.add(catsData.categories[item].title).setIcon(R.drawable.ic_menu_bshair_v).setOnMenuItemClickListener {
                val newId = catsData.categories[item].id
                val intent = Intent(this, CatActivity::class.java)
                intent.putExtra("cat_id", newId)
                startActivity(intent)


                true
            }
        }


        val subMenu = menu.addSubMenu("")

        subMenu.add(getString(R.string.text_setting)).setIcon(R.drawable.ic_menu_add_comment_v)
        subMenu.add(getString(R.string.about_us)).setIcon(R.drawable.ic_menu_share_v)
        subMenu.add(getString(R.string.contact_us)).setIcon(R.drawable.ic_menu_person_v)
        subMenu.add(getString(R.string.share_app)).setIcon(R.drawable.ic_menu_clock_v)
        subMenu.add(getString(R.string.send_bugs)).setIcon(R.drawable.ic_menu_clock_v)



        navView.invalidate()

    }




}
