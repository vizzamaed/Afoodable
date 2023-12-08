data class SellerData(
    val businessName: String? = null,
    val sellerFullName: String? = null,
    val sellerEmail: String? = null,
    val businessLocation: String? = null,
    val tinNumber: String? = null,
    val dtiNumber: String? = null,
    val bfarNumber: String? = null
) {
    fun isValid(): Boolean {
        return tinNumber?.isNumeric() == true &&
                dtiNumber?.isNumeric() == true &&
                bfarNumber?.isNumeric() == true
    }
}

fun String.isNumeric(): Boolean {
    return this.matches("\\d+".toRegex())
}
