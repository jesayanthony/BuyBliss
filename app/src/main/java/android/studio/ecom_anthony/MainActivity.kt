package android.studio.ecom_anthony

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.database.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), ProductAdapter.OnItemClickListener {

    private lateinit var viewPager: ViewPager2
    private lateinit var leftButton: ImageButton
    private lateinit var rightButton: ImageButton
    private lateinit var dots: Array<View>
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val productList = mutableListOf<Product>()
    private lateinit var nameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Database reference
        val database = FirebaseDatabase.getInstance()
        val productRef = database.getReference("product")
        val userRef = database.getReference("user").child("1")

        viewPager = findViewById(R.id.bannerViewPager)
        leftButton = findViewById(R.id.leftButton)
        rightButton = findViewById(R.id.rightButton)
        dots = arrayOf(
            findViewById(R.id.dot1),
            findViewById(R.id.dot2),
            findViewById(R.id.dot3)
        )
        recyclerView = findViewById(R.id.recyclerView)
        nameTextView = findViewById(R.id.nameTxt)

        val banners = listOf(R.drawable.banner1, R.drawable.banner2, R.drawable.banner3)
        val adapter = BannerAdapter(banners)
        viewPager.adapter = adapter

        leftButton.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem > 0) {
                viewPager.currentItem = currentItem - 1
            }
        }

        rightButton.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem < banners.size - 1) {
                viewPager.currentItem = currentItem + 1
            }
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateDots(position)
            }
        })

        updateDots(0)

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        productAdapter = ProductAdapter(productList, this)
        recyclerView.adapter = productAdapter

        fetchProducts(productRef)
        fetchUserName(userRef)

        val profileIcon: ImageView = findViewById(R.id.profile_icon)
        profileIcon.setOnClickListener {
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
        }

        val cartIcon: ImageView = findViewById(R.id.bell_icon)
        cartIcon.setOnClickListener {
            val intent = Intent(this, CartPage::class.java)
            startActivity(intent)
        }
    }

    private fun fetchProducts(productRef: DatabaseReference) {
        productRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                productList.clear()
                for (snapshot in dataSnapshot.children) {
                    try {
                        val product = snapshot.getValue(Product::class.java)
                        val productId = snapshot.key // Use the Firebase key as productId
                        if (product != null && productId != null) {
                            product.productId = productId // Set the productId in the product object
                            productList.add(product)
                            Log.d("MainActivity", "Product fetched: ${product.name}")
                        } else {
                            Log.d("MainActivity", "Product is null for snapshot: ${snapshot.key}")
                        }
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Failed to convert product", e)
                    }
                }
                productAdapter.notifyDataSetChanged()
                Log.d("MainActivity", "Total products fetched: ${productList.size}")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("MainActivity", "Failed to read product data", databaseError.toException())
            }
        })
    }

    private fun fetchUserName(userRef: DatabaseReference) {
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val name = dataSnapshot.child("name").getValue(String::class.java)
                nameTextView.text = name ?: "User"
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("MainActivity", "Failed to read user data", databaseError.toException())
            }
        })
    }

    private fun updateDots(selectedPosition: Int) {
        for (i in dots.indices) {
            dots[i].setBackgroundResource(
                if (i == selectedPosition) R.drawable.dot_active else R.drawable.dot_inactive
            )
        }
    }

    override fun onItemClick(product: Product) {
        val intent = Intent(this, ProductPage::class.java).apply {
            putExtra("productId", product.productId)
            putExtra("productName", product.name)
            putExtra("productPrice", product.price)
            putExtra("productDescription", product.description)
            putExtra("productImageUrl", product.url)
        }
        startActivity(intent)
    }
}
