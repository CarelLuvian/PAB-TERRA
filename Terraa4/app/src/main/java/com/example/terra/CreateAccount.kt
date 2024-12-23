package com.example.terra

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class CreateAccount : AppCompatActivity() {

    private lateinit var database: DatabaseReference // Reference to Firebase Realtime Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().reference

        // Back Button
        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            finish() // Close this activity and go back to the previous one
        }

        // Input fields
        val fullNameInput = findViewById<EditText>(R.id.full_name_input)
        val usernameInput = findViewById<EditText>(R.id.username_input)
        val dateOfBirthInput = findViewById<TextView>(R.id.date_of_birth_input) // Changed to TextView
        val emailInput = findViewById<EditText>(R.id.email_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)

        // Gender Radio Group
        val genderGroup = findViewById<RadioGroup>(R.id.gender_group)

        // Date of Birth Picker
        dateOfBirthInput.setOnClickListener {
            // Get current date
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Show DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Format selected date as dd/MM/yyyy
                    val formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                    dateOfBirthInput.text = formattedDate
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        // Continue Button
        val continueButton = findViewById<Button>(R.id.continue_button)
        continueButton.setOnClickListener {
            val fullName = fullNameInput.text.toString().trim()
            val username = usernameInput.text.toString().trim()
            val dateOfBirth = dateOfBirthInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            val selectedGenderId = genderGroup.checkedRadioButtonId
            val gender = if (selectedGenderId != -1) {
                findViewById<RadioButton>(selectedGenderId).text.toString()
            } else {
                ""
            }

            // Validate inputs
            if (fullName.isEmpty() || username.isEmpty() || dateOfBirth.isEmpty() || email.isEmpty() || password.isEmpty() || gender.isEmpty()) {
                Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create user data map
            val user = hashMapOf(
                "fullname" to fullName,
                "username" to username,
                "dateOfBirth" to dateOfBirth,
                "email" to email,
                "password" to password
            )

            // Store user data in Firebase Realtime Database
            database.child("users").child(username).setValue(user).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Registrasi gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
