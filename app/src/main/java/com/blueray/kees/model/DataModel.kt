package com.blueray.kees.model

sealed class NetworkResults<out R> {
    data class Success<out T>(val data: T) : NetworkResults<T>()
    data class ErrorMessage<out T>(val data :ErrorResponse?):NetworkResults<T>()
    data class Error(val exception: Exception) : NetworkResults<Nothing>()
}

data class ErrorResponse(
    val status : Int?,
    val message: String,
    val success: Boolean,
    val `data`: Any,
)
data class CustomerData(
    val address: String,
    val area: String,
    val city_id: String,
    val city_name: String,
    val date_of_birth: String,
    val email: String,
    val full_name: String,
    val gender: String,
    val house_image: Any,
    val id: Int,
    val image: Any,
    val latitude: String,
    val longitude: String,
    val otp_is_verified: String,
    val phone: String,
    val otp_code:Int
)
data class RegistrationModel(
    val customer_data: CustomerData,
    val status: Int,
    val success: Boolean,
    val token: String
)
data class LoginResponse(
    val customer_data: CustomerData,
    val status: Int,
    val success: Boolean,
    val token: String
)
data class GetMainCategories(
    val `data`: List<GetMainCategoriesData>,
    val status: Int,
    val success: Boolean
)
data class GetMainCategoriesData(
    val description: Any,
    val id: Int,
    val image: String?,
    val name: String
)
data class GetProductsModel(
    val `data`: List<GetProductsData>,
    val status: Int,
    val success: Boolean
)
data class GetProductsData(
    val category_id: Int,
    val category_name: String,
    val description: String,
    val id: Int,
    val image: String,
    val name: String,
    val on_sale_price: String,
    val on_sale_price_status: String,
    val sale_price: String,
    val sub_category_id: Int,
    val sub_category_name: String
)