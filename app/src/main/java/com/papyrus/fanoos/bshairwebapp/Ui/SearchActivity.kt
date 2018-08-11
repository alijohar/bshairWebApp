package com.papyrus.fanoos.bshairwebapp.Ui

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.View
import com.papyrus.fanoos.bshairwebapp.Adapters.NewsAdapter
import com.papyrus.fanoos.bshairwebapp.Api.NewsApi
import com.papyrus.fanoos.bshairwebapp.Api.NewsClinet
import com.papyrus.fanoos.bshairwebapp.Util.EndlessRecyclerViewScrollListener
import com.papyrus.fanoos.bshairwebapp.Models.News
import com.papyrus.fanoos.bshairwebapp.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.app_bar_main_search.*
import kotlinx.android.synthetic.main.content_main_search.*
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class SearchActivity : AppCompatActivity() {
    var textSearched: String? = null
    internal lateinit var myNewsApi: NewsApi
    internal var compositeDisposable = CompositeDisposable()
    internal lateinit var myNewsAdapter: NewsAdapter
    val checkConnection = MainActivity()
    var isRunOneTimeAtLeast:Boolean? = null

    var pageCount: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        if (!checkConnection.internet_connection(this)) {
            checkConnection.showToastNotInternet(this)

        } else {
            mSearchView.isIconified = false
            mSearchView.queryHint = getString(R.string.search)
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
                            .setDefaultFontPath("droidkufi_regular.ttf")
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
                            if (!checkConnection.internet_connection(this@SearchActivity)) {
                                checkConnection.showToastNotInternet(this@SearchActivity)
                            } else {
                                var newCount = page + 1
                                progressbar_search.visibility = View.VISIBLE

                                fetchData(textSearched!!, newCount)
                            }

                        }
                    })

                    fetchData(textSearched!!, pageCount)
                    return false

                }

            })


        }
    }

    private fun fetchData(textSearched: String, localPageCount: Int) {
        try {
            compositeDisposable.add(myNewsApi.getPostFromSearching(textSearched, localPageCount).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { newsData -> displayData(newsData) })

        }catch (e:Exception){
            checkConnection.showToastNotInternet(this)
        }

    }

    private fun displayData(newsData: News?) {
        if (newsData!!.posts.isNotEmpty()) {
            isRunOneTimeAtLeast = true
            no_item_text_view.visibility = View.GONE
            progressbar_search.visibility = View.GONE
            Log.i("newSlist1", newsData.posts.size.toString())
            myNewsAdapter.addMoreItemForSearch(newsData.posts)
        }
            else{
            if (isRunOneTimeAtLeast == true){
                if (myNewsAdapter.setNewsList().size == 0){
                    progressbar_search.visibility = View.GONE
                    no_item_text_view.text = getText(R.string.no_item)
                    no_item_text_view.visibility = View.VISIBLE
                }else{
                    progressbar_search.visibility = View.GONE
                    no_item_text_view.visibility = View.GONE
                }


            }else if (isRunOneTimeAtLeast == null) {
            progressbar_search.visibility = View.GONE
            no_item_text_view.text = getText(R.string.no_item)
            no_item_text_view.visibility = View.VISIBLE

        }
    }
    }

    //    ForCustomFont
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }


}
