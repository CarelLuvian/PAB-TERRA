package com.example.terra

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class BadgeActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var badgeAdapter: BadgeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.badge_saya)

        val backButton: ImageView = findViewById(R.id.btnBack)
        recyclerView = findViewById(R.id.rvBadges)
        recyclerView.layoutManager = GridLayoutManager(this, 3) // 3 columns for the grid

        // Handle Back Button Click
        backButton.setOnClickListener {
            finish() // Kembali ke activity sebelumnya
        }

        // Initialize Firebase
        database = FirebaseDatabase.getInstance().getReference("users")

        // Ambil username dari SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)

        if (username != null) {
            fetchQuizzesCompleted(username) // Ambil data pengguna berdasarkan username
        } else {
            Toast.makeText(this, "Pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish() // Kembali ke layar login jika username tidak ditemukan
        }
    }

    private fun fetchQuizzesCompleted(username: String) {
        database.child(username).child("quizzesCompleted").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val quizzesCompletedCount = snapshot.childrenCount.toInt()
                displayBadges(quizzesCompletedCount)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@BadgeActivity, "Gagal mengambil data pengguna", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayBadges(quizzesCompleted: Int) {
        // Tentukan badge berdasarkan jumlah kuis yang diselesaikan
        val badges = mutableListOf<Badge>()

        if (quizzesCompleted >= 1) {
            badges.add(Badge("Sprout Starter", "Anda telah berhasil membuat akun", R.drawable.sticker1))
        }
        if (quizzesCompleted >= 2) {
            badges.add(Badge("Green Learner", "Anda telah berhasil menyelesaikan 2 kuis", R.drawable.sticker2))
        }
        if (quizzesCompleted >= 5) {
            badges.add(Badge("Pest Protector", "Anda telah berhasil menyelesaikan 5 kuis", R.drawable.sticker3))
        }
        if (quizzesCompleted >= 7) {
            badges.add(Badge("Farm Friend", "Anda telah berhasil menyelesaikan 7 kuis", R.drawable.sticker4))
        }
        if (quizzesCompleted >= 10) {
            badges.add(Badge("Eco Advocate", "Anda telah berhasil menyelesaikan 10 kuis", R.drawable.sticker5))
        }
        if (quizzesCompleted >= 12) {
            badges.add(Badge("Soil Savvy", "Anda telah berhasil menyelesaikan 12 kuis", R.drawable.sticker6))
        }
        if (quizzesCompleted >= 15) {
            badges.add(Badge("Crop Champion", "Anda telah berhasil menyelesaikan 15 kuis", R.drawable.sticker7))
        }
        if (quizzesCompleted >= 17) {
            badges.add(Badge("Nature Keeper", "Anda telah berhasil menyelesaikan 17 kuis", R.drawable.sticker8))
        }
        if (quizzesCompleted >= 20) {
            badges.add(Badge("Harvest Hero", "Anda telah berhasil menyelesaikan 20 kuis", R.drawable.sticker9))
        }

        // Set up adapter
        badgeAdapter = BadgeAdapter(badges) { selectedBadge ->
            Toast.makeText(this, "Selected: ${selectedBadge.name}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = badgeAdapter
    }
}

// Data class untuk Badge
data class Badge(val name: String, val description: String, val imageRes: Int)
