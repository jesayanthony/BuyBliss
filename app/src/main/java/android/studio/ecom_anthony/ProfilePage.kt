package android.studio.ecom_anthony

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

class ProfilePage : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var backgroundImageView: ImageView
    private lateinit var profileImageView: CircleImageView
    private lateinit var usernameTextView: TextView
    private lateinit var userBioTextView: TextView
    private lateinit var editProfileButton: Button
    private lateinit var myVouchersButton: Button
    private lateinit var notificationsButton: Button
    private lateinit var supportCenterButton: Button
    private lateinit var logoutButton: Button

    private lateinit var database: FirebaseDatabase
    private lateinit var userRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_page)

        backgroundImageView = findViewById(R.id.background_image)
        profileImageView = findViewById(R.id.profile_image)
        usernameTextView = findViewById(R.id.username)
        userBioTextView = findViewById(R.id.user_bio)
        editProfileButton = findViewById(R.id.edit_profile_button)
        myVouchersButton = findViewById(R.id.my_vouchers_button)
        notificationsButton = findViewById(R.id.notifications_button)
        supportCenterButton = findViewById(R.id.support_center_button)
        logoutButton = findViewById(R.id.logout_button)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance()
        userRef = database.getReference("user").child("1")

        // Fetch user data from Firebase
        fetchUserData()

        // Set button click listeners
        editProfileButton.setOnClickListener {
            // Handle edit profile click
        }

        myVouchersButton.setOnClickListener {
            // Handle my vouchers click
        }

        notificationsButton.setOnClickListener {
            // Handle notifications click
        }

        supportCenterButton.setOnClickListener {
            // Handle support center click
        }

        logoutButton.setOnClickListener {
            val intent = Intent(this@ProfilePage, LoginPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish() // This will close the current activity and go back to MainActivity
        }
    }

    private fun fetchUserData() {
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                if (user != null) {
                    usernameTextView.text = user.name
                    userBioTextView.text = user.bio

                    // Load images using Glide
                    Glide.with(this@ProfilePage)
                        .load(user.profpic)
                        .into(profileImageView)

                    Glide.with(this@ProfilePage)
                        .load(user.bgpic)
                        .into(backgroundImageView)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors.
            }
        })
    }
}
