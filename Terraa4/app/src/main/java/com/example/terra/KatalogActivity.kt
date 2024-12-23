package com.example.terra

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.tokopestisida.TokoPestisida
import com.google.android.material.bottomnavigation.BottomNavigationView

class KatalogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_katalog)

        // Initialize BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set selected item to "Katalog"
        bottomNavigationView.selectedItemId = R.id.item_katalog

        // Handle navigation item selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home -> {
                    val intent = Intent(this, BerandaActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0) // Disable animation
                    return@setOnItemSelectedListener true
                }

                R.id.item_katalog -> {
                    // Already in KatalogActivity
                    return@setOnItemSelectedListener true
                }

                R.id.item_profil -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0) // Disable animation
                    return@setOnItemSelectedListener true
                }

                R.id.item_toko_pestisida -> {
                    val intent = Intent(this, TokoPestisida::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        // Tombol "Tonton Sekarang" untuk quiz1
        val btnTontonSekarang = findViewById<Button>(R.id.tontonsekarang)
        btnTontonSekarang.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("quizId", "quiz1") // Kirim data untuk quiz1
            intent.putExtra("videoId", "video_url") // atau "video_url2", "video_url3"
            startActivity(intent)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        // Tombol "Tonton Sekarang 1" untuk quiz2
        val btnTontonSekarang1 = findViewById<Button>(R.id.tontonsekarang1)
        btnTontonSekarang1.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("quizId", "quiz2") // Quiz kedua
            intent.putExtra("videoId", "video_url2") // ID video sesuai dengan Firebase
            startActivity(intent)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        // Tombol "Tonton Sekarang 2" untuk quiz3
        val btnTontonSekarang2 = findViewById<Button>(R.id.tontonsekarang2)
        btnTontonSekarang2.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("quizId", "quiz3") // Kirim data untuk quiz3
            intent.putExtra("videoId", "video_url3") // atau "video_url2", "video_url3"
            startActivity(intent)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        // Tombol "Tonton Sekarang 3" untuk quiz4
        val btnTontonSekarang3 = findViewById<Button>(R.id.tontonsekarang3)
        btnTontonSekarang3.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("quizId", "quiz4") // Kirim data untuk quiz3
            intent.putExtra("videoId", "video_url4") // atau "video_url2", "video_url3"
            startActivity(intent)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        // Tombol "Tonton Sekarang 4" untuk quiz5
        val btnTontonSekarang4 = findViewById<Button>(R.id.tontonsekarang4)
        btnTontonSekarang4.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("quizId", "quiz5") // Kirim data untuk quiz3
            intent.putExtra("videoId", "video_url5") // atau "video_url2", "video_url3"
            startActivity(intent)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        // Tombol "Tonton Sekarang 5" untuk quiz6
        val btnTontonSekarang5 = findViewById<Button>(R.id.tontonsekarang5)
        btnTontonSekarang5.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("quizId", "quiz6") // Kirim data untuk quiz3
            intent.putExtra("videoId", "video_url6") // atau "video_url2", "video_url3"
            startActivity(intent)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        // Tombol "Tonton Sekarang 6" untuk quiz7
        val btnTontonSekarang6 = findViewById<Button>(R.id.tontonsekarang6)
        btnTontonSekarang6.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("quizId", "quiz7") // Kirim data untuk quiz3
            intent.putExtra("videoId", "video_url7") // atau "video_url2", "video_url3"
            startActivity(intent)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
    }
}
