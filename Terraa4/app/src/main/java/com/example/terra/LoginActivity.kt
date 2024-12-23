package com.example.terra

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference // Reference to Firebase Realtime Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().reference.child("users")

        val usernameField = findViewById<EditText>(R.id.etUsername)
        val passwordField = findViewById<EditText>(R.id.etPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val linkCreateAccount = findViewById<TextView>(R.id.link_create_account)
        val backButton = findViewById<ImageView>(R.id.btnBack)

        // Logika tombol login
        loginButton.setOnClickListener {
            val username = usernameField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            // Validasi input
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Mohon isi semua kolom!", Toast.LENGTH_SHORT).show()
            } else {
                // Validasi kredensial dari Firebase
                database.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val storedPassword = snapshot.child("password").value.toString()
                            if (storedPassword == password) {
                                // Login berhasil, simpan data ke SharedPreferences
                                val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("username", username)
                                editor.putString("fullname", snapshot.child("fullname").value.toString())
                                editor.apply()

                                // Berpindah ke activity berikutnya
                                Toast.makeText(this@LoginActivity, "Login berhasil!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@LoginActivity, PickTopikActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity, "Password salah", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@LoginActivity, "Username tidak ditemukan", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@LoginActivity, "Terjadi kesalahan: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        // Handle klik teks "Daftar"
        linkCreateAccount.setOnClickListener {
            // Navigasi ke halaman pendaftaran
            val intent = Intent(this, CreateAccount::class.java)
            startActivity(intent)
        }

        // Logika tombol back
        backButton.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
