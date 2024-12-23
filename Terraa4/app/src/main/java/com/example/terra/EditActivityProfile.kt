package com.example.terra

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var fullnameEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var saveChangesButton: Button
    private lateinit var editNameIcon: ImageView
    private lateinit var editUsernameIcon: ImageView
    private lateinit var editPasswordIcon: ImageView
    private lateinit var buttonBack: ImageView
    private lateinit var btnUbahAvatar: ImageView
    private lateinit var profilePicture: ImageView

    private lateinit var profileFullname: TextView
    private lateinit var profileUsername: TextView

    private var isNameEditable = false
    private var isUsernameEditable = false
    private var isPasswordEditable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Inisialisasi Database
        database = FirebaseDatabase.getInstance().reference.child("users")

        // Inisialisasi Komponen UI
        fullnameEditText = findViewById(R.id.edit_fullname)
        usernameEditText = findViewById(R.id.edit_username)
        passwordEditText = findViewById(R.id.edit_password)
        saveChangesButton = findViewById(R.id.btnSaveChanges)
        editNameIcon = findViewById(R.id.edit_name_icon)
        editUsernameIcon = findViewById(R.id.edit_username_icon)
        editPasswordIcon = findViewById(R.id.edit_password_icon)
        buttonBack = findViewById(R.id.btnBack)
        btnUbahAvatar = findViewById(R.id.ubah_avatar)
        profilePicture = findViewById(R.id.profile_picture)

        profileFullname = findViewById(R.id.profile_fullname)
        profileUsername = findViewById(R.id.profile_username)

        // Disable input field awal
        disableAllFields()

        // Ambil data user dari SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val currentUsername = sharedPreferences.getString("username", null)

        if (currentUsername != null) {
            fetchUserData(currentUsername)
        } else {
            Toast.makeText(this, "Username tidak ditemukan di perangkat", Toast.LENGTH_SHORT).show()
        }

        // Edit field listener
        editNameIcon.setOnClickListener {
            toggleField(fullnameEditText)
            isNameEditable = !isNameEditable
        }

        editUsernameIcon.setOnClickListener {
            toggleField(usernameEditText)
            isUsernameEditable = !isUsernameEditable
        }

        editPasswordIcon.setOnClickListener {
            toggleField(passwordEditText)
            isPasswordEditable = !isPasswordEditable
        }

        // Simpan perubahan
        saveChangesButton.setOnClickListener {
            saveChanges(currentUsername, sharedPreferences)
        }

        // Arahkan ke halaman AvatarSelectionActivity
        btnUbahAvatar.setOnClickListener {
            val intent = Intent(this, AvatarSelectionActivity::class.java)
            startActivity(intent)
        }

        // Tombol kembali
        buttonBack.setOnClickListener { finish() }
    }

    private fun fetchUserData(username: String) {
        database.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val fullname = snapshot.child("fullname").value.toString()
                    val username = snapshot.child("username").value.toString()
                    val avatarName = snapshot.child("avatar").value?.toString() ?: ""

                    fullnameEditText.setText(fullname)
                    usernameEditText.setText(username)

                    profileFullname.text = fullname
                    profileUsername.text = username

                    // Set gambar profil jika ada avatar
                    if (avatarName.isNotEmpty()) {
                        val resourceId = resources.getIdentifier(avatarName, "drawable", packageName)
                        if (resourceId != 0) {
                            profilePicture.setImageResource(resourceId)
                        }
                    }
                } else {
                    Toast.makeText(this@EditProfileActivity, "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditProfileActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveChanges(username: String?, sharedPreferences: SharedPreferences) {
        if (username == null) {
            Toast.makeText(this, "Gagal menyimpan, username tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedFullname = fullnameEditText.text.toString().trim()
        val updatedUsername = usernameEditText.text.toString().trim()
        val updatedPassword = passwordEditText.text.toString().trim()

        if (updatedFullname.isEmpty() || updatedUsername.isEmpty()) {
            Toast.makeText(this, "Nama lengkap dan username tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        if (updatedPassword.isNotEmpty() && updatedPassword.length < 6) {
            Toast.makeText(this, "Password harus terdiri dari minimal 6 karakter", Toast.LENGTH_SHORT).show()
            return
        }

        val updates = mutableMapOf<String, Any>(
            "fullname" to updatedFullname,
            "username" to updatedUsername
        )

        if (updatedPassword.isNotEmpty()) {
            updates["password"] = updatedPassword
        }

        database.child(username).updateChildren(updates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                sharedPreferences.edit()
                    .putString("username", updatedUsername)
                    .apply()

                Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                profileFullname.text = updatedFullname
                profileUsername.text = updatedUsername
                disableAllFields()
            } else {
                Toast.makeText(this, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toggleField(editText: EditText) {
        editText.isEnabled = !editText.isEnabled
    }

    private fun disableAllFields() {
        fullnameEditText.isEnabled = false
        usernameEditText.isEnabled = false
        passwordEditText.isEnabled = false
    }
}
