package com.blueray.kees.api

import com.blueray.kees.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface
Api {

    @Multipart
    @POST("customer/register")
    suspend fun register(
        @Part("lang") Lang: RequestBody,
        @Part("full_name") fullName: RequestBody,
        @Part("gender") gender: RequestBody,//1 if male 2 if female
        @Part("date_of_birth") date_of_birth: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("city_id") city_id: RequestBody,
        @Part("area") area: RequestBody,
        @Part("address") address: RequestBody,
        @Part("password") password: RequestBody,
        @Part("password_confirmation") password_confirmation: RequestBody,
        @Part image: MultipartBody.Part? = null,
        @Part("email") email: RequestBody? = null,
        @Part("phone") phone: RequestBody? = null,
        @Part house_image: MultipartBody.Part? = null
    ): Response<RegistrationModel>

    @Multipart
    @POST("customer/sendOtpCodeRequest")
    suspend fun sendOtpCode(
        @Part("otp_code") otpCode: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("lang") lang: RequestBody
    ): Response<ErrorResponse>

    @Multipart
    @POST("customer/login")
    suspend fun login(
        @Part("lang") lang: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody
    ): Response<LoginResponse>

    @Multipart
    @POST("frontend/getCategories")
    suspend fun getMainCategories(
        @Part("lang") lang: RequestBody
    ): Response<GetMainCategories>

    @Multipart
    @POST("frontend/getSubCategories")
    suspend fun getSubCategories(
        @Part("lang") lang: RequestBody,
        @Part("category_id") category_id: RequestBody
    ): Response<GetMainCategories>

    @Multipart
    @POST("frontend/getProducts")
    suspend fun getProducts(
        @Header("Authorization") auth: String,
        @Part("lang") lang: RequestBody,
        @Part("category_id") category_id: RequestBody?,
        @Part("sub_category_id") sub_category_id: RequestBody?,
        @Part("text_search") text_search: RequestBody?
    ): Response<GetProductsModel>

    @POST("customer/chooseNumberOfWeeklyBasketsAfterRegister")
    @Headers("Content-Type: application/json", "Accept: application/json")
    suspend fun chooseNumberOfWeeklyBasketsAfterRegister(
        @Header("Authorization") auth: String,
        @Body chooseNumberOfWeeks: NumberOfWeeksModel
    ): Response<ErrorResponse>

    @Multipart
    @POST("frontend/getShifts")
    suspend fun getShifts(
        @Part("lang") lang: RequestBody
    ): Response<ShiftsModel>

    @Multipart
    @POST("customer/getWeeklyBaskets")
    suspend fun getWeeklyCart(
        @Part("lang") lang: RequestBody,
        @Header("Authorization") auth: String
    ): Response<GetWeeklyCartModel>

    @POST("customer/addProductToWeeklyBaskets")
    @Headers("Content-Type: application/json", "Accept: application/json")
    suspend fun addProductToWeeklyBaskets(
        @Header("Authorization") auth: String,
        @Body data: AddToBasketRequestBody
    ): Response<ErrorResponse>

    @POST("customer/checkoutMultiWeeklyBasket")
    @Headers("Content-Type: application/json", "Accept: application/json")
    suspend fun checkOutMultiBaskets(
        @Header("Authorization") auth: String,
        @Body data: checkOutMultiBasketRequestBody
    ): Response<ErrorResponse>

    @Multipart
    @POST("frontend/getProductDetails")
    suspend fun getProductDetails(
        @Header("Authorization") auth: String,
        @Part("lang") lang: RequestBody,
        @Part("product_id") productId: RequestBody
    ): Response<GetProductDetailsResponse>

    @Multipart
    @POST("customer/addRemoveWishlistProduct")
    suspend fun addRemoveWishlistProduct(
        @Header("Authorization") auth: String,
        @Part("product_id") productId: RequestBody,
        @Part("lang") lang: RequestBody,
    ): Response<AddRemoveWishListResponse>

    @POST("customer/getWishlistProducts")
    suspend fun getWishlistProducts(
        @Header("Authorization") auth: String
    ): Response<GetProductsModel>

    @POST("customer/getMyProfile")
    suspend fun getCustomerProfile(
        @Header("Authorization") auth: String
    ): Response<CustomerProfileModel>

    @Multipart
    @POST("customer/changeMyPhoneNumber")
    suspend fun customerChangePhoneNumber(
        @Header("Authorization") auth: String,
        @Part("new_phone") newPhone: RequestBody
    ): Response<ErrorResponse>

    @Multipart
    @POST("customer/getMyAddresses")
    suspend fun customerGetMyAddresses(
        @Header("Authorization") auth: String,
        @Part("lang") lang: RequestBody
    ): Response<CustomerGetAddressesModel>

    @Multipart
    @POST("customer/updateMyAddress")
    suspend fun customerUpdateMyAddress(
        @Header("Authorization") auth: String,
        @Part("lang") lang: RequestBody,
        @Part("address_id") addressId: RequestBody,
        @Part("title") title: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("city_id") cityId: RequestBody,
        @Part("area") area: RequestBody,
        @Part("address") address: RequestBody,
    ): Response<ErrorResponse>

    @Multipart
    @POST("customer/addNewAddress")
    suspend fun customerAddNewAddress(
        @Header("Authorization") auth: String,
        @Part("lang") lang: RequestBody,
        @Part("title") title: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("city_id") cityId: RequestBody,
        @Part("area") area: RequestBody,
        @Part("address") address: RequestBody,
    ): Response<ErrorResponse>

    @Multipart
    @POST("customer/deleteMyAddress")
    suspend fun customerDeleteMyAddress(
        @Header("Authorization") auth: String,
        @Part("lang") lang: RequestBody,
        @Part("address_id") addressId: RequestBody
    ): Response<ErrorResponse>

    @Multipart
    @POST("frontend/getAboutUs")
    suspend fun getAboutUs(
        @Part("lang") lang: RequestBody
    ): Response<AboutUsModel>

    @Multipart
    @POST("frontend/getPrivacyAndPolicy")
    suspend fun getPrivacyPolicy(
        @Part("lang") lang: RequestBody
    ): Response<PrivacyPolicyModel>

    @Multipart
    @POST("customer/getMyProfile")
    suspend fun getMyProfile(
        @Header("Authorization") auth: String,
        @Part("lang") lang: RequestBody,
    ): Response<GetMyProfileModel>

    @Multipart
    @POST("customer/changeMyPassword")
    suspend fun changeMyPassword(
        @Header("Authorization") auth: String,
        @Part("lang") lang: RequestBody,
        @Part("old_password") old_password: RequestBody,
        @Part("password") password: RequestBody,
        @Part("password_confirmation") password_confirmation: RequestBody,
    ): Response<ErrorResponse>

    @Multipart
    @POST("driver/login")
    suspend fun driverLogin(
        @Part("lang") lang: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody
    ): Response<DriverLoginResponse>

    @POST("driver/getMyProfile")
    suspend fun getDriverProfile(
        @Header("Authorization") auth: String,

        ): Response<GetDriverProfileResponse>

    @POST("driver/getMyOrders")
    suspend fun getDriverOrders(
        @Header("Authorization") auth: String,

        ): Response<DriverOrdersResponse>

    @Multipart
    @POST("driver/getDriverOrderDetails")
    suspend fun getDriverOrderDetails(
        @Header("Authorization") auth: String,
        @Part("weekly_basket_id") weekly_basket_id: RequestBody,
        @Part("lang") lang: RequestBody
    ): Response<DriverOrderDetailsResponse>

    @Multipart
    @POST("driver/updateDeliveryStatus")
    suspend fun updateDeliveryStatus(
        @Header("Authorization") auth: String,
        @Part("weekly_basket_id") weekly_basket_id: RequestBody
    ): Response<UpdateDeliveryStatusResponse>

    @Multipart
    @POST("driver/finishOrder")
    suspend fun finishOrder(
        @Header("Authorization") auth: String,
        @Part("weekly_basket_id") weekly_basket_id: RequestBody
    ): Response<UpdateDeliveryStatusResponse>

    @Multipart
    @POST("frontend/contactUsRequest")
    suspend fun contactUsRequest(
        @Part("lang") lang: RequestBody,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("subject") subject: RequestBody,
        @Part("message") message: RequestBody,
    ): Response<UpdateDeliveryStatusResponse>

    @POST("driver/getFinishOrders")
    suspend fun getFinishedOrders(
        @Header("Authorization") auth: String,
    ): Response<FinishedOrdersRespose>

    @POST("frontend/getMyNotifications")
    suspend fun getNotifications(
        @Header("Authorization") auth: String,

        ): Response<NotificationsResponse>

    @Multipart
    @POST("customer/checkoutSingleWeeklyBasket")
    suspend fun checkOutSingleBasket(
        @Header("Authorization") auth: String,
        @Part("lang") lang: RequestBody,
        @Part("weekly_basket_id") weekly_basket_id: RequestBody,
        @Part("coupon_code") coupon_code: RequestBody,
        @Part("address_id") address_id: RequestBody,
        @Part("title") title: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("city_id") city_id: RequestBody,
        @Part("area") area: RequestBody,
        @Part("address") address: RequestBody,
        @Part("note") note: RequestBody
    ): Response<UpdateDeliveryStatusResponse>

    @Multipart
    @POST("frontend/deleteNotificationsById")
    suspend fun deleteNotificationsById(
        @Header("Authorization") auth: String,
        @Part("notification_id") notification_id: RequestBody
    ): Response<DeleteNotificationsResponse>

    @POST("frontend/deleteAllNotifications")
    suspend fun deleteAllNotifications(
        @Header("Authorization") auth: String,
    ): Response<DeleteNotificationsResponse>

    @POST("customer/getPastOrders")
    suspend fun customerPastOrders(
        @Header("Authorization") auth: String,
    ): Response<CustomerPastOrdersResponse>

    @Multipart
    @POST("driver/getFinishOrderDetails")
    suspend fun driverFinishedOrderDetails(
        @Header("Authorization") auth: String,
        @Part("order_id") order_id: RequestBody,
        @Part("lang") lang: RequestBody
    ): Response<FinishedOrderDetailsResponse>

    @Multipart
    @POST("customer/cancelWeeklyBasketPayment")
    suspend fun cancelBasketPayment(
        @Header("Authorization") auth: String,
        @Part("lang") lang: RequestBody,
        @Part("weekly_basket_id") weekly_basket_id: RequestBody
    ): Response<ErrorResponse>

    @Multipart
    @POST("customer/getPastOrderDetails")
    suspend fun pastOrderDetails(
        @Header("Authorization") auth: String,
        @Part("order_id") order_id: RequestBody,
        @Part("lang") lang: RequestBody
    ): Response<PastOrderDetailsResponse>

    @Multipart
    @POST("customer/resetPassword")
    suspend fun resetPassword(
        @Header("Authorization") auth: String,
        @Part("phone") phone: RequestBody
    ): Response<ErrorResponse>

    @Multipart
    @POST("customer/resetPasswordRequest")
    suspend fun resetPasswordRequest(
        @Header("Authorization") auth: String,
        @Part("phone") phone: RequestBody,
        @Part("otp_code") otp_code: RequestBody,
        @Part("password") password: RequestBody,
        @Part("password_confirmation") password_confirmation: RequestBody,

        ): Response<ErrorResponse>

    @Multipart
    @POST("customer/addWeeklyBasket")
    suspend fun addWeeklyBasket(
        @Header("Authorization") auth: String,
        @Part("number_of_week") number_of_week: RequestBody,
        @Part("start_time") start_time: RequestBody,
        @Part("end_time") end_time: RequestBody,
        @Part("day") day: RequestBody
    ): Response<ErrorResponse>

    @Multipart
    @POST("customer/getMyWalletTransactions")
    suspend fun getMyWalletTransactions(
        @Header("Authorization") auth: String,
        @Part("lang") lang: RequestBody
    ): Response<WalletTransactionResponse>

    @Multipart
    @POST("customer/updateProductWeeklyBasket")
    suspend fun weeklyBasketUpdateProduct(
        @Header("Authorization") auth: String,
        @Part("lang") lang: RequestBody,
        @Part("weekly_basket_id") weekly_basket_id: RequestBody,
        @Part("weekly_basket_product_id") weekly_basket_product_id: RequestBody,
        @Part("quantity") quantity: RequestBody
    ): Response<ErrorResponse>

    @Multipart
    @POST("frontend/getContactUs")
    suspend fun getContactUs(
        @Part("lang")lang: RequestBody
    ): Response<ContactUsInfo>
}

