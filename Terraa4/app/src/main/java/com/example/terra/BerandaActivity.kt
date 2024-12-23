package com.example.terra

import com.example.terra.model.NewsItem
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.terra.adapters.NewsAdapter
import com.example.terra.repository.NewsRepository
import com.example.tokopestisida.TokoPestisida
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import com.example.terra.adapters.AdsAdapter

class BerandaActivity : AppCompatActivity() {

    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsRepository: NewsRepository
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var viewPagerAds: ViewPager2
    private val scrollRunnable = Runnable { scrollToNextPage() }

    // Delay antar iklan (atur secara manual untuk masing-masing pergeseran)
    private val delayIklan1to2 = 7000L // 7 detik dari iklan 1 ke iklan 2
    private val delayIklan2to3 = 10000L // 5 detik dari iklan 2 ke iklan 3
    private val delayIklan3to1 = 8000L // 8 detik dari iklan 3 kembali ke iklan 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beranda)

        // Inisialisasi ViewPager2 untuk Iklan
        viewPagerAds = findViewById(R.id.viewPagerAds)
        val adsList = listOf(R.drawable.iklan1, R.drawable.iklan2, R.drawable.iklan3) // Daftar iklan
        val adsAdapter = AdsAdapter(adsList)
        viewPagerAds.adapter = adsAdapter

        // Mulai dari posisi tengah agar looping mulus
        val initialPosition = Int.MAX_VALUE / 2
        viewPagerAds.setCurrentItem(initialPosition - (initialPosition % adsList.size), false)

        // Auto-scroll iklan
        startAutoScroll()

        // Inisialisasi ViewPager2 untuk berita
        val viewPager2: ViewPager2 = findViewById(R.id.viewPager2)
        newsAdapter = NewsAdapter(mutableListOf())
        viewPager2.adapter = newsAdapter
        newsRepository = NewsRepository()
        fetchNewsData()

        // Button Lokasi 1
        findViewById<Button>(R.id.location_button_1).setOnClickListener {
            startActivity(Intent(this, TokoPestisida::class.java).apply {
                putExtra("LOKASI_ID", 1)
            })
        }

        // Button Lokasi 2
        findViewById<Button>(R.id.location_button_2).setOnClickListener {
            startActivity(Intent(this, TokoPestisida::class.java).apply {
                putExtra("LOKASI_ID", 2)
            })
        }

        // Bottom Navigation
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home -> true
                R.id.item_katalog -> {
                    startActivity(Intent(this, KatalogActivity::class.java))
                    true
                }
                R.id.item_profil -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.item_toko_pestisida -> {
                    startActivity(Intent(this, TokoPestisida::class.java))
                    true
                }
                else -> false
            }
        }

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun fetchNewsData() {
        lifecycleScope.launch {
            try {
                val topic = "pertanian" // Topik berita yang diinginkan
                val newsList = newsRepository.fetchNews(topic)
                newsAdapter.updateData(newsList)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@BerandaActivity, "Gagal memuat berita", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startAutoScroll() {
        handler.postDelayed(scrollRunnable, getDelayForCurrentPage())
    }

    private fun scrollToNextPage() {
        viewPagerAds.currentItem = viewPagerAds.currentItem + 1
        handler.postDelayed(scrollRunnable, getDelayForCurrentPage())
    }

    private fun getDelayForCurrentPage(): Long {
        return when (viewPagerAds.currentItem % 3) {
            0 -> delayIklan1to2 // Dari iklan 1 ke iklan 2
            1 -> delayIklan2to3 // Dari iklan 2 ke iklan 3
            2 -> delayIklan3to1 // Dari iklan 3 kembali ke iklan 1
            else -> delayIklan1to2
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(scrollRunnable)
    }

    override fun onResume() {
        super.onResume()
        startAutoScroll()
    }
}
