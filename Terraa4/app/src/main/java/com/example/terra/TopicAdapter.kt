package com.example.terra

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TopicAdapter(
    private val topics: List<String>,
    private val onSelectionChanged: (Set<String>) -> Unit
) : RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    private val selectedTopics = mutableSetOf<String>()

    val selectedTopicsList: Set<String>
        get() = selectedTopics

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_topik, parent, false)
        return TopicViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val topic = topics[position]
        holder.bind(topic)
    }

    override fun getItemCount(): Int = topics.size

    inner class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val topicButton: Button = itemView.findViewById(R.id.btnTopicName)

        fun bind(topic: String) {
            topicButton.text = topic

            // Update button appearance based on selection state
            updateSelectionStyle(topic in selectedTopics)

            // Handle button clicks
            topicButton.setOnClickListener {
                if (topic in selectedTopics) {
                    // Deselect the topic if already selected
                    selectedTopics.remove(topic)
                } else {
                    // Select the topic if not selected and limit is not exceeded
                    if (selectedTopics.size < 3) {
                        selectedTopics.add(topic)
                    } else {
                        Log.d("TopicAdapter", "Cannot select more than 3 topics.")
                    }
                }

                // Notify the listener and update the button appearance
                onSelectionChanged(selectedTopics)
                updateSelectionStyle(topic in selectedTopics)
            }
        }

        private fun updateSelectionStyle(isSelected: Boolean) {
            val context = itemView.context
            if (isSelected) {
                topicButton.setBackgroundColor(ContextCompat.getColor(context, R.color.selected_background))
                topicButton.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                topicButton.setBackgroundColor(ContextCompat.getColor(context, R.color.unselected_background))
                topicButton.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
        }
    }
}
