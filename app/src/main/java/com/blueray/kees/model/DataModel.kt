package com.blueray.kees.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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
data class NumberOfWeeksModel(
    @SerializedName("lang") val lang :String,
    @SerializedName("number_of_weeks") val numberOfWeeks :List<Int>,
    @SerializedName("start_time") val startTime :String,
    @SerializedName("end_time") val endTime :String,
    @SerializedName("day") val day :String,
)
data class ShiftsModel(
    val `data`: List<ShiftsData>,
    val status: Int,
    val success: Boolean
)
data class ShiftsData(
    val end_time: String,
    val friday: String,
    val id: Int,
    val name: String,
    val start_time: String,
    var selected : Boolean = false,
    val active_days :List<Day>
)
data class Days(
    val dayWeek : Day,
    var selected: Boolean = false
        )

data class Day(
    val day_name : String,
    val day_number : String
)
data class GetWeeklyCartModel(
    val `data`: List<WeeklyBasketData>,
    val status: Int,
    val success: Boolean
)

@Parcelize
data class WeeklyBasketProduct(
    val color_id: Int,
    val color_name: String,
    val id: Int,
    val image: String?,
    val product_id: Int,
    val product_name: String,
    val product_price: String,
    val product_tax: Int,
    val quantity: Int,
    val size_id: Int,
    val size_name: String,
    val unit_id: Int,
    val unit_name: String,
    val weekly_basket_id: Int,
    val weight_id: Int,
    val weight_name: String
):Parcelable
data class WeeklyBasketData(
    val date: String,
    val day: String,
    val end_time: String,
    val id: Int,
    val payment_status: String,
    val process_status: String,
    val start_time: String,
    val sup_total_price: Int,
    val total_price: Double,
    val total_tax: Double,
    val week_number: String,
    var weekly_basket_products: List<WeeklyBasketProduct>,
    var selected : Boolean = false
)

data class AddToBasketRequestBody(
   val lang :String,
   val weekly_basket_ids :List<Int>,
   val product_id :String,
   val quantity :String,
   val color_id :String,
   val size_id :String,
   val unit_id :String,
   val weight_id :String,
)

data class GetProductDetailsResponse(
    val `data`: GetProductDetailsData,
    val status: Int,
    val success: Boolean
)
data class GetProductDetailsData(
    val category_id: Int,
    val category_name: String,
    val color_id: Int,
    val color_name: String,
    val description: String,
    val id: Int,
    val image: String,
    val name: String,
    val on_sale_price: String,
    val on_sale_price_status: String,
    val product_other_image: List<Any>,
    val quantity: Int,
    val sale_price: String,
    val size_id: Int,
    val size_name: String,
    val sub_category_id: Int,
    val sub_category_name: String,
    val tax_percentage: Int,
    val unit_id: Int,
    val unit_name: String,
    val variation_type: String,
    val weight_id: Int,
    val weight_name: String
)
