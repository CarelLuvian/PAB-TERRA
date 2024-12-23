package com.example.terra

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tokopestisida.TokoPestisida
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import java.io.IOException

class ProfileActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var fullnameTextView: TextView
    private lateinit var usernameTextView: TextView
    private lateinit var editProfileButton: Button
    private lateinit var badgeButton: Button
    private lateinit var addPhotoButton: ImageView
    private lateinit var profileImageView: ImageView
    private lateinit var logoutButton: Button

    private val IMAGE_REQUEST_CODE = 1000
    private val CAMERA_REQUEST_CODE = 1001
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().reference.child("users")

        // Initialize UI elements
        fullnameTextView = findViewById(R.id.tvFullName)
        usernameTextView = findViewById(R.id.tvUserName)
        editProfileButton = findViewById(R.id.btnEditProfile)
        badgeButton = findViewById(R.id.btnBadge)
        profileImageView = findViewById(R.id.imgAvatar)
        logoutButton = findViewById(R.id.btnKeluar)

        // Set BottomNavigationView to item_profil
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView.selectedItemId = R.id.item_profil

        // Get current username from SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val currentUsername = sharedPreferences.getString("username", null)

        if (currentUsername != null) {
            // Fetch user data from Firebase
            database.child(currentUsername)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // Ambil fullname dan username dari database
                            val fullname = snapshot.child("fullname").value?.toString()
                                ?: "Nama tidak tersedia"
                            val username = snapshot.child("username").value?.toString()
                                ?: "Username tidak tersedia"
                            val avatarName = snapshot.child("avatar").value?.toString() ?: ""

                            // Perbarui TextView dengan data pengguna
                            fullnameTextView.text = fullname
                            usernameTextView.text = username

                            // Update avatar jika avatarName ada
                            if (avatarName.isNotEmpty()) {
                                val resourceId =
                                    resources.getIdentifier(avatarName, "drawable", packageName)
                                if (resourceId != 0) {
                                    profileImageView.setImageResource(resourceId) // Set image to ImageView
                                } else {
                                    Toast.makeText(
                                        this@ProfileActivity,
                                        "Avatar tidak ditemukan",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            // Jika data pengguna tidak ditemukan
                            Toast.makeText(
                                this@ProfileActivity,
                                "Data pengguna tidak ditemukan di database",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Tangani kesalahan Firebase
                        Toast.makeText(
                            this@ProfileActivity,
                            "Gagal memuat data: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        } else {
            // Jika username tidak ditemukan di SharedPreferences
            Toast.makeText(
                this,
                "Anda belum login, silakan login terlebih dahulu",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Handle Edit Profile button click
        editProfileButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        // Handle Badge button click
        badgeButton.setOnClickListener {
            val intent = Intent(this, BadgeActivity::class.java)
            startActivity(intent)
        }

        // Handle Logout button click
        logoutButton.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            // Navigate to LoginActivity
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Toast.makeText(this, "Anda telah keluar", Toast.LENGTH_SHORT).show()
        }

        // Bottom Navigation listener for navigation between pages
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home -> {
                    startActivity(Intent(this, BerandaActivity::class.java))
                    true
                }

                R.id.item_katalog -> {
                    startActivity(Intent(this, KatalogActivity::class.java))
                    true
                }

                R.id.item_profil -> {
                    true
                }

                R.id.item_toko_pestisida -> {
                    startActivity(Intent(this, TokoPestisida::class.java))
                    true
                }

                else -> false
            }
        }
    }
}
