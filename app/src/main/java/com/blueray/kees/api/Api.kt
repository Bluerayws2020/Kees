package com.blueray.kees.api

import com.blueray.kees.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @Multipart
    @POST("customer/register")
    suspend fun register(
        @Part("lang") Lang : RequestBody,
        @Part("full_name") fullName : RequestBody,
        @Part("gender") gender : RequestBody,//1 if male 2 if female
        @Part("date_of_birth") date_of_birth : RequestBody,
        @Part("latitude") latitude : RequestBody,
        @Part("longitude") longitude : RequestBody,
        @Part("city_id") city_id : RequestBody,
        @Part("area") area : RequestBody,
        @Part("address") address : RequestBody,
        @Part("password") password : RequestBody,
        @Part("password_confirmation") password_confirmation : RequestBody,
        @Part image : MultipartBody.Part? = null,
        @Part("email") email : RequestBody? = null,
        @Part("phone") phone : RequestBody? = null,
        @Part house_image : MultipartBody.Part? = null
    ) : Response<RegistrationModel>

    @Multipart
    @POST("customer/sendOtpCodeRequest")
    suspend fun sendOtpCode(
        @Header("Authorization") auth: String,
        @Part("otp_code") otpCode : RequestBody,
        @Part("lang") lang :RequestBody
    ):Response<ErrorResponse>
    @Multipart
    @POST("customer/login")
    suspend fun login(
        @Part("lang") lang : RequestBody,
        @Part("email") email :RequestBody,
        @Part("password") password :RequestBody
    ):Response<LoginResponse>

    @Multipart
    @POST("frontend/getCategories")
    suspend fun getMainCategories(
        @Part("lang") lang : RequestBody
    ):Response<GetMainCategories>
    @Multipart
    @POST("frontend/getSubCategories")
    suspend fun getSubCategories(
        @Part("lang") lang : RequestBody,
        @Part("category_id") category_id : RequestBody
    ):Response<GetMainCategories>
    @Multipart
    @POST("frontend/getProducts")
    suspend fun getProducts(
        @Part("lang") lang : RequestBody,
        @Part("category_id") category_id : RequestBody?,
        @Part("text_search") text_search: RequestBody?
    ):Response<GetProductsModel>
    @POST("customer/chooseNumberOfWeeklyBasketsAfterRegister")
    @Headers("Content-Type: application/json","Accept: application/json")
    suspend fun chooseNumberOfWeeklyBasketsAfterRegister(
        @Header("Authorization") auth: String,
        @Body chooseNumberOfWeeks : NumberOfWeeksModel
    ):Response<ErrorResponse>

    @Multipart
    @POST("frontend/getShifts")
    suspend fun getShifts(
        @Part("lang") lang : RequestBody
    ):Response<ShiftsModel>
    @Multipart
    @POST("customer/getWeeklyBaskets")
    suspend fun getWeeklyCart(
        @Part("lang") lang : RequestBody,
        @Header("Authorization") auth: String
    ):Response<GetWeeklyCartModel>

    @POST("customer/addProductToWeeklyBaskets")
    @Headers("Content-Type: application/json","Accept: application/json")
    suspend fun addProductToWeeklyBaskets(
        @Header("Authorization") auth: String,
        @Body data : AddToBasketRequestBody
    ):Response<ErrorResponse>

    @Multipart
    @POST("frontend/getProductDetails")
    suspend fun getProductDetails(
        @Part("lang") lang : RequestBody,
        @Part("product_id") productId : RequestBody
    ):Response<GetProductDetailsResponse>
    @Multipart
    @POST("customer/addRemoveWishlistProduct")
    suspend fun addRemoveWishlistProduct(
        @Header("Authorization") auth: String,
        @Part("product_id") productId: RequestBody,
        @Part("lang") lang: RequestBody,
    ):Response<ErrorResponse>
    @POST("customer/getWishlistProducts")
    suspend fun getWishlistProducts(
        @Header("Authorization") auth: String
    ):Response<GetProductsModel>
}