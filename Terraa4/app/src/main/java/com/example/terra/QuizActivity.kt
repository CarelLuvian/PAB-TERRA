package com.example.terra

import android.content.Intent
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class QuizActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var linearLayout: LinearLayout
    private lateinit var videoWebView: WebView
    private val radioGroups = mutableListOf<RadioGroup>()
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // Ambil username dari SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        username = sharedPreferences.getString("username", null)
        if (username == null) {
            Toast.makeText(this, "Pengguna tidak ditemukan. Silakan login kembali.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Ambil nama quiz dan video ID dari Intent
        val quizId = intent.getStringExtra("quizId") ?: "quiz1"
        val videoId = intent.getStringExtra("videoId") ?: "video_url" // Default ke video_url1

        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance().reference.child("Quiz").child(quizId)

        // Inisialisasi UI
        videoWebView = findViewById(R.id.web_view)
        linearLayout = findViewById(R.id.quizLinearLayout)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        // Ambil data dari Firebase
        fetchQuizData(quizId, videoId)

        // Tombol Submit
        btnSubmit.setOnClickListener {
            if (radioGroups.any { it.checkedRadioButtonId == -1 }) {
                Toast.makeText(this, "Harap jawab semua pertanyaan sebelum melanjutkan.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Ambil jawaban
            val answers = radioGroups.map {
                findViewById<RadioButton>(it.checkedRadioButtonId).text.toString()
            }

            // Simpan data ke Firebase dan kembali ke halaman katalog
            saveQuizCompletionToDatabase(quizId, answers)
        }
    }

    private fun fetchQuizData(quizId: String, videoId: String) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val videoUrl = snapshot.child(videoId).getValue(String::class.java)
                if (videoUrl == null) {
                    Toast.makeText(this@QuizActivity, "URL video tidak ditemukan!", Toast.LENGTH_SHORT).show()
                } else {
                    setupVideo(videoWebView, videoUrl)
                }

                // Ambil data pertanyaan
                val questions = snapshot.child("questions").children
                for ((index, questionSnapshot) in questions.withIndex()) {
                    val questionText = questionSnapshot.child("text").getValue(String::class.java)
                    val options = questionSnapshot.child("options").children.map { it.getValue(String::class.java) }

                    if (questionText != null && options.isNotEmpty()) {
                        addQuestionToLayout(index + 1, questionText, options as List<String>)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@QuizActivity, "Gagal memuat data kuis: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupVideo(webView: WebView, videoUrl: String?) {
        // Validasi dan ubah URL menjadi format embed
        if (!videoUrl.isNullOrEmpty()) {
            val videoId = extractVideoIdFromUrl(videoUrl)
            val embedUrl = "https://www.youtube.com/embed/$videoId"

            val videoHtml = """
                <iframe width="100%" height="100%" 
                        src="$embedUrl"
                        frameborder="0" allow="accelerometer; autoplay; clipboard-write; 
                        encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen>
                </iframe>
            """.trimIndent()

            webView.settings.javaScriptEnabled = true
            webView.webChromeClient = WebChromeClient()
            webView.loadData(videoHtml, "text/html", "utf-8")
        } else {
            Toast.makeText(this@QuizActivity, "URL video tidak ditemukan!", Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi untuk mengekstrak ID video dari URL YouTube
    private fun extractVideoIdFromUrl(url: String): String {
        val regex = """(?:https?://)?(?:www\.)?(?:youtube\.com/watch\?v=|youtu\.be/)([a-zA-Z0-9_-]+)""".toRegex()
        val matchResult = regex.find(url)
        return matchResult?.groups?.get(1)?.value ?: ""
    }

    private fun addQuestionToLayout(questionNumber: Int, questionText: String, options: List<String>) {
        val textView = TextView(this).apply {
            text = "$questionNumber. $questionText"
            textSize = 16f
            setPadding(0, 16, 0, 8)
        }
        linearLayout.addView(textView)

        val radioGroup = RadioGroup(this).apply {
            orientation = RadioGroup.VERTICAL
        }
        options.forEach { option ->
            val radioButton = RadioButton(this).apply {
                text = option
                textSize = 14f
                setPadding(0, 8, 0, 8)
            }
            radioGroup.addView(radioButton)
        }
        radioGroups.add(radioGroup)
        linearLayout.addView(radioGroup)
    }

    private fun saveQuizCompletionToDatabase(quizId: String, answers: List<String>) {
        username?.let {
            val userQuizRef = FirebaseDatabase.getInstance().reference.child("users").child(it).child("quizzesCompleted")

            // Tambahkan informasi kuis ke node pengguna
            userQuizRef.child(quizId).setValue(answers).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Kuis selesai. Data tersimpan.", Toast.LENGTH_SHORT).show()

                    // Kembali ke halaman katalog
                    startActivity(Intent(this, KatalogActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Gagal menyimpan data: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
