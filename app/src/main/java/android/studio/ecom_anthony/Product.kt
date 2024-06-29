package android.studio.ecom_anthony

data class Product(
    var productId: String = "",
    val name: String? = "",
    val price: Double? = 0.0,
    val description: String? = "",
    val url: String? = "",
    val stock: Long? = 0 // Ensure stock is of type Long
)
