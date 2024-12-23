package com.example.terra.adapters

import com.example.terra.model.NewsItem
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.terra.R
import com.squareup.picasso.Picasso

class NewsAdapter(private var newsList: MutableList<NewsItem>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_viewpager, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsList[position]

        // Menampilkan judul, deskripsi, dll.
        holder.newsTitle.text = newsItem.title
        holder.newsDescription.text = newsItem.description
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    // Fungsi untuk memperbarui data berita
    fun updateData(newsItems: List<NewsItem>) {
        newsList.clear()
        newsList.addAll(newsItems)
        notifyDataSetChanged()
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsTitle: TextView = itemView.findViewById(R.id.news_title)
        val newsDescription: TextView = itemView.findViewById(R.id.news_description)
    }
}
