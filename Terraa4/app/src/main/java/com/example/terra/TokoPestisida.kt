package com.example.tokopestisida

import TokoAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.terra.BerandaActivity
import com.example.terra.KatalogActivity
import com.example.terra.ProfileActivity
import com.example.terra.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class TokoPestisida : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toko_pestisida)

        // Menghubungkan RecyclerView dengan ID di layout
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        // Mengatur RecyclerView dengan LinearLayoutManager
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Data daftar toko pestisida
        val tokoList = listOf(
            Toko("Pupuk Organik Jogja", "695P+HXV, Tegal Panggung, Kec. Danurejan, Kota Yogyakarta", R.drawable.ic_toko1),
            Toko("Ratu Organik - Supplier Pupuk Organik", "Jl. Jongke Kidul, Jongke Tengah, Sendangadi, Kec. Mlati, Kabupaten Sleman", R.drawable.ic_toko2),
            Toko("Roemah Organik", "Jl. Kaliurang No.Km.10, Jetis Baran, Sardonoharjo, Kec. Ngaglik, Kabupaten Sleman", R.drawable.ic_toko3),
            Toko("Tani Subur", "Unnamed Road, Tambak Rejo, Sariharjo, Kec. Ngaglik, Kabupaten Sleman", R.drawable.ic_toko4),
            Toko("Toko Pertanian Bijak Tani", "Jl. Sersan Kusdiyo, Kalah Ijo 1, Tlogoadi, Kec. Sleman, Kabupaten Sleman", R.drawable.ic_toko5),
            Toko("Sirayuki Pupuk Organik", "Jl. Semarang - Yogyakarta No.27, Panggen 7, Tlogoadi, Kec. Sleman, Kabupaten Sleman", R.drawable.ic_toko6),
            Toko("Tanu Urban", "Jl. Kabupaten No.KM 01, Area Sawah, Nogotirto, Kec. Gamping, Kabupaten Sleman", R.drawable.ic_toko7),
            Toko("Toko Pertanian Sumber Tani Maju", "Jl. Salak, Yogyakarta, Trimulyo, Kec. Sleman, Kabupaten Sleman", R.drawable.ic_toko8),
            Toko("Kahanan Tani", "Jl. Nakula no 17, Jakal No.km 12, Candi Dukuh, Sardonoharjo, Kec. Ngaglik, Kabupaten Sleman, Daerah Istimewa Yogyakarta 50211", R.drawable.ic_toko9)
        )

        // Menghubungkan Adapter ke RecyclerView
        val adapter = TokoAdapter(tokoList)
        recyclerView.adapter = adapter

        // Mengatur BottomNavigationView
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigation)

        // Menandai item Toko Pestisida sebagai aktif
        bottomNavigationView.selectedItemId = R.id.item_toko_pestisida

        // Menangani navigasi antar menu
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home -> {
                    // Navigasi ke Home
                    startActivity(Intent(this, BerandaActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.item_toko_pestisida -> {
                    // Tetap di Toko Pestisida
                    true
                }
                R.id.item_profil -> {
                    // Navigasi ke Profil
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.item_katalog -> {
                    // Navigasi ke Katalog
                    startActivity(Intent(this, KatalogActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
        // Opsional: Tambahkan animasi transisi jika diperlukan
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    // Data class untuk toko pestisida
    data class Toko(val nama: String, val alamat: String, val imageUrl: Int)
}
