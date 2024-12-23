package com.example.terra.repository

import com.example.terra.model.NewsItem
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class NewsRepository {

    private val newsApi: NewsApi

    init {
        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        newsApi = retrofit.create(NewsApi::class.java)
    }

    // Fungsi untuk mengambil data berita berdasarkan topik
    suspend fun fetchNews(query: String): List<NewsItem> {
        val apiKey = "e1f6f7d2865a42e09a8e3748d9c0935c" // Ganti dengan API Key Anda
        val response = newsApi.getTopHeadlines(apiKey = apiKey, query = query)

        return response.articles?.map { article ->
            NewsItem(
                title = article.title ?: "No Title",
                description = article.description ?: "No Description",
                urlToImage = article.urlToImage ?: "",
                articleUrl = article.url ?: ""
            )
        } ?: emptyList()
    }

    interface NewsApi {
        @GET("everything") // Menggunakan endpoint "everything" untuk pencarian spesifik
        suspend fun getTopHeadlines(
            @Query("apiKey") apiKey: String,
            @Query("q") query: String
        ): NewsResponse
    }

    data class NewsResponse(
        val status: String,
        val totalResults: Int,
        val articles: List<Article>
    )

    data class Article(
        val title: String?,
        val description: String?,
        val urlToImage: String?,
        val url: String?
    )
}
