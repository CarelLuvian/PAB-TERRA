package com.example.terra.model

data class NewsItem(
    val title: String,
    val description: String,
    val urlToImage: String, // Ubah dari imageUrl ke urlToImage
    val articleUrl: String
)
