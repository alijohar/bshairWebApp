package com.papyrus.bshairwebapp.Adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.papyrus.bshairwebapp.Models.Post
import com.papyrus.bshairwebapp.bshairwebapp.R
import com.papyrus.bshairwebapp.Ui.NewsDetail

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
        var imageThumbNewsUrlString:String?

        try{
            imageThumbNewsUrlString = newsList[position].thumbnail

        }catch (E:Exception){
            imageThumbNewsUrlString = "http://www.bshaer.net/wp-content/uploads/2018/12/default.jpg"

        }
        val titleNews = newsList[position].title
        val contentNews = newsList[position].content
        val tags = newsList[position].tags
        var fullImage:String?
        try {
            fullImage = newsList[position].thumbnail_images.medium_large.url

        }catch (E:Exception){
            fullImage = "http://www.bshaer.net/wp-content/uploads/2018/12/default.jpg"
        }
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
            var itsArticle: Boolean = false

            val newsIntent = Intent(context, NewsDetail::class.java)

            for (item in tags) {
                if (item.title == "مقال") {
                    itsArticle = true
                    val authorArcticle = newsList[position].custom_fields.authorName[0]
                    newsIntent.putExtra("authorArcticle", authorArcticle)


                }

            }

//            TODO: must get authorname and data and category and count of comments

            newsIntent.putExtra("itsArticle", itsArticle)
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

    fun addMoreItemForSearch(posts: List<Post>) {
        newsList.clear()
        newsList.addAll(posts)
        this.notifyDataSetChanged()
    }

    fun setNewsList(): ArrayList<Post> {
        return newsList
    }


}