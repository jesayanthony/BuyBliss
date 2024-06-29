package android.studio.ecom_anthony

import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProductPage : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var productImage: ImageView
    private lateinit var productName: TextView
    private lateinit var productPrice: TextView
    private lateinit var productDescription: TextView
    private lateinit var totalPrice: TextView
    private lateinit var addToCartButton: Button
    private lateinit var quantityText: TextView
    private lateinit var decrementButton: FrameLayout
    private lateinit var incrementButton: FrameLayout

    private lateinit var heartIcon: ImageView

    private var quantity = 1
    private var isHeartFilled = false

    private lateinit var database: FirebaseDatabase
    private lateinit var cartRef: DatabaseReference

    private lateinit var productId: String
    private lateinit var productNameValue: String
    private var productPriceValue: Double = 0.0
    private lateinit var productDescriptionValue: String
    private lateinit var productImageUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_page)

        // Initialize views
        productImage = findViewById(R.id.product_image)
        heartIcon = findViewById(R.id.heart_icon)
        productName = findViewById(R.id.product_name)
        productPrice = findViewById(R.id.product_price)
        productDescription = findViewById(R.id.product_description)
        totalPrice = findViewById(R.id.total_price2)
        addToCartButton = findViewById(R.id.add_to_cart_button)
        quantityText = findViewById(R.id.quantity_text)
        decrementButton = findViewById(R.id.decrement_button)
        incrementButton = findViewById(R.id.increment_button)
        backButton = findViewById(R.id.back_button)

        // Get data from the intent
        productId = intent.getStringExtra("productId") ?: ""
        productNameValue = intent.getStringExtra("productName") ?: ""
        productPriceValue = intent.getDoubleExtra("productPrice", 0.0)
        productDescriptionValue = intent.getStringExtra("productDescription") ?: ""
        productImageUrl = intent.getStringExtra("productImageUrl") ?: ""

        // Set data to views
        productName.text = productNameValue
        productPrice.text = "$$productPriceValue"
        productDescription.text = productDescriptionValue
        totalPrice.text = "$$productPriceValue"

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance()
        cartRef = database.getReference("cartitem").child("1").child("cart1")

        // Load image using Glide
        Glide.with(this)
            .load(productImageUrl)
            .into(productImage)

        // Set click listeners
        backButton.setOnClickListener {
            finish() // Close the current activity and go back to MainActivity
        }

        decrementButton.setOnClickListener {
            if (quantity > 1) {
                quantity--
                quantityText.text = quantity.toString()
                updateTotalPrice()
            }
        }

        incrementButton.setOnClickListener {
            quantity++
            quantityText.text = quantity.toString()
            updateTotalPrice()
        }

        heartIcon.setOnClickListener {
            isHeartFilled = !isHeartFilled
            heartIcon.setImageResource(
                if (isHeartFilled) R.drawable.ic_heart_filled else R.drawable.ic_heart_unfilled
            )
        }

        addToCartButton.setOnClickListener {
            addToCart(productId, productNameValue, productPriceValue, quantity, productImageUrl)
        }
    }

    private fun updateTotalPrice() {
        val total = quantity * productPriceValue
        totalPrice.text = "$$total"
    }

    private fun addToCart(productId: String, name: String, price: Double, quantity: Int, url: String) {
        val cartItem = CartItem(productId, name, price, quantity, url)
        val cartItemId = cartRef.push().key ?: productId // Generate a unique ID for the cart item
        cartRef.child(cartItemId).setValue(cartItem)
            .addOnSuccessListener {
                // Successfully added to cart
            }
            .addOnFailureListener { e ->
                // Failed to add to cart
            }
    }
}
