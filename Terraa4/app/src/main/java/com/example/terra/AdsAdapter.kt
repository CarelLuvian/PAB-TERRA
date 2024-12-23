package com.example.terra.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.terra.R

class AdsAdapter(private val adsList: List<Int>) : RecyclerView.Adapter<AdsAdapter.AdsViewHolder>() {

    // ViewHolder untuk gambar iklan
    inner class AdsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.adImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ads_viewpager, parent, false)
        return AdsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdsViewHolder, position: Int) {
        // Posisi looping: gunakan indeks modulus untuk membuat iklan berulang
        val actualPosition = position % adsList.size
        holder.imageView.setImageResource(adsList[actualPosition])
    }

    // Return jumlah item yang sangat besar untuk efek looping
    override fun getItemCount(): Int = Int.MAX_VALUE
}
