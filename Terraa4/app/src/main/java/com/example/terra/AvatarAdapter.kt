package com.example.terra

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class AvatarAdapter(
    private val avatarList: List<Int>, // List of drawable resource IDs
    private val onClick: (Int) -> Unit // Callback with the selected resource ID
) : RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_avatar, parent, false)
        return AvatarViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        val avatarResId = avatarList[position] // Get the resource ID from the list
        holder.bind(avatarResId)
    }

    override fun getItemCount(): Int = avatarList.size

    inner class AvatarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgAvatar: ImageView = itemView.findViewById(R.id.imgAvatar)

        fun bind(avatarResId: Int) {
            // Set the avatar image using the resource ID
            imgAvatar.setImageResource(avatarResId)

            // Set a click listener to trigger the callback with the selected resource ID
            itemView.setOnClickListener {
                onClick(avatarResId)
            }
        }
    }
}
