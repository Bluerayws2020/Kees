package com.blueray.kees.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlin.Unit

sealed class NetworkResults<out R> {
    data class Success<out T>(val data: T) : NetworkResults<T>()
    data class ErrorMessage<out T>(val data: ErrorResponse?) : NetworkResults<T>()
    data class Error(val exception: Exception) : NetworkResults<Nothing>()
}

data class ErrorResponse(
    val status: Int?,
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
    val otp_code: Int,
    val token: String
)

data class RegistrationModel(
    val data: CustomerData,
    val status: Int,
    val success: Boolean,

    )

data class LoginResponse(
    val data: CustomerData,
    val status: Int,
    val success: Boolean,
    val message: String

//    @SerializedName("is_authenticated") val isAuth: Boolean? = null
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
    val sub_category_name: String,
    val is_wishlist: Boolean
)

data class NumberOfWeeksModel(
    @SerializedName("lang") val lang: String,
    @SerializedName("number_of_weeks") val numberOfWeeks: List<Int>,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("end_time") val endTime: String,
    @SerializedName("day") val day: String,
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
    var selected: Boolean = false,
    val active_days: List<Day>
)

data class Days(
    val dayWeek: Day,
    var selected: Boolean = false
)

data class Day(
    val day_name: String,
    val day_number: String
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
) : Parcelable

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
    val delivery_fees: Double,
    val services_fees: Double,
    val total_tax: Double,
    val week_number: String,
    var weekly_basket_products: List<WeeklyBasketProduct>,
    var selected: Boolean = false
)

data class AddToBasketRequestBody(
    val lang: String,
    val weekly_basket_ids: List<Int>,
    val product_id: String,
    val quantity: String,
    val color_id: String,
    val size_id: String,
    val unit_id: String,
    val weight_id: String,
    @SerializedName("feature_ids") val feature_ids: List<String>? = null
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
    val weight_name: String,
    val features: List<Features>? = null,
    val is_wishlist: Boolean,
    val variations_multiple: List<VariationsMultiple>,
)

data class VariationsMultiple(
    val color_name: String,
    val on_sale_price: String,
    val on_sale_price_status: String,
    val sale_price: String,
    val size_name: String,
    val unit_name: String,
    var selected: Boolean = false,
    val weight_name: String,
    val color_id: Int,
    val size_id: Int,
    val weight_id: Int,
    val unit_id: Int,
)

data class Features(
    val id: Int,
    val name: String,
    val options: List<Option>
)

data class Option(
    val id: Int,
    val name: String
)

data class CustomerProfileData(
    val address: String,
    val area: String,
    val city_id: Int,
    val city_name: String,
    val date_of_birth: String,
    val email: Any,
    val full_name: String,
    val gender: String,
    val house_image: Any,
    val id: Int,
    val image: Any,
    val latitude: String,
    val longitude: String,
    val otp_code: String,
    val otp_is_verified: String,
    val otp_verified_at: String,
    val phone: String,
    val two_factor_confirmed_at: Any,
    val wallet_balance: String
)

data class CustomerProfileModel(
    val `data`: CustomerProfileData,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class CustomerGetAddressesData(
    val address: String,
    val area: String,
    val city_id: Int,
    val city_name: String,
    val id: Int,
    val latitude: String,
    val longitude: String,
    val title: String,
    val user_id: Int
)

data class CustomerGetAddressesModel(
    val `data`: List<CustomerGetAddressesData>,
    val status: Int,
    val success: Boolean
)

data class AboutUsData(
    val about_us_descriptio: String,
    val about_us_image: String,
    val about_us_title: String,
    val mission: String,
    val user_guide_video: Any,
    val value: String,
    val vision: String
)

data class AboutUsModel(
    val `data`: AboutUsData,
    val status: Int,
    val success: Boolean
)

data class PrivacyPolicyData(
    val description: String,
    val title: String
)

data class PrivacyPolicyModel(
    val `data`: PrivacyPolicyData,
    val status: Int,
    val success: Boolean
)

data class GetMyProfileModel(
    val `data`: GetMyProfileData,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class GetMyProfileData(
    val address: String,
    val area: String,
    val city_id: Int,
    val city_name: String,
    val date_of_birth: String,
    val email: Any,
    val full_name: String,
    val gender: String,
    val house_image: Any,
    val id: Int,
    val image: Any,
    val latitude: String,
    val longitude: String,
    val otp_code: String,
    val otp_is_verified: String,
    val otp_verified_at: String,
    val phone: String,
    val two_factor_confirmed_at: Any,
    val wallet_balance: String
)

data class DriverLoginResponse(
    val data: DriverData,
    val status: Int,
    val success: Boolean,

    )

data class DriverData(
    val city_id: Int,
    val city_name: String,
    val driver_status: String,
    val email: String,
    val full_name: String,
    val id: Int,
    val image: Any,
    val otp_code: String,
    val otp_is_verified: String,
    val otp_verified_at: String,
    val phone: String,
    val token: String
)

data class GetDriverProfileResponse(
    val data: DriverProfileData,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class DriverProfileData(
    val city_id: Int,
    val city_name: String,
    val driver_status: String,
    val email: String,
    val full_name: String,
    val id: Int,
    val image: Any,
    val otp_code: String,
    val otp_is_verified: String,
    val otp_verified_at: String,
    val phone: String
)

data class DriverOrdersResponse(
    val `data`: DriverOrdersData,
    val status: Int,
    val success: Boolean
)

data class DriverOrdersData(
    val under_delivery: List<UnderDelivery>,
    val waiting_for_delivery: List<WaitingForDelivery>
)

data class UnderDelivery(
    val address: String,
    val area: String,
    val coupon_id: Any,
    val coupon_status: String,
    val date: String,
    val day: String,
    val driver_status: String,
    val end_time: String,
    val id: Int,
    val latitude: String,
    val longitude: String,
    val payment_status: String,
    val process_status: String,
    val start_time: String,
    val week_number: String,
    val item_count: Int,
    val total_price: Int
)

data class WaitingForDelivery(
    val address: String,
    val area: String,
    val coupon_id: Any,
    val coupon_status: String,
    val date: String,
    val day: String,
    val driver_status: String,
    val end_time: String,
    val id: Int,
    val latitude: String,
    val longitude: String,
    val payment_status: String,
    val process_status: String,
    val start_time: String,
    val week_number: String,
    val item_count: Int,
    val total_price: Int

)

data class DriverOrderDetailsResponse(
    val `data`: OrderData,
    val status: Int,
    val success: Boolean
)

data class OrderData(
    val coupon_id: Any,
    val coupon_status: String,
    val date: String,
    val day: String,
    val end_time: String,
    val id: Int,
    val note: String,
    val payment_status: String,
    val process_status: String,
    val start_time: String,
    val week_number: String,
    val user_name: String,
    val user_email: String,
    val user_phone: String,
    val latitude: String,
    val longitude: String,
    val weekly_basket_products: List<DriverWeeklyBasketProduct>
)

data class DriverWeeklyBasketProduct(
    val color: Color,
    val color_id: Int,
    val id: Int,
    val product: Product,
    val product_id: Int,
    val quantity: Int,
    val size: Size,
    val size_id: Int,
    val unit: Unit,
    val unit_id: Int,
    val weekly_basket_id: Int,
    val weekly_basket_product_features: List<Any>,
    val weight: Weight,
    val weight_id: Int
)

data class Color(
    val id: Int,
    val name_ar: String,
    val name_en: String
)

data class Product(
    val id: Int,
    val image: Any,
    val name_ar: String,
    val name_en: String,
    val on_sale_price: String,
    val on_sale_price_status: String,
    val sale_price: String,
    val category_id: Int,
    val sub_category_id: Int,
    val category_name: String
)

data class Size(
    val id: Int,
    val name_ar: String,
    val name_en: String
)

data class Unit(
    val id: Int,
    val name_ar: String,
    val name_en: String
)

data class Weight(
    val id: Int,
    val name_ar: String,
    val name_en: String
)

data class UpdateDeliveryStatusResponse(
    val message: String,
    val status: Int,
    val success: Boolean
)

data class FinishedOrdersRespose(
    val `data`: List<FinishedOrderData>,
    val status: Int,
    val success: Boolean
)

data class FinishedOrderData(
    val address: String,
    val area: String,
    val city_id: Int,
    val coupon_id: Any,
    val created_at: String,
    val delivery_total: String,
    val end_total: String,
    val id: Int,
    val latitude: String,
    val longitude: String,
    val payment_method: String,
    val product_quantity: Int,
    val services_total: String,
    val sub_total: String,
    val updated_at: String,
    val user_id: Int,
    val user_name: String,
    val date: String,
    val time: String
)

data class NotificationsResponse(
    val `data`: List<NotificationsData>,
    val status: Int,
    val success: Boolean
)

data class NotificationsData(
    val id: Int,
    val notification_text: String,
    val date: String
)

data class DeleteNotificationsResponse(
    val `data`: String,
    val status: Int,
    val success: Boolean
)

data class CustomerPastOrdersResponse(
    val `data`: List<PastOrderData>,
    val status: Int,
    val success: Boolean,
    val message: String
)

data class PastOrderData(
    val address: String,
    val area: String,
    val city_id: Int,
    val coupon_id: Any,
    val date: String,
    val delivery_total: String,
    val end_total: String,
    val id: Int,
    val latitude: String,
    val longitude: String,
    val payment_method: String,
    val product_quantity: Int,
    val services_total: String,
    val sub_total: String,
    val time: String,
    val user_id: Int,
    val driver_name: String
)

data class FinishedOrderDetailsResponse(
    val `data`: FinishedOrderDetailsData,
    val status: Int,
    val success: Boolean
)

data class FinishedOrderDetailsData(
    val address: String,
    val area: String,
    val city_id: Int,
    val coupon_id: Any,
    val date: String,
    val delivery_total: String,
    val end_total: String,
    val id: Int,
    val latitude: String,
    val longitude: String,
    val payment_method: String,
    val product_quantity: Int,
    val sale_operations: List<SaleOperation>,
    val services_total: String,
    val sub_total: String,
    val time: String,
    val user_id: Int,
    val user_name: String,
    val user_phone: String,
    val day: String,
)

data class SaleOperation(
    val color: Color,
    val color_id: Int,
    val created_at: String,
    val end_total: String,
    val id: Int,
    val product: Product,
    val product_id: Int,
    val quantity: Int,
    val sale_id: Int,
    val size: Size,
    val size_id: Int,
    val sub_total: String,
    val unit: Unit,
    val unit_id: Int,
    val unit_price: String,
    val updated_at: String,
    val weight: Weight,
    val weight_id: Int
)

data class PastOrderDetailsResponse(
    val `data`: PastOrderDetailsData,
    val status: Int,
    val success: Boolean
)

data class PastOrderDetailsData(
    val address: String,
    val area: String,
    val city_id: Int,
    val coupon_id: Any,
    val date: String,
    val delivery_total: String,
    val driver_name: String,
    val end_total: String,
    val id: Int,
    val latitude: String,
    val longitude: String,
    val payment_method: String,
    val product_quantity: Int,
    val sale_operations: List<SaleOperation>,
    val services_total: String,
    val sub_total: String,
    val time: String,
    val user_id: Int,
    val day: String,
    val driver_phone: String

)

data class WalletTransactionResponse(
    val `data`: List<WalletData>,
    val status: Int,
    val success: Boolean
)

data class WalletData(
    val amount: String,
    val id: Int,
    val sale_id: Any,
    val user_id: Int,
    val wallet_balance_after: String,
    val wallet_balance_before: String,
    val wallet_transaction_type: String
)