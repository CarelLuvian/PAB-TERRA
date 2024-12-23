package com.example.terra

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BadgeAdapter(private val badgeList: List<Badge>, private val onClick: (Badge) -> Unit) :
    RecyclerView.Adapter<BadgeAdapter.BadgeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_badge, parent, false)
        return BadgeViewHolder(view)
    }

    override fun onBindViewHolder(holder: BadgeViewHolder, position: Int) {
        val badge = badgeList[position]
        holder.bind(badge)
    }

    override fun getItemCount(): Int {
        return badgeList.size
    }

    inner class BadgeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgBadge: ImageView = itemView.findViewById(R.id.imgBadge)
        private val tvBadgeName: TextView = itemView.findViewById(R.id.tvBadgeName)
        private val tvBadgeDescription: TextView = itemView.findViewById(R.id.tvBadgeDescription)

        fun bind(badge: Badge) {
            imgBadge.setImageResource(badge.imageRes)
            tvBadgeName.text = badge.name
            tvBadgeDescription.text = badge.description

            // Set click listener to call onClick with the selected badge
            itemView.setOnClickListener {
                onClick(badge) // Pass the selected badge to the onClick function
            }
        }
    }
}
