package android.studio.ecom_anthony

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class LoginPage : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var rememberMe: CheckBox
    private lateinit var signInButton: Button
    private lateinit var signUpPrompt: TextView
    private lateinit var errorMessage: TextView

    private lateinit var database: FirebaseDatabase
    private lateinit var userRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        rememberMe = findViewById(R.id.remember_me)
        signInButton = findViewById(R.id.sign_in_button)
        signUpPrompt = findViewById(R.id.sign_up_prompt)
        errorMessage = findViewById(R.id.error_message)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance()
        userRef = database.getReference("user")

        signInButton.setOnClickListener {
            val usernameInput = username.text.toString().trim()
            val passwordInput = password.text.toString().trim()

            if (usernameInput.isNotEmpty() && passwordInput.isNotEmpty()) {
                authenticateUser(usernameInput, passwordInput)
            } else {
                errorMessage.text = "Please enter username and password"
                errorMessage.visibility = View.VISIBLE
            }
        }

        signUpPrompt.setOnClickListener {
            val intent = Intent(this, RegisterPage::class.java)
            startActivity(intent)
        }
    }

    private fun authenticateUser(username: String, password: String) {
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var userFound = false

                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null && user.email == username && user.password == password) {
                        userFound = true
                        // Navigate to MainActivity or another activity
                        val intent = Intent(this@LoginPage, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        break
                    }
                }

                if (!userFound) {
                    errorMessage.text = "Invalid username or password"
                    errorMessage.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                errorMessage.text = "Authentication failed. Please try again."
                errorMessage.visibility = View.VISIBLE
            }
        })
    }
}
