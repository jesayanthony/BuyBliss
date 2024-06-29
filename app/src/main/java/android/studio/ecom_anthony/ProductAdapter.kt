package android.studio.ecom_anthony


import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import androidx.constraintlayout.widget.ConstraintLayout

class ProductAdapter(private val productList: List<Product>, private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(product: Product)
    }

    // Define a list of colors
    private val colors = listOf(
        "#50FFFFFF", // Red 100
        "#C8E6C9", // Green 100
        "#BBDEFB", // Blue 100
        "#FFECB3", // Yellow 100
        "#D1C4E9"  // Purple 100
    )

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.product_image)
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
        val productLayout: ConstraintLayout = itemView.findViewById(R.id.product_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.productName.text = product.name
        holder.productPrice.text = "$${product.price}"

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(product.url)
            .into(holder.productImage)

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(product)
        }
    }

    override fun getItemCount() = productList.size
}
