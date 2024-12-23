package com.example.terra

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

class AvatarSelectionActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddAvatar: Button
    private lateinit var btnBack: ImageView
    private lateinit var sharedPreferences: SharedPreferences

    private val avatarList = listOf(
        R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3, R.drawable.avatar4,
        R.drawable.avatar5, R.drawable.avatar6, R.drawable.avatar7, R.drawable.avatar8,
        R.drawable.avatar9, R.drawable.avatar10, R.drawable.avatar11, R.drawable.avatar12,
        R.drawable.avatar13, R.drawable.avatar14, R.drawable.avatar15
    )

    private val CAMERA_REQUEST_CODE = 1001
    private val GALLERY_REQUEST_CODE = 1002
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar_selection)

        recyclerView = findViewById(R.id.rvAvatars)
        btnAddAvatar = findViewById(R.id.btnAddAvatar)
        btnBack = findViewById(R.id.btnBack)

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        recyclerView.layoutManager = GridLayoutManager(this, 3)
        val adapter = AvatarAdapter(avatarList) { selectedAvatarResId ->
            onAvatarSelected(selectedAvatarResId)
        }
        recyclerView.adapter = adapter

        btnBack.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        btnAddAvatar.setOnClickListener {
            showImageSourceDialog()
        }
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Ambil Foto", "Pilih dari Galeri")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Pilih Avatar")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    selectedImageUri = data.data
                    selectedImageUri?.let { uri ->
                        uploadImageToFirebase(uri)
                    }
                }
                CAMERA_REQUEST_CODE -> {
                    val photo = data.extras?.get("data") as? Bitmap
                    photo?.let { bitmap ->
                        uploadImageToFirebase(bitmapToUri(bitmap))
                    }
                }
            }
        }
    }

    private fun uploadImageToFirebase(uri: Uri) {
        val storageReference = FirebaseStorage.getInstance().reference.child("avatars/avatar16.jpg")
        storageReference.putFile(uri)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                    saveAvatarToDatabase(downloadUri.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal mengunggah avatar. Coba lagi.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveAvatarToDatabase(downloadUrl: String) {
        val username = sharedPreferences.getString("username", "default_user") ?: "default_user"
        val database = FirebaseDatabase.getInstance()
        val userAvatarRef = database.getReference("users").child(username).child("avatar")
        userAvatarRef.setValue(downloadUrl)
            .addOnSuccessListener {
                Toast.makeText(this, "Avatar berhasil disimpan!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan avatar. Coba lagi.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun bitmapToUri(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "avatar16", null)
        return Uri.parse(path)
    }

    private fun onAvatarSelected(selectedAvatarResId: Int) {
        val selectedAvatarName = resources.getResourceEntryName(selectedAvatarResId)
        sharedPreferences.edit().putString("selected_avatar", selectedAvatarName).apply()

        val username = sharedPreferences.getString("username", "default_user") ?: "default_user"
        val database = FirebaseDatabase.getInstance()
        val userAvatarRef = database.getReference("users").child(username).child("avatar")
        userAvatarRef.setValue(selectedAvatarName)
            .addOnSuccessListener {
                Toast.makeText(this, "Avatar $selectedAvatarName berhasil disimpan!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan avatar. Coba lagi.", Toast.LENGTH_SHORT).show()
            }
    }
}
