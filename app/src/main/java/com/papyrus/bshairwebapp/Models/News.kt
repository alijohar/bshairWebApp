package com.papyrus.bshairwebapp.Models

data class News(
        val status: String,
        val count: Int,
        val count_total: Int,
        val pages: Int,
        val posts: List<Post>,
        val query: Query
)

data class NewsComments(
        val status: String,
        val post: Post,
        val previous_url: String
)

data class Post(
        val id: Int,
        val type: String,
        val slug: String,
        val url: String,
        val status: String,
        val title: String,
        val title_plain: String,
        val content: String,
        val excerpt: String,
        val date: String,
        val modified: String,
        val categories: List<Category>,
        val tags: List<Any>,
        val author: Author,
        val comments: List<Comment>,
        val attachments: List<Attachment>,
        val comment_count: Int,
        val comment_status: String,
        val thumbnail: String,
        val custom_fields: CustomFields,
        val thumbnail_size: String,
        val thumbnail_images: ThumbnailImages
)

data class Comment(
        val id: Int,
        val name: String,
        val url: String,
        val date: String,
        val content: String,
        val parent: Int
)

data class Category(
        val id: Int,
        val slug: String,
        val title: String,
        val description: String,
        val parent: Int,
        val post_count: Int
)

data class CustomFields(
        val websiteAdvertising: List<String>
)


data class ThumbnailImages(
        val full: Full,
        val thumbnail: Thumbnail,
        val medium: Medium,
        val medium_large: MediumLarge,
        val large: Large,
        val smallThumb: SmallThumb,
        val featuredImage: FeaturedImage
)

data class SmallThumb(
        val url: String,
        val width: Int,
        val height: Int
)

data class Thumbnail(
        val url: String,
        val width: Int,
        val height: Int
)

data class MediumLarge(
        val url: String,
        val width: Int,
        val height: Int
)

data class Full(
        val url: String,
        val width: Int,
        val height: Int
)

data class Large(
        val url: String,
        val width: Int,
        val height: Int
)

data class Medium(
        val url: String,
        val width: Int,
        val height: Int
)

data class FeaturedImage(
        val url: String,
        val width: Int,
        val height: Int
)

data class Author(
        val id: Int,
        val slug: String,
        val name: String,
        val first_name: String,
        val last_name: String,
        val nickname: String,
        val url: String,
        val description: String
)

data class Attachment(
        val id: Int,
        val url: String,
        val slug: String,
        val title: String,
        val description: String,
        val caption: String,
        val parent: Int,
        val mime_type: String,
        val images: Images
)

data class Images(
        val full: Full,
        val thumbnail: Thumbnail,
        val medium: Medium,
        val medium_large: MediumLarge,
        val large: Large,
        val smallThumb: SmallThumb,
        val featuredImage: FeaturedImage
)

data class Query(
        val ignore_sticky_posts: Boolean
)



data class CatList(
    val status: String,
    val count: Int,
    val categories: List<Category>
)


data class PostFromCatIndex(
    val status: String,
    val count: Int,
    val pages: Int,
    val category: Category,
    val posts: List<Post>
)


data class commentStatus(
    val status: String,
    val id: Int,
    val name: String,
    val url: String,
    val date: String,
    val content: String,
    val parent: Int
)