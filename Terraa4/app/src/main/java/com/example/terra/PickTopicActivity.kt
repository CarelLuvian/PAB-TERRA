package com.example.terra

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import TokoAdapter
import android.content.Intent

class PickTopikActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_topik)

        // List of topics
        val topics = listOf(
            "Pestisida", "Tanaman", "Lingkungan", "Insektisida",
            "Herbisida", "Fungisida", "Rodentisida", "Jamur",
            "Pertanian", "Hama", "IPM", "Teknologi", "Mikrobiologi",
            "Fumigatior", "Soil Application", "Umpan Beracun", "Hortikultura",
            " Penyemprotan", "Gulma", "Drone", "Hemat", "Pengendalian", "Modern"
        )

        // Initialize RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rvTopics)
        recyclerView.layoutManager = GridLayoutManager(this, 3) // 3 columns

        // Adapter setup
        val topicAdapter = TopicAdapter(topics) { selectedTopics ->
            // Enable/disable "Lanjut" button based on selection
            findViewById<Button>(R.id.btnNext).isEnabled = selectedTopics.size == 3
        }
        recyclerView.adapter = topicAdapter

        // Next button action
        val btnNext = findViewById<Button>(R.id.btnNext)
        btnNext.setOnClickListener {
            // Get selected topics and proceed
            val selectedTopics = topicAdapter.selectedTopicsList.toList()
            Toast.makeText(this, "Selected: $selectedTopics", Toast.LENGTH_SHORT).show()
            // Navigate to BerandaActivity
            val intent = Intent(this, BerandaActivity::class.java)
            // Pass selected topics to BerandaActivity (optional)
            intent.putStringArrayListExtra("selectedTopics", ArrayList(selectedTopics))
            startActivity(intent)
        }
    }
}