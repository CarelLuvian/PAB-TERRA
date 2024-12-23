package com.example.terra

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Referensi tombol dan teks
        val btnSignIn = findViewById<Button>(R.id.btn_sign_in)
        val linkCreateAccount = findViewById<TextView>(R.id.link_create_account)

        // Handle tombol Masuk
        btnSignIn.setOnClickListener {
            // Logika autentikasi dapat ditambahkan di sini
            Toast.makeText(this, "Silahkan Isi Data Anda", Toast.LENGTH_SHORT).show()

            // Contoh navigasi ke halaman utama (LoginActivity)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Tutup aktivitas ini
        }

        // Handle klik teks "Daftar"
        linkCreateAccount.setOnClickListener {
            // Navigasi ke halaman pendaftaran
            val intent = Intent(this, CreateAccount::class.java)
            startActivity(intent)
        }
    }
}
