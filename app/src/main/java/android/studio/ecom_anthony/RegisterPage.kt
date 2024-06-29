package android.studio.ecom_anthony

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterPage : AppCompatActivity() {

    private lateinit var registerUsername: EditText
    private lateinit var registerMobileNumber: EditText
    private lateinit var registerPassword: EditText
    private lateinit var signUpButton: Button
    private lateinit var signInPrompt: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)

        registerUsername = findViewById(R.id.register_username)
        registerMobileNumber = findViewById(R.id.register_mobile_number)
        registerPassword = findViewById(R.id.register_password)
        signUpButton = findViewById(R.id.sign_up_button)
        signInPrompt = findViewById(R.id.sign_in_prompt)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        signUpButton.setOnClickListener {
            val usernameText = registerUsername.text.toString()
            val mobileNumberText = registerMobileNumber.text.toString()
            val passwordText = registerPassword.text.toString()

            registerUser(usernameText, mobileNumberText, passwordText)
        }

        signInPrompt.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser(username: String, mobileNumber: String, password: String) {
        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""
                    val user = User(
                        bgpic = "default_bgpic_url", // Set default or fetch from input
                        bio = "default_bio", // Set default or fetch from input
                        email = username,
                        name = "default_name", // Set default or fetch from input
                        password = password,
                        profpic = "default_profpic_url" // Set default or fetch from input
                    )

                    database.child("user").child(userId).setValue(user)
                        .addOnSuccessListener {
                            Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LoginPage::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to register user: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    data class User(
        val bgpic: String,
        val bio: String,
        val email: String,
        val name: String,
        val password: String,
        val profpic: String
    )
}
