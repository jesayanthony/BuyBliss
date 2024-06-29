package android.studio.ecom_anthony

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class CartPage : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private val cartItems = mutableListOf<CartItem>()

    private lateinit var database: FirebaseDatabase
    private lateinit var cartRef: DatabaseReference

    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cart_page)

        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish() // Close the current activity and go back to MainActivity
        }

        recyclerView = findViewById(R.id.cart_items_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(cartItems, this)
        recyclerView.adapter = cartAdapter

        database = FirebaseDatabase.getInstance()
        cartRef = database.getReference("cartitem").child("1").child("cart1")

        fetchCartItems()
    }

    private fun fetchCartItems() {
        cartRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                cartItems.clear()
                for (snapshot in dataSnapshot.children) {
                    val cartItem = snapshot.getValue(CartItem::class.java)
                    if (cartItem != null) {
                        cartItems.add(cartItem)
                    }
                }
                cartAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors.
            }
        })
    }
}
