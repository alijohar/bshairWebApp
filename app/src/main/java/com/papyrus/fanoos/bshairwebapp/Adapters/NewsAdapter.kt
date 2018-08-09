package com.papyrus.fanoos.bshairwebapp.Adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.papyrus.fanoos.bshairwebapp.Models.Post
import com.papyrus.fanoos.bshairwebapp.R
import com.papyrus.fanoos.bshairwebapp.Ui.NewsDetail

class NewsAdapter(internal var context: Context, internal var newsList: ArrayList<Post>) : RecyclerView.Adapter<NewsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)

        return NewsHolder(itemView)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
            val locatImageView = holder.news_image
            val imageThumbNewsUrlString = newsList[position].thumbnail
            val titleNews = newsList[position].title
            val contentNews = newsList[position].content
            val fullImage = newsList[position].thumbnail_images.medium_large.url
            val urlPost: String = newsList[position].url
            val numberComment = newsList[position].comment_count
//        TODO: must get all cats if the customer needs it
            val catName = newsList[position].categories[0].title
            val authorName = newsList[position].author.nickname
            val timePosting = newsList[position].date
            val postId = newsList[position].id
            holder.news_title.text = titleNews
            Glide.with(context).load(imageThumbNewsUrlString).into(locatImageView)

            val newCardView = holder.news_card_view
            newCardView.setOnClickListener {
                val newsIntent = Intent(context, NewsDetail::class.java)
//            TODO: must get authorname and data and category and count of comments
                newsIntent.putExtra("title", titleNews)
                newsIntent.putExtra("content", contentNews)
                newsIntent.putExtra("full_image", fullImage)
                newsIntent.putExtra("post_url", urlPost)
                newsIntent.putExtra("number_comment", numberComment)
                newsIntent.putExtra("cat_name", catName)
                newsIntent.putExtra("author_name", authorName)
                newsIntent.putExtra("time_post", timePosting)
                newsIntent.putExtra("post_id", postId)








                context.startActivity(newsIntent)
            }

    }


    fun addMoreItem(posts: List<Post>) {
        newsList.addAll(posts)
        this.notifyDataSetChanged()
    }

    fun setNewsList():ArrayList<Post>{
        return newsList
    }





}