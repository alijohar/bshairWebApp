package com.papyrus.fanoos.bshairwebapp

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.papyrus.fanoos.bshairwebapp.Adapters.NewsAdapter
import com.papyrus.fanoos.bshairwebapp.Api.NewsApi
import com.papyrus.fanoos.bshairwebapp.Api.NewsClinet
import com.papyrus.fanoos.bshairwebapp.Models.News
import com.papyrus.fanoos.bshairwebapp.Models.PostFromCatIndex
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.app_bar_main_search.*
import kotlinx.android.synthetic.main.content_main_cat.*
import kotlinx.android.synthetic.main.content_main_search.*
import kotlinx.android.synthetic.main.no_internet.*
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class SearchActivity : AppCompatActivity() {
    var textSearched: String? = null
    internal lateinit var myNewsApi: NewsApi
    internal var compositeDisposable = CompositeDisposable()
    internal lateinit var myNewsAdapter: NewsAdapter

    var pageCount: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        if (!internet_connection(this)) {
            showGifNotInternet(this)

        } else {
            mSearchView.isIconified = false
            mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {

                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    textSearched = query
                    //        Show Data
                    progressbar_search.visibility = View.VISIBLE

                    //        Init adapter of news
                    myNewsAdapter = NewsAdapter(this@SearchActivity, ArrayList())
                    recycler_news_search.adapter = myNewsAdapter


//        Init Api
                    val myNewsClinetNew = NewsClinet.instance
                    myNewsApi = myNewsClinetNew.create(NewsApi::class.java)


//        CustomFont
                    CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                            .setDefaultFontPath("droidkufi_bold.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )



                    fetchData(textSearched!!, pageCount)


//        Init RecyclerView
                    recycler_news_search.setHasFixedSize(true)
                    var newLayoutManger = LinearLayoutManager(this@SearchActivity)
                    recycler_news_search.layoutManager = newLayoutManger
                    recycler_news_search.addOnScrollListener(object : EndlessRecyclerViewScrollListener(newLayoutManger) {
                        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                            var newCount = page + 1
                            progressbar_search.visibility = View.VISIBLE

                            fetchData(textSearched!!, newCount)


                        }
                    })

                    fetchData(textSearched!!, pageCount)
                    return false

                }

            })


        }
    }
        private fun fetchData(textSearched: String, localPageCount: Int) {
            compositeDisposable.add(myNewsApi.getPostFromSearching(textSearched, localPageCount).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { newsData -> displayData(newsData) })
        }

        private fun displayData(newsData: News?) {
            progressbar_search.visibility = View.GONE
            myNewsAdapter.addMoreItem(newsData!!.posts)
        }

        //    ForCustomFont
        override fun attachBaseContext(newBase: Context) {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
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


}
