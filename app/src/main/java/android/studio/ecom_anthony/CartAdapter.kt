package android.studio.ecom_anthony

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CartAdapter(private val cartItems: MutableList<CartItem>, private val context: Context) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.item_image)
        val itemName: TextView = itemView.findViewById(R.id.item_name)
        val itemQuantity: TextView = itemView.findViewById(R.id.item_quantity)
        val itemPrice: TextView = itemView.findViewById(R.id.item_price)
        val incrementButton: View = itemView.findViewById(R.id.increment_button)
        val decrementButton: View = itemView.findViewById(R.id.decrement_button)
        val deleteButton: ImageView = itemView.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        Glide.with(holder.itemView.context).load(cartItem.url).into(holder.itemImage)
        holder.itemName.text = cartItem.name
        holder.itemQuantity.text = cartItem.quantity.toString()
        holder.itemPrice.text = "$${cartItem.price?.times(cartItem.quantity ?: 0) ?: 0.0}"

        holder.incrementButton.setOnClickListener {
            val currentStock = getCurrentStock(cartItem.productId)
            if (currentStock != null && (cartItem.quantity ?: 0) < currentStock) {
                cartItem.quantity = (cartItem.quantity ?: 1) + 1
                updateCartItem(cartItem)
                notifyItemChanged(position)
            }
        }

        holder.decrementButton.setOnClickListener {
            if ((cartItem.quantity ?: 1) > 1) {
                cartItem.quantity = (cartItem.quantity ?: 1) - 1
                updateCartItem(cartItem)
                notifyItemChanged(position)
            }
        }

        holder.deleteButton.setOnClickListener {
            removeCartItem(cartItem, position)
        }
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    private fun updateCartItem(cartItem: CartItem) {
        val database = FirebaseDatabase.getInstance()
        val cartRef = database.getReference("cartitem").child("1").child("cart1").child(cartItem.productId)
        cartRef.setValue(cartItem)
    }

    private fun removeCartItem(cartItem: CartItem, position: Int) {
        val database = FirebaseDatabase.getInstance()
        val cartRef = database.getReference("cartitem").child("1").child("cart1").child(cartItem.productId)
        cartRef.removeValue().addOnSuccessListener {
            cartItems.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartItems.size)
            android.util.Log.d("CartAdapter", "Successfully deleted item from Firebase")
        }.addOnFailureListener { e ->
            android.util.Log.e("CartAdapter", "Failed to delete item from Firebase", e)
        }
    }

    private fun getCurrentStock(productId: String): Int? {
        // This function should return the current stock from Firebase for the given productId
        // You may need to fetch the stock value from the Firebase database and return it here
        // For now, let's assume a placeholder value
        return 10 // Replace this with actual stock fetching logic
    }
}
