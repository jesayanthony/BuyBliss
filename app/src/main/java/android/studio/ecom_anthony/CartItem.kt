package android.studio.ecom_anthony

data class CartItem(
    val productId: String = "",
    val name: String = "",
    val price: Double? = null,
    var quantity: Int? = 1,
    val url: String = ""
)
