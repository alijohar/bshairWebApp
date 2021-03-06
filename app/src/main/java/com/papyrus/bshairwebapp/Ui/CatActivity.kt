package com.papyrus.bshairwebapp.Ui

import android.content.Context
import android.content.Intent
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
import com.papyrus.bshairwebapp.Adapters.NewsAdapter
import com.papyrus.bshairwebapp.Api.NewsApi
import com.papyrus.bshairwebapp.Api.NewsClinet
import com.papyrus.bshairwebapp.Util.EndlessRecyclerViewScrollListener
import com.papyrus.bshairwebapp.Models.CatList
import com.papyrus.bshairwebapp.Models.PostFromCatIndex
import com.papyrus.bshairwebapp.bshairwebapp.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_cat.*
import kotlinx.android.synthetic.main.app_bar_main_cat.*
import kotlinx.android.synthetic.main.content_main_cat.*
import kotlinx.android.synthetic.main.no_internet.*
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class CatActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    internal lateinit var myNewsApi: NewsApi
    internal var compositeDisposable = CompositeDisposable()
    internal lateinit var myNewsAdapter: NewsAdapter
    var isShowErrorPageBefor:Boolean = false

    var pageCount: Int = 1
    val mainActivityObject = MainActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat)
        if (!mainActivityObject.internet_connection(this)) {
            mainActivityObject.showToastNotInternet(this)

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
                    .setDefaultFontPath("droidkufi_regular.ttf")
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
        try {
            compositeDisposable.add(myNewsApi.getCatList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe ({ catsData -> displayCatData(catsData) },{ error -> displayError(error, this)}))
        } catch (e: Exception) {
            mainActivityObject.showToastNotInternet(this)
        }

    }

    private fun displayCatData(catsData: CatList?) {

        try {
            addMenuItemInNavMenuDrawer(catsData, this)
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }


    private fun fetchData(idCat: Int, localPageCount: Int) {
        try {
            compositeDisposable.add(myNewsApi.getPostFromCat(idCat, localPageCount).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe ({ newsData -> displayData(newsData) }, { error -> displayError(error, this)}))

        } catch (e: Exception) {
            mainActivityObject.showToastNotInternet(this)
        }

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
            R.id.action_sending_news -> {
                mainActivityObject.sendNews(this)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_share -> {
                mainActivityObject.shareTextUrl(this)
            }
//            R.id.nav_about -> {
//                mainActivityObject.openAboutActivity(this)
//
//            }
            R.id.nav_contact -> {
                mainActivityObject.sendMail(this)
            }
            R.id.nav_error -> {
                mainActivityObject.sendBugMail(this)
            }
            R.id.change_font_and_size -> {
                mainActivityObject.showDialogFontChooser(this)
            }

        }

        drawer_layout_cat.closeDrawer(GravityCompat.START)
        return true
    }


    //    ForCustomFont
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    //    For add item to submenu in drawerlayout
        fun addMenuItemInNavMenuDrawer(catsData: CatList?, context: Context) {
            val navView = findViewById<View>(R.id.nav_view_cat) as NavigationView

            val menu = navView.menu

            val subMenu = menu.addSubMenu(R.string.cats)
            for (item in 0 until catsData!!.categories.size) {
                if (catsData.categories[item].id == 7) {
                    subMenu.add(1, 1, 1, catsData.categories[item].title).setIcon(R.drawable.ic_menu_bshair_v).setOnMenuItemClickListener {
                        val newId = catsData.categories[item].id
                        val newCatTitle = catsData.categories[item].title
                        val intent = Intent(context, CatActivity::class.java)
                        intent.putExtra("cat_id", newId)
                        intent.putExtra("cat_title", newCatTitle)
                        startActivity(intent)


                        true
                    }
                } else if (catsData.categories[item].id == 10) {
                    subMenu.add(1, 1, 2, catsData.categories[item].title).setIcon(R.drawable.ic_menu_bshair_v).setOnMenuItemClickListener {
                        val newId = catsData.categories[item].id
                        val newCatTitle = catsData.categories[item].title
                        val intent = Intent(context, CatActivity::class.java)
                        intent.putExtra("cat_id", newId)
                        intent.putExtra("cat_title", newCatTitle)
                        startActivity(intent)


                        true
                    }

                } else if (catsData.categories[item].id == 18) {
                    subMenu.add(1, 1, 3, catsData.categories[item].title).setIcon(R.drawable.ic_menu_bshair_v).setOnMenuItemClickListener {
                        val newId = catsData.categories[item].id
                        val newCatTitle = catsData.categories[item].title
                        val intent = Intent(context, CatActivity::class.java)
                        intent.putExtra("cat_id", newId)
                        intent.putExtra("cat_title", newCatTitle)
                        startActivity(intent)


                        true
                    }
                } else if (catsData.categories[item].id == 5) {
                    subMenu.add(1, 1, 4, catsData.categories[item].title).setIcon(R.drawable.ic_menu_bshair_v).setOnMenuItemClickListener {
                        val newId = catsData.categories[item].id
                        val newCatTitle = catsData.categories[item].title
                        val intent = Intent(context, CatActivity::class.java)
                        intent.putExtra("cat_id", newId)
                        intent.putExtra("cat_title", newCatTitle)
                        startActivity(intent)


                        true
                    }
                } else if (catsData.categories[item].id == 3) {
                    subMenu.add(1, 1, 5, catsData.categories[item].title).setIcon(R.drawable.ic_menu_bshair_v).setOnMenuItemClickListener {
                        val newId = catsData.categories[item].id
                        val newCatTitle = catsData.categories[item].title
                        val intent = Intent(context, CatActivity::class.java)
                        intent.putExtra("cat_id", newId)
                        intent.putExtra("cat_title", newCatTitle)
                        startActivity(intent)


                        true
                    }
                } else if (catsData.categories[item].id == 13) {
                    subMenu.add(1, 1, 6, catsData.categories[item].title).setIcon(R.drawable.ic_menu_bshair_v).setOnMenuItemClickListener {
                        val newId = catsData.categories[item].id
                        val newCatTitle = catsData.categories[item].title
                        val intent = Intent(context, CatActivity::class.java)
                        intent.putExtra("cat_id", newId)
                        intent.putExtra("cat_title", newCatTitle)
                        startActivity(intent)


                        true
                    }
                } else if (catsData.categories[item].id == 20) {
                    subMenu.add(1, 1, 7, catsData.categories[item].title).setIcon(R.drawable.ic_menu_bshair_v).setOnMenuItemClickListener {
                        val newId = catsData.categories[item].id
                        val newCatTitle = catsData.categories[item].title
                        val intent = Intent(context, CatActivity::class.java)
                        intent.putExtra("cat_id", newId)
                        intent.putExtra("cat_title", newCatTitle)
                        startActivity(intent)


                        true
                    }
                } else if (catsData.categories[item].id == 50) {
                    subMenu.add(1, 1, 8, catsData.categories[item].title).setIcon(R.drawable.ic_menu_bshair_v).setOnMenuItemClickListener {
                        val newId = catsData.categories[item].id
                        val newCatTitle = catsData.categories[item].title
                        val intent = Intent(context, CatActivity::class.java)
                        intent.putExtra("cat_id", newId)
                        intent.putExtra("cat_title", newCatTitle)
                        startActivity(intent)


                        true
                    }
                } else if (catsData.categories[item].id == 51) {
                    subMenu.add(1, 1, 9, catsData.categories[item].title).setIcon(R.drawable.ic_menu_bshair_v).setOnMenuItemClickListener {
                        val newId = catsData.categories[item].id
                        val newCatTitle = catsData.categories[item].title
                        val intent = Intent(context, CatActivity::class.java)
                        intent.putExtra("cat_id", newId)
                        intent.putExtra("cat_title", newCatTitle)
                        startActivity(intent)


                        true
                    }
                } else if (catsData.categories[item].id == 52) {
                    subMenu.add(1, 1, 10, catsData.categories[item].title).setIcon(R.drawable.ic_menu_bshair_v).setOnMenuItemClickListener {
                        val newId = catsData.categories[item].id
                        val newCatTitle = catsData.categories[item].title
                        val intent = Intent(context, CatActivity::class.java)
                        intent.putExtra("cat_id", newId)
                        intent.putExtra("cat_title", newCatTitle)
                        startActivity(intent)


                        true

                    }
                } else if (catsData.categories[item].id == 53) {
                    subMenu.add(1, 1, 10, catsData.categories[item].title).setIcon(R.drawable.ic_menu_bshair_v).setOnMenuItemClickListener {
                        val newId = catsData.categories[item].id
                        val newCatTitle = catsData.categories[item].title
                        val intent = Intent(context, CatActivity::class.java)
                        intent.putExtra("cat_id", newId)
                        intent.putExtra("cat_title", newCatTitle)
                        startActivity(intent)


                        true

                    }
                }
            }

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
